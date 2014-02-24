//
//  Main.cpp
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//
#include "WProgram.h"
#define PACKET_HEADER_SIZE 4

void process (uint8_t * packet);

int main() {
    init();
    
    Serial.begin(9600);

    for (;;) {
    	uint8_t buffer[PACKET_HEADER_SIZE];

    	while (Serial.available() < PACKET_HEADER_SIZE);
    	for (int i = 0; i < PACKET_HEADER_SIZE; ++i) buffer[i] = Serial.read();

    	process(buffer);
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
	 *   --> MODE: Modo del pin: OUTPUT=0xFF, INPUT=D9
	 *   --> VALUE: Valor de salida del pin (uint8_t) 0-255 [0x00-0xFF]
	 *   --> TYPE: Tipo de salida: DIGITAL=0xFF, ANALOG=0xD9
	 */
	pinMode(packet[0], packet[1]);

	if (packet[3] == 0xFF) digitalWrite(packet[0], packet[2]);
	else analogWrite(packet[0], packet[2]);
}
