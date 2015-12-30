package client.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import client.ClientMain;
import server.Menu;

public class IntroMenu implements Menu {
	
	private long initTime;
	
	public void init(GameContainer gc) {
		initTime = System.nanoTime();
	}
	public void render(GameContainer gc, Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		float wh = Math.min(gc.getWidth(), gc.getHeight());
		float r1 = wh/12;
		float r2 = wh/144;
		float lineW = wh/288;
		
		g.setAntiAlias(true);
		g.setColor(Color.cyan);
		g.setLineWidth(lineW);
		g.drawOval(gc.getWidth()/2 - r1, gc.getHeight()/2 - r1, r1 * 2, r1 * 2);
		g.setColor(Color.red);
		g.fillOval(gc.getWidth()/2 + r1 - r2, gc.getHeight()/2 - r2, r2 * 2, r2 * 2);
		g.setLineWidth(2);
	}
	public void update(GameContainer gc) {
		//if 3 seconds have passed since init
		if(System.nanoTime() - initTime >= 3000000000L) {
			ClientMain.menu = new TransitionMenu(this, ClientMain.mainMenu);
		}
	}
	
}
