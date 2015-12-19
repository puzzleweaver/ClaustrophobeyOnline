package client.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import server.Menu;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;

public class GameOverMenu implements Menu {
	
	public void init(GameContainer gc) {}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		g.setColor(Colors.titleColor);
		g.setFont(ClientMain.font);
		g.drawString("Game Over", gc.getWidth()/2-ClientMain.font.getWidth("Game Over")/2, gc.getHeight()/8);
	}
	public void update(GameContainer gc) {}
	
}
