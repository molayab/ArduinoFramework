//
//  Led.java
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//

package ArduinoFramework;

import java.io.IOException;

public class Led extends Arduino {
	private int pin;
	
	public Led(int pin) {
		super();
		setMode(Arduino.OUTPUT);
		setType(Arduino.DIGITAL);
		
		
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
	
	public void power(float val) {
		try {
			write(pin, (int) (val * 100));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(int pin, int data) { }
}
