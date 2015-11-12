package client;

import java.awt.Toolkit;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import main.Menu;

public class ClientMain extends BasicGame {
	
	public static Menu menu;
	
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
			HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int pixW = 10;
	
	public static boolean exited = false;
	
	public ClientMain() {
		super("GISOLKUQ");
	}
	
	public static void main(String[] args) {
		menu = new MainMenu();
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ClientMain());
			app.setDisplayMode(WIDTH, HEIGHT, true);
			app.setMinimumLogicUpdateInterval(15);
			app.setAlwaysRender(true);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void init(GameContainer gc) throws SlickException {
		menu.init(gc);
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		menu.render(gc, g);
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		menu.update(gc);
		if(exited)
			gc.exit();
	}
	
	public boolean closeRequested() {
		exited = true;
		return false;
	}
	
}
