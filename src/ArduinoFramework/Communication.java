package ArduinoFramework;

public class Communication {
	private static Communication instance = null;
	
	protected Communication() {
		
	}
	
	public static Communication getInstance() {
		if (instance == null) {
			instance = new Communication();
		}
		
		return instance;
	}
	
	public synchronized void send(byte[] data) {
		
	}	
}
