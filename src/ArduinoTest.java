import ArduinoFramework.Arduino;
import ArduinoFramework.Button;
import ArduinoFramework.Led;


public class ArduinoTest {

	public static void main(String[] args) throws InterruptedException {
		
		/**
		 * 1) Led parpadeando digital
		 * 2) Led variable en intensidad.
		 * 3) GUI con Arduino
		 */
		int ejemplo = 1;
		
		if (ejemplo == 1) {
			Led l = new Led(13);
			
			Button btn = new Button(3);
			btn.init();
			
			for (;;) {
				Thread.sleep(700);
				l.on();
				Thread.sleep(700);
				l.off();
			}
		} else if (ejemplo == 2) {
			Led l = new Led(9);
			l.setType(Arduino.ANALOG);
			
			for (;;) {
				for (int i = 0; i < 100; i++) {
					l.power((float)i / 100);
					Thread.sleep(10);
				}
				
				for (int i = 100; i >= 0; i--) {
					l.power((float)i / 100);
					Thread.sleep(10);
				}
			}
		} else if (ejemplo == 3) {
			new GUITest().setVisible(true);
		}
	}
}
