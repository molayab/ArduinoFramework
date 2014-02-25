//
//  Button.java
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//

package ArduinoFramework;

import java.io.IOException;

public class Button extends Arduino {
	private int pin;
	private boolean value;
	
	public Button(int pin) {
		super();
		
		this.pin = pin;
		
		init();
	}
	
	public boolean getValue() {
		return value;
	}
	
	public synchronized void init() {
		try {
			setMode(Arduino.INPUT);
			setType(Arduino.DIGITAL);
			write(pin, 0xD9);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(int pin, int data) {
		System.out.println(pin + " " + data);
	}
}
