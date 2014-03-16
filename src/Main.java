
import ArduinoFramework.Communication;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author molayab
 */
public class Main {
    public static void main(String[] args) {
        try {
            Communication comm = Communication.getInstance();
            
            byte[] sender = "Este es mi primer mensaje: Hola Mundo!".getBytes();
            
            byte[] data = comm.makePacket((byte)0x5, sender);
            
            
            System.out.println("Data length: " + data.length);
            System.out.println("Checksum: " + Integer.toHexString(data[data.length - 2]));
            System.out.println("CHECK: " + ((comm.checksum(data, data[data.length - 2])) ? "YES" : "NO"));
            
            System.out.println("\nPACKET: ");
            
            int i = 0;
            for (byte b : data) {
                System.out.println(String.format("%3s", Integer.toHexString(i)) 
                        + ": "
                        + String.format("%8s", Integer.toBinaryString(b)).replace(" ", "0")
                        + " " + String.format("%2s", Integer.toHexString(b)) 
                        + " -> " + (char)b);
                i += 8;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
} 
