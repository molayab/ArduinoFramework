#include "communication.h"
#include <string.h>
#include <stdlib.h>

Communication::Communication() {
	packetCount = 0;
}

Communication::~Communication() {

}

void Communication::send(uint8_t * packet) {

}

void Communication::recv() {

}

bool Communication::isValid(uint8_t * packet) {

}

void Communication::checksum(uint8_t * packet) {
	uint8_t checksum = 0;
	size_t length = (sizeof(packet) / sizeof(uint8_t));

	for (int i = 0; i < length; ++i) {
		checksum ^= packet[i];
	}

	packet[length - 2] = checksum;
}

uint8_t * Communication::build(uint8_t * packet, packet_flag flag) {
	uint8_t * p;
	size_t len = sizeof(packet) / sizeof(uint8_t);

	p = (uint8_t *) malloc((len + 6) * sizeof(uint8_t));

	p[0] = (uint8_t) 2;
	p[1] = (uint8_t) flag;
	p[2] = (uint8_t) packetCount++;
	p[3] = (uint8_t) len;

	//memcpy(p + 4, packet, len);

	p[len + 4] = 0;
	p[len + 5] = (uint8_t) 3;

	checksum(p);

	Serial.print((int)p[0]);
	Serial.print(" ");
	Serial.print((int)p[1]);
	Serial.print(" ");
	Serial.print((int)p[2]);
	Serial.print(" ");
	Serial.print((int)p[3]);
	Serial.print(" ");
	Serial.print((int)p[len + 4]);
	Serial.println("");

	return p;
}
