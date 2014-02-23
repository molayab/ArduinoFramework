package ArduinoFramework;

import java.io.IOException;

public class Led extends Arduino {
	private int pin;
	
	public Led(int pin) {
		super();
		
		this.pin = pin;
	}
	
	public void on() {
		try {
			write(pin, 255);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void off() {
		try {
			write(pin, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(int data) {
		
	}
}
