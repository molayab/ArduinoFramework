package ArduinoFramework;

class Log {
	public final static String REDB = "\033[1;41m";
	public final static String REDF = "\033[31m";
	public final static String GREENB = "\033[1;42m";
	public final static String GREENF = "\033[1;32m";
	public final static String YELLOWB = "\033[1;43m";
	public final static String YELLOWF = "\033[1;33m";
	public final static String BLUEB = "\033[1;44m";
	public final static String BLUEF = "\033[1;34m";
	public final static String MAGENTAB = "\033[1;45m";
	public final static String MAGENTAF = "\033[1;35m";
	public final static String CYANB = "\033[1;46m";
	public final static String CYANF = "\033[1;36m";
	public final static String WHITEB = "\033[1;47m";
	public final static String WHITEF = "\033[1;37m";
	public final static String RESET = "\033[0m";

	public static void notice(String msg) {
		System.out.println(BLUEF + "[INFO]" + RESET + " " + msg);
	}

	public static void error(String msg) {
		System.out.println(REDF + "[ERROR]" + RESET + " " + msg);
	}

	public static void ok(String msg) {
		System.out.println(GREENF + "[ OK ]" + RESET + " " + msg);
	}

	public static void notice(String msg, boolean ret) {
		System.out.print(BLUEF + "[INFO]" + RESET + " " + msg + (ret ? "\r" : "\r∫\n"));
	}
}