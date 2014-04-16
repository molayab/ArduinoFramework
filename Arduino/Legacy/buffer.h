//
//  buffer.h
//  
//
//  Created by Mateo Olaya Bernal on 3/16/14.
//
//

#ifndef BUFFER_H_
#define BUFFER_H_

#include <stdlib.h>
#include <stdint.h>

class BufferStream {
private:
	uint8_t * bytes;
	size_t size;
	size_t length;
	size_t seek;

public:
	BufferStream();
	~BufferStream();
	void appendByte(uint8_t byte);
	uint8_t read();
	uint8_t * readAll() const;
	size_t getLength();
	void flush();
};

#endif