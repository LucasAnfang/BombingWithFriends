package Main;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class OptionPane extends JOptionPane {
	private static final long serialVersionUID = 1;
	public OptionPane(String m, String title, int messageType) {
		JLabel message = new JLabel(m + "      ");
		optionType = JOptionPane.OK_OPTION;
		message.setFont(new Font("Minecraft", Font.PLAIN, 14));
		showMessageDialog(null, message, title, messageType);
	}
	
}
