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
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Clase encargada de la comunicacion via Serial entre Java y Arduino
 * @author molayab
 */
public class Communication implements SerialPortEventListener {
    private static Communication instance = null;
    private final String BUNDLE_ID = "ardujava_1.0";
    private final int TIMEOUT = 128;
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
    private BufferedReader inputStream;
    private ByteArrayOutputStream buffer; 
    private int numberOfRetriesLeft;
    private int contRead;
    private boolean cond1;
    private boolean cond2;
    
    protected Communication() throws Exception {
        if (OS.isWindows()) {
            Log.notice("Using Windows; will connect to COM*");
            portName = "COM";
        } else if (OS.isUnix()) {
            Log.notice("Using UNIX-LIKE maybe Linux; will connect to /dev/tty*");
            portName = "/dev/tty";
        } else if (OS.isMac()) {
            Log.notice("Using Darwin (UNIX-LIKE) maybe OSX; will connect to /dev/tty.usbmodem*");
            portName = "/dev/tty.usbmodem";
        } else {
            Log.error("Driver not found.");
            throw new Exception("Controlador no encontrado.");
        }

        buffer = new ByteArrayOutputStream();
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
        
        Log.notice("Connecting...");

        while (id == null && ports.hasMoreElements()) {
            CommPortIdentifier portId
            = (CommPortIdentifier) ports.nextElement();
            
            if (portId.getName().equals(portName)
                || portId.getName().startsWith(portName)) {

                Log.notice("Conneced to " + portId.getName());

                if (portId != null) {
                    configure(portId);
                    Log.ok("Configure port to use.");
                } else {
                    Log.error("Could not find COM or TTY port.");
                }

                return;
            }
        }

        Log.error("Link unsuccessful.");
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

            inputStream = new BufferedReader(new InputStreamReader(port.getInputStream()));

            port.addEventListener(this);
            port.notifyOnDataAvailable(true);

            Log.ok("Link successful");
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
        port.getOutputStream().write(build(ENQ, data));

        cond1 = false;
        cond2 = false;
        
        while (!cond1 && !cond2) {
            try {
               wait();
            } catch(InterruptedException ie) { }

            byte[] buff = buffer.toByteArray();

            if (isValidPacket(buff)) {
                if (buff[1] == NAK) {

                    port.getOutputStream().write(build(ENQ, data));
                    numberOfRetriesLeft--;
                } else if(buff[1] == ACK) {
                    buffer.reset();
                    cond1 = true;
                    return ;
                }
            } else {
                Log.error("Error on communication, retrie #" + (NUMBER_OF_RETIES - numberOfRetriesLeft) +
                        " of " + NUMBER_OF_RETIES);

                numberOfRetriesLeft--;
            }
            if (numberOfRetriesLeft == 0) { 
                cond2 = true;
            }
        }

        Log.error("Communication Error: number of retries exeded");
        buffer.reset();
    }
    
    /**
     * Puertos disponibles para la comunicación serial.
     *
     * @return Enumeration Puertos disponibles.
     */
    public Enumeration<?> getPortsAvailable() {
        return CommPortIdentifier.getPortIdentifiers();
    }
    
    public synchronized void close() {
        if (port != null) {
            port.removeEventListener();
            port.close();
        }
    }
    
    public boolean isValidPacket(byte[] packet) {
        if (packet[0] == STX && packet[packet.length - 1] == ETX) {
            return checksum(packet, packet[packet.length - 2]);
        }
        
        return false;
    }
    
    @Override
    public synchronized void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                byte cur = (byte)(inputStream.read() & 0xFF);

                Log.notice("Read byte -> " + String.format("%2s", Integer.toHexString(cur)), true);
                
                buffer.write(cur);

                if(cur == ETX){
                    Log.notice("Received x03: End of text flag.");
                    
                    byte[] buff = buffer.toByteArray();

                    if (isValidPacket(buff)) {
                        Log.ok("Packet is valid, wait...");


                    } else {
                        Log.error("Packet isn't valid.");

                        // ENVIAR NAK
                    }

                    //buffer.reset();
                   notify();
                } 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
