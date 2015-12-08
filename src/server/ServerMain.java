package server;

import java.awt.Font;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

import net.GameSocket;
import net.InputData;
import net.Serializer;
import net.ServerData;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import client.MenuBackground;
import server.menu.ServerPlayMenu;
import server.menu.ServerSetupMenu;

public class ServerMain extends GameSocket {
	
	public static Random r = new Random();
	
	public static ServerData data = new ServerData();
	
	public static TrueTypeFont font, fontSmall;
	
	public static Menu menu;
	public static Menu serverSetupMenu, serverPlayMenu;
	
	public static ServerMain server;
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ServerScreen());
			app.setDisplayMode(600, 600, false);
			app.setMinimumLogicUpdateInterval(15);
			app.setAlwaysRender(true);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public ServerMain() {
		super();
		try {
			this.socket = new DatagramSocket(PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readData(DatagramPacket packet) {
		try {
			if(data.processData((InputData) Serializer.deserialize(packet.getData()), packet.getAddress(), packet.getPort())) {
				sendData(Serializer.serialize(data.getOutputData()), packet.getAddress(), packet.getPort());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class ServerScreen extends BasicGame {
		
		public ServerScreen() {
			super("CLASTERFOOBRY");
			serverSetupMenu = new ServerSetupMenu();
			serverPlayMenu = new ServerPlayMenu();
		}
		
		public void init(GameContainer gc) throws SlickException {
			InputStream inputStream = ResourceLoader.getResourceAsStream("res/half_bold_pixel-7.ttf");
			Font awtFont;
			try {
				awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream); //16, 24
				font = new TrueTypeFont(awtFont.deriveFont((float) Math.min(48, gc.getWidth()/20)), false);
				fontSmall = new TrueTypeFont(awtFont.deriveFont((float) Math.min(48, gc.getHeight()/24)), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			serverSetupMenu.init(gc);
			serverPlayMenu.init(gc);
			menu = serverSetupMenu;
			MenuBackground.init();
		}
		public void render(GameContainer gc, Graphics g) throws SlickException {
			menu.render(gc, g);
		}
		public void update(GameContainer gc, int delta) throws SlickException {
			menu.update(gc);
		}
		
	}
	
}
