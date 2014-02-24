//
//  Button.java
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//

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
