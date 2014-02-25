//
//  Main.cpp
//  ArduinoFramework Java
//
//  Created by Mateo Olaya Bernal on 1/22/14.
//  Copyright (c) 2014 Mateo Olaya Bernal. All rights reserved.
//
#include "WProgram.h"
#define PACKET_HEADER_SIZE 4

typedef struct {
	uint8_t pin;
	uint8_t value;
} pin_map;

typedef struct {
	pin_map * data;
	size_t length;
	size_t size;
} mapper;

/**
 * Se definen las funciones del Mapper [Struct]
 */

void init_mapper(mapper * m, size_t s) {
	m->data = (pin_map *) malloc(s * sizeof(pin_map));
	m->length = 0;
	m->size = s;
}

void add_mapper(mapper * m, pin_map p) {
	if (m->length == m->size) {
		m->size += 2;
		m->data = (pin_map *) realloc(m->data, m->size * sizeof(pin_map));
	}

	m->data[m->length++] = p;
}

/********************************************************/

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
		pinMode(packet[0], INPUT );
	} else {
		pinMode(packet[0], OUTPUT);
	}

	if (packet[3] == 0xFF) digitalWrite(packet[0], packet[2]);
	else analogWrite(packet[0], packet[2]);
}

void read() {
	pinMode(3, INPUT);

	Serial.write(0x03);
	Serial.write(digitalRead(3));
}
