package GUIs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panel extends JPanel {
	
	public static final long serialVersionUID = 1;
	
	public Panel(LayoutManager lm) {	
		setLayout(lm);
		setOpaque(true);
		setBackground(Color.BLACK);
	}
	
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        try {
			g.drawImage(ImageIO.read(new File("resources/GUIs/Grassy_hill.jpg")), 
					0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}		
    }

}
