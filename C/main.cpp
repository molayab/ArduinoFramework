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

typedef struct {
	uint8_t pin;
	uint8_t mode;
	uint8_t value;
	uint8_t type;
} packet_t;

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

void process (packet_t * packet);
void write(packet_t * packet);
void read();

mapper m;

int main() {
    init();
    
    init_mapper(&m, 2);
    Serial.begin(115200);

    for (;;) {
    	uint8_t buffer[PACKET_HEADER_SIZE];

    	if (Serial.available() >= PACKET_HEADER_SIZE) {
    		for (int i = 0; i < PACKET_HEADER_SIZE; ++i) buffer[i] = Serial.read() & 0xFF;
    		packet_t p;
    		p.pin = buffer[0];
    		p.mode = buffer[1];
    		p.value = buffer[2];
    		p.type = buffer[3];

    		process(&p);
    	}

    	read();
    }
    
    return 0;
}

void process (packet_t * packet) {
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

	if (packet->mode == 0xD9) {
		pin_map p;
		p.pin = packet->pin;
		p.value = 0;

		add_mapper(&m, p);

		pinMode(packet->pin, INPUT );
	} else {
		pinMode(packet->pin, OUTPUT);
	}

	if (packet->type == 0xFF) digitalWrite(packet->pin, packet->value);
	else analogWrite(packet->pin, packet->value);
}

void read() {
	for (int i = 0; i < m.length; ++i) {
		pin_map p = m.data[i];

		Serial.println(p.pin);
	}

	// delay(100);
}

void write(packet_t * packet) {
	Serial.write(packet->pin);
	Serial.write(packet->mode);
	Serial.write(packet->value);
	Serial.write(packet->type);
}
