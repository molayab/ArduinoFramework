# Preparar el compilador para AVR (AtMega328 Arduino)
AVR=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
AVR_CXX=$(AVR)/avr-g++
INCLUDE=-I Lib/Hardware/
LIBS=-L Lib/Hardware -lm -larduino
MCU=-mmcu=atmega328p
CPU_SPEED=-DF_CPU=16000000UL
CFLAGS=$(MCU) $(CPU_SPEED) -Os -w -Wl,--gc-sections -ffunction-sections -fdata-sections
PORT=/dev/tty.usbmodem1411 
AVR_CONFIG=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/etc/avrdude.conf

default: build upload clean

## Compilar el codigo:
START=main

build: $(START).hex

main.hex: $(START).elf
	$(AVR)/avr-objcopy -O ihex $< $@

OBJECTS=

main.elf: $(START).cpp $(OBJECTS)
	$(AVR_CXX) $(CFLAGS) $(INCLUDE) $^ -o $@ $(LIBS)

upload:
	$(AVR)/avrdude -C$(AVR_CONFIG) -V -F -p m328p -c arduino -b 115200 -Uflash:w:$(START).hex -P$(PORT)

clean:
	rm *.elf
	rm *.hex

%.o: %.cpp
	$(CXX) $< $(CFLAGS) $(INCLUDE) -c -o $@
