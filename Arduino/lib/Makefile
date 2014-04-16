## CONFIG MAKEFILE
AVR=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
AVR_CC=$(AVR)/avr-gcc
AVR_CXX=$(AVR)/avr-g++
AVR_AR=$(AVR)/avr-ar
MCU=atmega328p
CPU_SPEED=16000000UL
BOARD=arduino
##########################################

AVR_FLAGS=-mmcu=$(MCU) -DF_CPU=$(CPU_SPEED) -Os -w
LIBNAME=lib$(BOARD).a
LIBDIR=bin/

OBJECTS=pins_arduino.o wiring.o wiring_analog.o wiring_digital.o wiring_pulse.o wiring_shift.o HardwareSerial.o Print.o Tone.o WMath.o WString.o WInterrupts.o forward.o

default: $(OBJECTS)
	$(AVR_AR) rcs $(LIBNAME) $^
	mkdir -p $(LIBDIR)
	cp *.h $(LIBDIR)
	mv $(LIBNAME) $(LIBDIR)
	rm *.o

%.o : %.c
	$(AVR_CC) $< $(AVR_FLAGS) -c -o $@

%.o : %.cpp
	$(AVR_CXX) $< $(AVR_FLAGS) -c -o $@

clean:
	rm -rf bin
