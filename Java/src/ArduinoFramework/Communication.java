/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ArduinoFramework;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Clase encargada de la comunicacion via Serial entre Java y Arduino
 * @author molayab
 */
public class Communication implements SerialPortEventListener {
    private static Communication instance = null;
    private final String BUNDLE_ID = "ardujava_1.0";
    private final int TIMEOUT = 1;
    private final int BAUDRATE = 115200;
    
    private final byte ACK = 0x6;
    private final byte NAK = 0x15;
    private final byte ENQ = 0x5;
    private final byte STX = 0x2;
    private final byte ETX = 0x3;

    private final int NUMBER_OF_RETIES = 3;
    
    private SerialPort port;
    private String portName;
    private byte packetCount;
    private InputStream inputStream;
    private byte[] buffer; 
    private int numberOfRetriesLeft;
    private int contRead;
    private boolean cond1;
    private boolean cond2;
    
    protected Communication() throws Exception {
        if (OS.isWindows()) {
            portName = "COM";
        } else if (OS.isUnix()) {
            portName = "/dev/tty";
        } else if (OS.isMac()) {
            portName = "/dev/tty.usbmodem";
        } else {
            throw new Exception("Controlador no encontrado.");
        }

        numberOfRetriesLeft = NUMBER_OF_RETIES;
        contRead = 0;
    }
    
    /**
     * Conecta un dispositivo y lo prepara para su posterior uso, es 
     * importante cerrar la conexión con el dispositivo una ves finalizado los
     * envío/recepción de datos.
     * 
     * @throws TooManyListenersException Demasiados Listeners en espera.
     * @throws UnsupportedCommOperationException Comunicación no soportada.
     * @throws PortInUseException Puerto en uso, conexión fallida.
     * @throws IOException Excepcion en el input/output (Stream).
     */
    public void connect() throws PortInUseException, 
            UnsupportedCommOperationException, TooManyListenersException, IOException {
        Enumeration<?> ports = getPortsAvailable();
        CommPortIdentifier id = null;

        while (id == null && ports.hasMoreElements()) {
            CommPortIdentifier portId
                    = (CommPortIdentifier) ports.nextElement();
            
            if (portId.getName().equals(portName) 
                    || portId.getName().startsWith(portName)) {
                
                configure(portId);
            }
        }
    }
    
    private void configure(CommPortIdentifier portId) throws PortInUseException, 
            UnsupportedCommOperationException, TooManyListenersException, IOException {
        if (port == null) {
            /*
             * Posibles causa de excepciones:
             * -> Puerto en uso: PortInUseException;
             * -> Comunicacion no soportada: UnsupportedCommOperationException
             * -> Muchos Listeners: TooManyListenersException
             */
            port = (SerialPort) portId.open(BUNDLE_ID, TIMEOUT);
            port.setSerialPortParams(BAUDRATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            inputStream = port.getInputStream(); 
            
            port.addEventListener(this);
            port.notifyOnDataAvailable(true);
        }
    }
    
    public boolean isConnected() {
        return (port != null);
    }
    
    /**
     * (Singleton) Crea una nueva instancia de la comunicación si esta no existe
     * en caso que exista devuelve la instancia.
     * 
     * @return Instancia de la clase Communication
     * @throws Exception OS no es compatible con las librerías.
     */
    public static Communication getInstance() throws Exception {
        if (instance == null) instance = new Communication();
        
        return instance;
    }
    
    public boolean checksum(byte[] packet, byte checksum) {
        byte tmp = packet[packet.length - 2];
        packet[packet.length - 2] = 0;
        boolean ret = (makeChecksum(packet) == checksum);
        packet[packet.length - 2] = tmp;
        
        return ret;
    }
    
    /**
     * Crea el Checksum del paquete.
     * Metodo: Sumas progresivas;
     * @param packet Paquete a calcular.
     * @return Byte Checksum.
     */
    private byte makeChecksum(byte[] packet) {
        byte checksum = 0;
        
        for (byte data : packet) {
            checksum ^= data;
        }
        
        return checksum;
    }
    
    /**
     * Crea un nuevo paquete para ser enviado al dispositivo.
     * 
     * @param flag Byte de estado ENQ, ANK, NAK (ENQ: Para request).
     * @param data Bytes a enviar.
     * @return Paquete con los datos a eviar (MAX 256 bytes).
     * @throws IndexOutOfBoundsException Se súpero el tamaño maximo del contenido. (MAX 250 bytes).
     */
    public byte[] build(byte flag, byte[] data) throws IndexOutOfBoundsException {
        byte[] packet = new byte[data.length + 6];
        
        /*
         * Se crea la cabecera del paquete que contiene 4 bytes
         * [ STX | TYPE | #PCK | DAT_SIZE ]
         */
        
        if (data.length <= 250) {
            packet[0] = STX;
            packet[1] = flag;
            packet[2] = (byte)(packetCount++ & 0xFF);
            packet[3] = (byte)(0xFF & data.length);
            
            System.arraycopy(data, 0, packet, 4, data.length);
            
            packet[4 + data.length] = 0;
            packet[5 + data.length] = ETX;
            
            packet[4 + data.length] = makeChecksum(packet);
            
            return packet;
        } else {
            throw new IndexOutOfBoundsException("Se supero el tamano maximo del paquete.");
        }
    }
    
    public static int getIntegerValue(byte frame) {
        return (frame & 0xFF);
    }
    
    public synchronized void send(byte[] data) throws IOException {
        numberOfRetriesLeft = NUMBER_OF_RETIES;
        // while(numberOfRetriesLeft>0) {
        port.getOutputStream().write(build(ENQ, data));
        //TODO: espera paquete respuesta
        cond1 = false;
        cond2 = false;
        while (!cond1 && !cond2) {
            try {
                wait();
            } catch(InterruptedException ie) {
            }

            if (isValidPacket(buffer)) {
                if (buffer[1] == NAK) {

                    port.getOutputStream().write(build(ENQ, data));
                    numberOfRetriesLeft--;
                } else if(buffer[1] == ACK) {
                    buffer = null;
                    cond1 = true;
                    return ;
                }
            } else {
                System.out.println("Error on communication, retrie #" + (NUMBER_OF_RETIES - numberOfRetriesLeft) +
                        " of " + NUMBER_OF_RETIES);
                numberOfRetriesLeft--;
            }
            if (numberOfRetriesLeft == 0) { 
                cond2 = true;
            }
        }
    // }

        System.out.println("Communication Error: number of retries exeded");
        buffer = null;
    }
    
    /**
     * Puertos disponibles para la comunicación serial.
     * 
     * @return Enumeration Puertos disponibles.
     */
    public Enumeration<?> getPortsAvailable() {
        return CommPortIdentifier.getPortIdentifiers();
    }
    
    public void close() {
        if (port != null) {
            port.removeEventListener();
            port.close();
        }
    }
    
    public boolean isValidPacket(byte[] packet) {
        if (packet[0] == STX && packet[packet.length - 1] == ETX)
            return checksum(packet, packet[packet.length - 2]);
        
        return false;
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                int cur = inputStream.read();
                
                buffer[contRead] = (byte) cur;
                contRead++;
                
                if(cur == ETX){
                   notify();
                } 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
