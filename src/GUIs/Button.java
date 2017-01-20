package GUIs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class Button extends JButton {

	private static final long serialVersionUID = 1;
	private Font font;
	public Button(String name) {
		super(name);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/GUIs/Minecraft.ttf"));
			GraphicsEnvironment ge = 
		            GraphicsEnvironment.getLocalGraphicsEnvironment();
		        ge.registerFont(font);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setFont(new Font("Minecraft", Font.PLAIN, 14));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);	
        //setPreferredSize(new Dimension(100, 25));
        setForeground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g) {
        try {
        	g.drawImage(ImageIO.read(new File("resources/GUIs/BWF_Button.png")), 
        			0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	super.paintComponent(g);
    }		
	
}