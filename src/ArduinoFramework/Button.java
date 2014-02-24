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
	private boolean value;
	
	public Button(int pin) {
		super();
		
		setMode(Arduino.INPUT);
		
		try {
			write(pin, 0xD9);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getValue() {
		return value;
	}

	@Override
	public void read(int pin, int data) {
		System.out.println(pin + " " + data);
	}
}
