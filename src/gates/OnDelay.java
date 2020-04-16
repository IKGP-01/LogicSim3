package gates;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.Date;

import javax.swing.JOptionPane;

import logicsim.Gate;
import logicsim.I18N;
import logicsim.Lang;
import logicsim.Pin;

/**
 * ON-Delay component for LogicSim
 * 
 * @author Andreas Tetzl
 * @author Peter Gabriel
 * @version 2.0
 */
public class OnDelay extends Gate {
	static final long serialVersionUID = -2350098633141393951L;

	static final String DELAY = "delay";
	static final String DELAY_DEFAULT = "500";

	transient long startTime;
	transient boolean lastInputState;
	int delayTime = 1000;

	public OnDelay() {
		super("input");
		type = "ondelay";
		createInputs(1);
		createOutputs(1);
		loadProperties();
	}

	@Override
	protected void loadProperties() {
		delayTime = Integer.parseInt(getPropertyWithDefault(DELAY, DELAY_DEFAULT));
	}

	public void simulate() {
		Pin in = getPin(0);
		Pin out = getPin(1);

		if (lastInputState == false && in.getLevel()) { // positive flanke
			startTime = new Date().getTime();
		}

		if (new Date().getTime() - startTime > delayTime && in.getLevel())
			out.setLevel(true);

		if (!in.getLevel())
			out.setLevel(false);

		lastInputState = in.getLevel();
	}

	public boolean hasPropertiesUI() {
		return true;
	}

	public boolean showPropertiesUI(Component frame) {
		String h = (String) JOptionPane.showInputDialog(frame, I18N.getString(type, DELAY), I18N.tr(Lang.SETTINGS),
				JOptionPane.QUESTION_MESSAGE, null, null, Integer.toString((int) delayTime));
		if (h != null && h.length() > 0) {
			delayTime = Integer.parseInt(h);
			setPropertyInt(DELAY, delayTime);
		}
		return true;
	}

	@Override
	protected void drawFrame(Graphics2D g2) {
		super.drawFrame(g2);
		int cX = width / 2 + getX();
		int cY = height / 2 + getY();
		int cd = 15;
		g2.drawOval(cX - cd / 2, cY - cd / 2 + 5, cd, cd);
		g2.drawLine(getX() + cd, getY() + cd, getX() + width - cd, getY() + cd);
		g2.drawString("0", getX() + cd, getY() + cd + 12);
		g2.drawString("1", getX() + width - cd - 6, getY() + cd + 12);
		Path2D ptr = new Path2D.Double();
		ptr.moveTo(cX, cY);
		ptr.lineTo(cX, cY + 5);
		ptr.lineTo(cX + 3, cY + 5);
		g2.draw(ptr);
	}

	@Override
	public void loadLanguage() {
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "On Delay");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "Delays the signal when turning to HIGH");
		I18N.addGate(I18N.ALL, type, DELAY, "Time of delay (ms)");

		I18N.addGate("de", type, I18N.TITLE, "Einschaltverzögerung");
		I18N.addGate("de", type, I18N.DESCRIPTION, "Verzögert das High-Signal");
		I18N.addGate("de", type, DELAY, "Verzögerungszeit (ms)");

		I18N.addGate("es", type, I18N.TITLE, "Retardo de 0 a 1");
		I18N.addGate("es", type, DELAY, "Introduce tiempo de retardo (ms)");

		I18N.addGate("fr", type, I18N.TITLE, "Temporisation travail");
		I18N.addGate("fr", type, DELAY, "Décalage (ms)");
	}
}