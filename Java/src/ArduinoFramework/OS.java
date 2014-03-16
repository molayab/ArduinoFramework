/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ArduinoFramework;

/**
 *
 * @author molayab
 */
public class OS {
    private static final String uname = System.getProperty("os.name").toLowerCase();
    
    public static boolean isWindows() {
        return (uname.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (uname.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (uname.indexOf("nix") >= 0
                || uname.indexOf("nux") >= 0
                || uname.indexOf("aix") > 0);
    }

    public static boolean isSolaris() {
        return (uname.indexOf("sunos") >= 0);
    }
}
