
import ArduinoFramework.Led;

public class ArduinoTest {

	public static void main(String[] args) throws InterruptedException {
		Led l1 = new Led(13);
		
		for ( ; ; ) {
			l1.on();
			Thread.sleep(1000);
			l1.off();
			Thread.sleep(1000);
		}
	}
}
