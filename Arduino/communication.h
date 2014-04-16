#ifndef COMMUNICATION_H
#define COMMUNICATION_H
#include <stdlib.h>
#include <stdint.h> 

struct packet_t {
	uint8_t start;
	uint8_t flag;
	uint8_t p_id;
	uint8_t d_size;
	uint8_t * data;
	uint8_t checksum;
	uint8_t end;
};

void packet_send(struct packet_t * __packet);
void packet_checksum(struct packet_t * __packet);
bool packet_validate(struct packet_t * __packet);

#endif