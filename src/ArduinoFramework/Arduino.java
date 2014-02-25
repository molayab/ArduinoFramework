//
//  Arduino.java
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//

package ArduinoFramework;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;


public abstract class Arduino implements SerialPortEventListener, Runnable {
	public static final int OUTPUT = 0xFF;
	public static final int INPUT = 0x00;
	public static final int DIGITAL = 0xFF;
	public static final int ANALOG = 0x00;
	public static final int PACKET_SIZE = 2;
	private static final String PORT_NAMES[] = { 
	    	"/dev/tty.usbmodem", 	// OSX
//	    	"/dev/tty", 	// Linux
//	    	"COM", 			// Windows
		};
	private final int TIMEOUT = 1;
	
	private BufferedReader input;
	private int mode;
	private int type;
	private int[] data = new int[2];
	private int pointer = 0;
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
		        	init(comm_id, 115200);
		            break;
		        }
		    }
		}
	}
	
	public Arduino(CommPortIdentifier comm) {
		init(comm, 115200);
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
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void write(byte[] data) throws IOException {
		synchronized (this) {
			port.getOutputStream().write(data);
		}
	}
	
	public void write(int pin, int value) throws IOException {
		synchronized (this) {
			byte[] send = {(byte)pin, (byte)mode, (byte)value, (byte)type};
			
			port.getOutputStream().write(send);
		}
	}
	
	public void close() {
		if (port != null) {
			port.removeEventListener();
			port.close();
		}
	}
	
	public Enumeration<?> getPortsAvailable() {
		return CommPortIdentifier.getPortIdentifiers();
	}

	public abstract void read(int pin, int data);

	@Override
	public synchronized void serialEvent(SerialPortEvent arg0) {
		if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			synchronized (this) {
				try {
					if (pointer < PACKET_SIZE) {
						data[pointer] = input.read();
						++pointer;
						System.out.println(pointer);
					} else {
						read(data[0], data[1]);
						pointer = 0;
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		if (arg0.getEventType() == SerialPortEvent.BI) {
			System.out.println("BI <---");
		}
	}
	
	@Override
	public void run() {
		try {
			input  = new BufferedReader(new InputStreamReader(port.getInputStream()));
			
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);
			port.notifyOnBreakInterrupt(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
