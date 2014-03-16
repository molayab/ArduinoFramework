¿Como compilar?
===============

Para poder compilar mediante un archivo .cpp/.c es necesario descargar avr-gcc, avr-g++, avrdude (Todos los puede encontar en el IDE de Arduino). Una vez instalados los programas necesarios, es necesario configurar los Makefile(s), el primero de ellos esta en la carpeta donde va el codigo del arduino en c/c++, compila el codigo y lo monta al arduino, el segundo se encuentra en la carpeta lib/ y es el encargado de compilar la libreria necesaria para usar las funciones del IDE original del Arduino como pinMode(), digitalWrite(), etc.

Ambos Makefile deben llevar la misma configuracion (Datos del Arduino). Para compilar cualquier codigo, primero hay que genarar la libreria (libarduino.a).

Configuración
=============

**Path a AVR**
Path donde se encuentran instalados los compiladores AVR. (Por defecto vienen en el IDE de Arduino)

```bash
AVR=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
```
**Path a AVRDUDE**
Path donde se encuentra instalado AVRDUDE el uploader. (Por defecto vienen en el IDE de Arduino)

```bash
AVR_DUDE=/Applications/Arduino.app/Contents/Resources/Java/hardware/tools/avr/bin
```

**Puerto Serie**
Puerto donde esta conectado el arduino. (Por defecto: tty.usb*)

```bash
PORT=/dev/tty.usb*
```

**MCU**
Microcontroladorm, es muy importante configurar bien este. (Por defecto: Arduino Uno -> atmega328p)

Microcontroladores:
Arduino Uno -> atmega328p
Arduino Mega 2560 -> atmega2560

```bash
MCU=atmega328p
```

**Velocidad**
La velocidad de reloj del Arduino (Por defecto: 16MHz)

```bash
CPU_SPEED=-DF_CPU=16000000UL
```