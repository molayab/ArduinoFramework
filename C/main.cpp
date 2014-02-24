//
//  Main.cpp
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//
#include "WProgram.h"
#define PACKET_HEADER_SIZE 4

uint8_t * pin_map;
uint8_t pin_count = 0;

void process (uint8_t * packet);
void read();

int main() {
    init();
    
    Serial.begin(115200);

    for (;;) {
    	uint8_t buffer[PACKET_HEADER_SIZE];

    	if (Serial.available() >= PACKET_HEADER_SIZE) {
    		for (int i = 0; i < PACKET_HEADER_SIZE; ++i) buffer[i] = Serial.read();
    		process(buffer);
    	}

    	read();
    }
    
    return 0;
}

void process (uint8_t * packet) {
	/*
	 * Paquete de 4bytes que contiene la informacion necesaria para
	 * la salida digital
	 *
	 * Paquete (4bytes)
	 * [PORT_NO][MODE][VALUE][TYPE]
	 *
	 *   --> PORT_NO: Numero del pin (uint8_t) 0-255 [0x00-0xFF]
	 *   --> MODE: Modo del pin: OUTPUT=0xFF, INPUT=0x00
	 *   --> VALUE: Valor de salida del pin (uint8_t) 0-255 [0x00-0xFF]
	 *   --> TYPE: Tipo de salida: DIGITAL=0xFF, ANALOG=0x00
	 */
	if (packet[1] == 0) {
		uint8_t i = 0;
		uint8_t __map[++pin_count];

		for(; i < pin_count - 1; ++i) {
			__map[i] = pin_map[i];
		}

		__map[i] = packet[0];
		pin_map = __map;

		pinMode(packet[0], INPUT );
	} else {
		pinMode(packet[0], OUTPUT);
	}

	if (packet[3] == 0xFF) digitalWrite(packet[0], packet[2]);
	else analogWrite(packet[0], packet[2]);
}

void read() {
	pinMode(3, INPUT);

	//Serial.write(0x03);
	//Serial.write(digitalRead(3));
}
