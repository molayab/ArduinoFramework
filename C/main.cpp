#include "WProgram.h"

int main() {
    init();
    
    pinMode(13, OUTPUT);
    
    for (;;) {
        digitalWrite(13, HIGH);
        delay(100);
        digitalWrite(13, LOW);
        delay(100);
    }
    
    return 0;
}