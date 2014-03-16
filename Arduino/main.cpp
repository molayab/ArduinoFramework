//
//  Main.cpp
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//
#include <WProgram.h>
#include "buffer.h"

int main() {
    init();
    
    Serial.begin(115200);

    BufferStream b;

    for(;;) {

    	while (Serial.available() > 0) {
    		uint8_t s = Serial.read();
    		b.appendByte(s);
    	}

    	for (int i = 0; i < b.getLength(); ++i) {
    		Serial.print((char) b.read());
    	}



    	b.flush();
    }
    
    return 0;
}