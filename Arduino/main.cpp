#include <WProgram.h>
#include <stdlib.h>
#include <stdint.h>
#include "communication.h"

struct buffer_t {
	size_t size;
	size_t length;
	size_t seek;
	uint8_t * content;
};

void buffer_init(struct buffer_t * __buffer) {
	__buffer->length = 0;
	__buffer->seek = 0;
	__buffer->size = 4;

	__buffer->content = (uint8_t *) malloc(__buffer->size * sizeof(uint8_t));
}

void buffer_append(struct buffer_t * __buffer, uint8_t __byte) {
	if (__buffer->length == __buffer->size) {
		__buffer->size += 4;

		__buffer->content = (uint8_t *) realloc(__buffer->content, (__buffer->size * sizeof(uint8_t)));
	}

	__buffer->content[__buffer->length++] = __byte;
}

uint8_t buffer_read(struct buffer_t * __buffer) {
	if (__buffer->seek < __buffer->length) {
		return __buffer->content[__buffer->seek++];
	}

	return 0;
}

void buffer_clear(struct buffer_t * __buffer) {
	__buffer->length = 0;
	__buffer->size = 0;

	free(__buffer->content);
	__buffer->content = NULL;
} 

buffer_t * buffer;
packet_t packet;

void thread();
void read();

int main() {
	// Initialize board, GPIO, Serial, etc.
	init();
	pinMode(13, OUTPUT);

	char * data = "";

	// Prepare pre-configure package.
	packet.start = 0x2;
	packet.flag = 0x6;
	packet.p_id = 0x0;
	packet.d_size = 0x0;
	packet.data = (uint8_t *) data;
	packet.checksum = 0;
	packet.end = 0x3;

	packet_checksum(&packet);

	// Initialize buffer.
	buffer_init(buffer);

	// Initialize serial communication at 115200 bauds.
	Serial.begin(115200);

	// Send pre-configure package
	//packet_send(&packet);

	// Main thread
	for (;;) {
		thread();
	}
}

void thread() {
	// It has bytes on queue. When it has got ETX byte, read it.
	if (Serial.available() > 0) {
		uint8_t recv = (uint8_t) Serial.read();

		if (recv != 0x3) {
			buffer_append(buffer, recv);
		} else {
			buffer_append(buffer, (uint8_t) 0x3);

			read();
		}

		
	}

packet_send(&packet);
	//packet_send(&packet);
	//Serial.println("HelloWorld");
}

void read() {
	// Do something with the data.
	digitalWrite(13, HIGH);

	buffer_clear(buffer);
}