#include <WProgram.h>
#include "communication.h"

void packet_send(struct packet_t * __packet) {
	uint8_t i;

	// Starts transmission.
	Serial.write(__packet->start);
	Serial.write(__packet->flag);
	Serial.write(__packet->p_id);
	Serial.write(__packet->d_size);

	for (i = 0; i < __packet->d_size; ++i) {
		Serial.write(__packet->data[i]);
	}

	Serial.write(__packet->checksum);
	Serial.write(__packet->end);
}

void packet_checksum(struct packet_t * __packet) {
	uint8_t i, check = 0;
	
	check ^= __packet->start;
	check ^= __packet->flag;
	check ^= __packet->p_id;
	check ^= __packet->d_size;

	for (i = 0; i < __packet->d_size; ++i) {
		check ^= __packet->data[i];
	}

	check ^= __packet->end;

	__packet->checksum = check;
}

bool packet_validate(struct packet_t * __packet) {
	uint8_t tmp_check;

	tmp_check = __packet->checksum;
	__packet->checksum = 0;

	packet_checksum(__packet);

	if (__packet->checksum == tmp_check) {
		return true;
	}
	
	__packet->checksum = tmp_check;
	return false;
}