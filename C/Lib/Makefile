AVR=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
AVR_CC=$(AVR)/avr-gcc
AVR_CXX=$(AVR)/avr-g++
MCU=-mmcu=atmega328p
CPU_SPEED=-DF_CPU=16000000UL
AVR_FLAGS=$(MCU) $(CPU_SPEED) -Os -w
BOARD=arduino
LIBNAME=lib$(BOARD).a
LIBDIR=Hardware/

OBJECTS=pins_arduino.o wiring.o wiring_analog.o wiring_digital.o wiring_pulse.o wiring_shift.o HardwareSerial.o Print.o Tone.o WMath.o WString.o WInterrupts.o forward.o

default: $(OBJECTS)
	@echo "[COMPILING] ==>"
	avr-ar rcs $(LIBNAME) $^
	mkdir -p $(LIBDIR)
	cp *.h $(LIBDIR)/
	mv $(LIBNAME) $(LIBDIR)
	@echo "[ OK ]"
	@echo "[CLEANING] ==>"
	rm *.o
	@echo "[ OK ]"

%.o : %.c
	$(AVR_CC) $< $(AVR_FLAGS) -c -o $@

%.o : %.cpp
	$(AVR_CXX) $< $(AVR_FLAGS) -c -o $@
