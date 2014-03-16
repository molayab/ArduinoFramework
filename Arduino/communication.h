#ifndef _COMM_GUARD
#define _COMM_GUARD

#include <WProgram.h>
#include "buffer.h"

typedef uint8_t packet_t;

class Communication {
private:
	BufferStream buffer;

public:
	Communication();
	~Communication();

	void send(packet_t * packet);
	bool isValid(packet_t * packet);
	bool checksum(packet_t * packet);
	uint8_t makeChecksum(packet * packet);
};

#endif