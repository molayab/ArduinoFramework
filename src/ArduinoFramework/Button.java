package ArduinoFramework;

public class Button extends Arduino {
	private boolean value;
	
	public boolean getValue() {
		return value;
	}
	
	@Override
	public void read(int data) {
		value = (data == 0xFF) ? true : false;
	}
}
