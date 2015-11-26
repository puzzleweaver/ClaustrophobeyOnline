package client;

import java.awt.Font;
import java.awt.Toolkit;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import main.Menu;

public class ClientMain extends BasicGame {
	
	public static Menu menu;
	
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
			HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int pixW = 10;
	
	public static Menu mainMenu, serverManagerMenu, playMenu, settingsMenu;
	
	public static boolean exited = false;
	
	public static TrueTypeFont font, fontSmall;
	
	public ClientMain() {
		super("GISOLKUQ");
	}
	
	public static void main(String[] args) {
		Sounds.load();
		Sounds.music.loop();
		Sounds.music.setVolume(0.0f);
		mainMenu = new MainMenu();
		serverManagerMenu = new ServerManagerMenu();
		playMenu = new PlayMenu();
		settingsMenu = new SettingsMenu();
		menu = mainMenu;
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
		Font awtfont;
		//InputStream inputStream = ResourceLoader.getResourceAsStream("res/Fipps-Regular.ttf");
		awtfont = new Font("Arial", Font.BOLD, 10);//Font.createFont(Font.TRUETYPE_FONT, inputStream);
		font = new TrueTypeFont(awtfont.deriveFont(48f), false);
		fontSmall = new TrueTypeFont(awtfont.deriveFont(36f), false);
		MenuBackground.init();
		mainMenu.init(gc);
		serverManagerMenu.init(gc);
		settingsMenu.init(gc);
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		menu.render(gc, g);
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			exited = true;
		menu.update(gc);
		if(exited)
			gc.exit();
	}
	
	public boolean closeRequested() {
		exited = true;
		return false;
	}
	
}
