//
//  Main.cpp
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//
#include <WProgram.h>
#include "communication.h"
#include "buffer.h"

int main() {
    init();
    
    Serial.begin(115200);

    BufferStream b;
    Communication comm;

    uint8_t * p;
    uint8_t data[] = {1, 2, 3, 4, 5, 6, 7};

    for(;;) {
    	

    	while (Serial.available() > 0) {
    		uint8_t s = Serial.read();
    		b.appendByte(s);
    	}

    	p = comm.build(data, ACK);
    	free(p);
    	p = NULL;

    	delay(50);
    	b.flush();
    }
    
    return 0;
}