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
    
    private SerialPort port;
    private String portName;
    
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
    }
    
    /**
     * Conecta un dispositivo y lo prepara para su posterior uso, es 
     * importante cerrar la conexión con el dispositivo una ves finalizado los
     * envío/recepción de datos.
     * 
     * @throws TooManyListenersException Demasiados Listeners en espera.
     * @throws UnsupportedCommOperationException Comunicación no soportada.
     * @throws PortInUseException Puerto en uso, conexión fallida.
     */
    public void connect() throws PortInUseException, 
            UnsupportedCommOperationException, TooManyListenersException {
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
            UnsupportedCommOperationException, TooManyListenersException {
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
    
    public void send(byte[] data) {
        
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

    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            System.out.println("RECV: ");
        }
    }
}
