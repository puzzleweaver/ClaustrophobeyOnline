package main;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import main.game.GameMenu;

public class Main extends BasicGame {

	public static Menu menu;
	public static Random r = new Random();
	public static GameMenu gameMenu = new GameMenu();
	
	public static void main(String[] args) {
		gameMenu.init();
		menu = gameMenu;
		AppGameContainer app;
		try {
			app = new AppGameContainer(new Main());
			app.setDisplayMode(400, 400, false);
			app.setMinimumLogicUpdateInterval(15);
			app.setAlwaysRender(true);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public Main() {
		super("YA TOOCHECLOONK");
	}

	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		menu.render(arg1);
	}

	public void init(GameContainer arg0) throws SlickException {
		
	}

	public void update(GameContainer arg0, int arg1) throws SlickException {
		menu.update();
//		try {
//			Thread.sleep(100);
//		}catch(Exception e) {}
	}
	
}
