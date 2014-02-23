package ArduinoFramework;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;


public abstract class Arduino implements SerialPortEventListener {
	public static final int OUTPUT = 0;
	public static final int INPUT = 1;
	private static final String PORT_NAMES[] = { 
	    	"/dev/tty.usbmodem", 	// OSX
//	    	"/dev/tty", 	// Linux
//	    	"COM", 			// Windows
		};
	private final int TIMEOUT = 1;
	
	private BufferedReader input;
	private OutputStream output;
	private static SerialPort port;
	
	public Arduino() {
		Enumeration<?> ports = getPortsAvailable();
		CommPortIdentifier id = null;
		
		while (id == null && ports.hasMoreElements()) {
		    CommPortIdentifier comm_id = 
		        (CommPortIdentifier) ports.nextElement();
		    for (String portName : PORT_NAMES) {
		        if ( comm_id.getName().equals(portName) 
		          || comm_id.getName().startsWith(portName)) 
		        {
		        	init(comm_id, 9600);
		            break;
		        }
		    }
		}
	}
	
	public Arduino(CommPortIdentifier comm) {
		init(comm, 9600);
	}
	
	public Arduino(CommPortIdentifier comm, int baud) {
		init(comm, baud);
	}
	
	private void init(CommPortIdentifier comm, int baud) {
		if (port == null) {
			try {
				port = (SerialPort) comm.open("Arduino Java", TIMEOUT);
				port.setSerialPortParams(baud, 
						SerialPort.DATABITS_8, 
						SerialPort.STOPBITS_1, 
						SerialPort.PARITY_NONE);
				
				output = port.getOutputStream();
				input  = new BufferedReader(new InputStreamReader(port.getInputStream()));
				
				port.addEventListener(this);
				port.notifyOnDataAvailable(true);
				port.notifyOnBreakInterrupt(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void write(byte[] data) throws IOException {
		port.getOutputStream().write(data);
	}
	
	public void write(int pin, int value) throws IOException {
		byte[] send = {(byte) pin, (byte) value};
		
		port.getOutputStream().write(send);
	}
	
	public synchronized void close() {
		if (port != null) {
			port.removeEventListener();
			port.close();
		}
	}
	
	public Enumeration<?> getPortsAvailable() {
		return CommPortIdentifier.getPortIdentifiers();
	}

	public abstract void read(int data);

	@Override
	public synchronized void serialEvent(SerialPortEvent arg0) {
		if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				read(input.read());
				
				String inputLine=input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		if (arg0.getEventType() == SerialPortEvent.BI) {
			System.out.println("BI <---");
		}
	}
}
