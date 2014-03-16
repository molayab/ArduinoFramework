ArduinoFramework
================

Arduino Framework comunica Java con Arduino pudiendo controlar con estos todos los pines desde java, programando así un elementos externos a la maquina.

Instalación
===========

Es necesario instalar unas dependencias antes de poder usar el Framework.

Descargar las bibliotecas necesarias:
..* [**RXTXXomm.jar**](http://arduino.googlecode.com/files/arduino-1.0.5-src.tar.gz)
..* Windows: [**rxtxSerial.dll (32 Bits)**](http://jlog.org/v4/windows/win32/rxtxSerial.dll)
..* Windows: [**rxtxSerial.dll (64 Bits)**](http://arduino.googlecode.com/files/arduino-1.0.5-src.tar.gz)
..* Mac OSX: [**librxtxSerial.jnilib**](http://blog.iharder.net/wp-content/uploads/2009/08/librxtxSerial.jnilib)

###Copiar en el path:

**Mac OSX:** 
*RXTXComm.jar* en */Library/Java/Extensions* y **librxtxSerial.jnilib** en */Library/Java/Extensions*

**Windows:** 
*rxtxSerial.dll* en c:\Windows\System32

###Expandir el path:

**Mac OSX:** 

```bash
export DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:/Library/Java/Extensions
```



