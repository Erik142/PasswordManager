package passwordmanager.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Helper class used to center a frame on the screen, both horizontally and
 * vertically
 * 
 * @author Erik Wahlberger
 */
public class FrameUtil {
	/***
	 * Positions the <code>JFrame</code> in the center of the screen.
	 * 
	 * @param frame The <code>JFrame</code> to be centered
	 */
	public static void centerFrame(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = frame.getSize();

		int windowXPos = (int) (screenSize.getWidth() / 2 - windowSize.getWidth() / 2);
		int windowYPos = (int) (screenSize.getHeight() / 2 - windowSize.getHeight() / 2);

		frame.setBounds(windowXPos, windowYPos, (int) windowSize.getWidth(), (int) windowSize.getHeight());
	}
}
