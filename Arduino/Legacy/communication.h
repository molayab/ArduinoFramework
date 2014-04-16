#ifndef _COMM_GUARD
#define _COMM_GUARD

#include <WProgram.h>
#include "buffer.h"

typedef enum packet_flag {
	ACK = 0x6,
	NAK = 0x15,
	ENQ = 0x5,
	STX = 0x2,
	ETX = 0x3
} packet_flag;

class Communication {
private:
	BufferStream buffer;
	uint8_t packetCount;

public:
	Communication();
	~Communication();

	void send(uint8_t * packet);
	void recv();
	bool isValid(uint8_t * packet);
	void checksum(uint8_t * packet);
	uint8_t * build(uint8_t * packet, packet_flag flag);
};

#endif