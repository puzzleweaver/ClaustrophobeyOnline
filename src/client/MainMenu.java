package client;

import main.Menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MainMenu implements Menu {
	
	public void render(GameContainer gc, Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, ClientMain.WIDTH, ClientMain.HEIGHT);
	}
	
	public void update(GameContainer gc) {
		
	}
	
}
