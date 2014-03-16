## CONFIG MAKEFILE
# Preparar el compilador para AVR (AtMega328 Arduino)

# Path donde esta instalado AVR y AVRDUDE (Por defecto: Arduino.app); 
# si esta en el PATH del sistema dejar vacio.
AVR=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
AVR_DUDE=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
# Archivo de configuracion de AVRDUDE (Por defecto: en Arduino.app)
AVR_CONFIG=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/etc/avrdude.conf
# Compilador de C++/AVR si el path de AVR es vecio remover $(AVR)
AVR_CXX=$(AVR)/avr-g++
AVR_CC=$(AVR)/avr-gcc
# AVRDUDE PORT Puerto donde esta conectado el Arduino.
PORT=/dev/tty.usb*

##
# MCU Microcontroladro
# atmega2560 :: Arduino Mega 2560
# atmega328p :: Arduino UNO
##
MCU=atmega328p
##
# Velocidad de reloj del arduino;
# Por defecto: 16MHz -> 16000000UL
##
CPU_SPEED=-DF_CPU=16000000UL
################################################################

INCLUDE=-I lib/bin
LIBS=-L lib/bin -lm -larduino

##
# Configurar AVRDUDE (BOOTLOADER)
##

CFLAGS=-mmcu=$(MCU) $(CPU_SPEED) -Os -w -Wl,--gc-sections -ffunction-sections -fdata-sections

default: build upload clean

## Compilar el codigo:
OBJECTS=buffer.o communication.o

default: build upload

build: main.hex

main.hex: main.elf
	$(AVR)/avr-objcopy -O ihex $< $@

main.elf: main.cpp $(OBJECTS)
	$(AVR_CXX) $(CFLAGS) $(INCLUDE) $^ -o $@ $(LIBS)

## DEPENDENCIAS ##################################
buffer.o: buffer.cpp
	$(AVR_CXX) -c buffer.cpp $(CFLAGS) $(INCLUDE)

communication.o: communication.cpp
	$(AVR_CXX) -c communication.cpp $(CFLAGS) $(INCLUDE)

##################################################
upload:
	$(AVR_DUDE)/avrdude -C$(AVR_CONFIG) -V -F -p $(MCU) -c arduino -b 115200 -Uflash:w:main.hex -P $(PORT)

clean:
	@echo -n Cleaning ...
	$(shell rm Test.elf 2> /dev/null)
	$(shell rm Test.hex 2> /dev/null)
	$(shell rm *.o 2> /dev/null)
	@echo " done"

#%.o: %.cpp
#	$(CXX) $< $(CFLAGS) $(INCLUDE) -c -o $@
#
#%.o: %.c
#	$(AVR_CC) $< $(CFLAGS) $(INCLUDE) -c -o $@
