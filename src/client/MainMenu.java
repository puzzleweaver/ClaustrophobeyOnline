package client;

import java.awt.Font;

import main.Menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class MainMenu implements Menu {
	
	private static Font font;
	
	public MainMenu() {
		font = new Font("Arial", Font.BOLD, 26);
	}
	
	public void render(GameContainer gc, Graphics g) {
		//maybe a background scrolling map - not sure how to do this yet
		//-Riley
		g.setColor(Color.red);
		g.fillRect(0, 0, ClientMain.WIDTH, ClientMain.HEIGHT);
		//title
		g.setColor(Color.black);
		g.setFont(new TrueTypeFont(font, false));
		g.drawString("Claustrophobey", ClientMain.WIDTH/2 - g.getFont().getWidth("Claustrophobey")/2, 100);
	}
	
	public void update(GameContainer gc) {
		
	}
	
}
