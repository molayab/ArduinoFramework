//
//  buffer.cpp
//  
//
//  Created by Mateo Olaya Bernal on 3/16/14.
//
//

#include "buffer.h"

BufferStream::BufferStream() {
	size = 2;
	seek = 0;
	length = 0;

	bytes = (uint8_t *) malloc(size * sizeof(uint8_t));
}

BufferStream::~BufferStream() {
	free(bytes);
	bytes = NULL;
}

void BufferStream::appendByte(uint8_t byte) {
	if (size == length) {
		size += 2;

		bytes = (uint8_t *) realloc(bytes, (size * sizeof(uint8_t)));
	}

	bytes[length++] = byte;
}

uint8_t BufferStream::read() {
	if (seek <= length)
	{
		return bytes[seek++];
	}
	
	return 0;
}

uint8_t * BufferStream::readAll() const {
	return bytes;
}

size_t BufferStream::getLength() {
	return length;
}

void BufferStream::flush() {
	free(bytes);

	size = 0;
	length = 0;
	seek = 0;
	bytes = NULL;
}
