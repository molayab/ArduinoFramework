import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ArduinoFramework.Arduino;
import ArduinoFramework.Button;
import ArduinoFramework.Led;


public class GUITest extends JFrame implements ActionListener, ChangeListener {
	private JButton ledOFF;
	private JButton ledON;
	private JSlider slider;
	private Button btnButton2;
	
	private Led led1;
	private Led led2;
	private boolean isOn = false;
	
	public GUITest()  {
		super("Gui Test");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3, 1));
		
				led1 = new Led(13);
				led2 = new Led(9);
				led2.setType(Arduino.ANALOG);
				
				btnButton2 = new Button(4);
				btnButton2.init();
			
		
		
		slider = new JSlider(0, 255, 0);
		slider.addChangeListener(this);
		
		ledOFF = new JButton("Apagar");
		ledOFF.addActionListener(this);
		
		ledON = new JButton("Prender");
		ledON.addActionListener(this);
		
		add(ledOFF);
		add(ledON);
		add(slider);
		
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ledOFF) ) {
			led1.off();
		} else {
			led1.on();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		led2.power((float)slider.getValue() / 255);
	}
}
