package client;

import java.awt.Font;
import java.io.InputStream;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import client.menus.MainMenu;
import client.menus.PlayMenu;
import client.menus.ServerManagerMenu;
import client.menus.SettingsMenu;
import client.menus.TransitionMenu;
import main.Menu;

public class ClientMain extends BasicGame {
	
	public static Menu menu;
//	
//	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
//			HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//	public static final boolean FS = true;
	public static final int WIDTH = 800, HEIGHT = 600;
	public static final boolean FS = false;
	public static final int pixW = (int) Math.ceil(WIDTH/(6.0*Math.sqrt(1.0+3512.0*HEIGHT/WIDTH)))+3;
	public static Menu mainMenu, serverManagerMenu, playMenu, settingsMenu;
	public static GameContainer defaultGC;
	
	public static boolean exited = false;
	
	public static TrueTypeFont font, fontSmall;
	
	public ClientMain() {
		super("CLOOSTERFOOBEY");
	}
	
	public static void main(String[] args) {
		Settings.read();
		Sounds.load();
		Sounds.music.loop();
		Sounds.music.setVolume(Settings.musicVolume);
		mainMenu = new MainMenu();
		serverManagerMenu = new ServerManagerMenu();
		playMenu = new PlayMenu();
		settingsMenu = new SettingsMenu();
		menu = mainMenu;
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ClientMain());
			app.setDisplayMode(WIDTH, HEIGHT, FS);
			app.setMinimumLogicUpdateInterval(15);
			app.setAlwaysRender(true); 
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void init(GameContainer gc) throws SlickException {
		defaultGC = gc;
		InputStream inputStream = ResourceLoader.getResourceAsStream("res/half_bold_pixel-7.ttf");
		Font awtFont;
		try {
			awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream); //16, 24
			font = new TrueTypeFont(awtFont.deriveFont((float) Math.min(48, ClientMain.WIDTH/20)), false);
			fontSmall = new TrueTypeFont(awtFont.deriveFont((float) Math.min(48, ClientMain.WIDTH/24)), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MenuBackground.init();
		mainMenu.init(gc);
		serverManagerMenu.init(gc);
		settingsMenu.init(gc);
		menu = new TransitionMenu(serverManagerMenu, serverManagerMenu);
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		menu.render(gc, g);
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			exited = true;
		menu.update(gc);
		if(exited) {
			Settings.write();
			gc.exit();
		}
	}
	
	public boolean closeRequested() {
		exited = true;
		return false;
	}
	
}
