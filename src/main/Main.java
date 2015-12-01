package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Random;

import net.GameSocket;
import net.InputData;
import net.Serializer;
import net.ServerData;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends GameSocket {
	
	public static Random r = new Random();
	
	public static ServerData data = new ServerData();
	
	private static String str1, str2;
	
	public static void main(String[] args) {
		new Main().start();
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ServerScreen());
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
			try {
				str1 = "Private IP: " + Inet4Address.getLocalHost().getHostAddress();
				URL whatismyip = new URL("http://checkip.amazonaws.com");
				BufferedReader in = new BufferedReader(new InputStreamReader(
		                whatismyip.openStream()));
				str2 = "Public IP: " + in.readLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void render(GameContainer gc, Graphics g) throws SlickException {
			g.setColor(Color.white);
			g.drawString(str1, gc.getWidth()/2 - g.getFont().getWidth(str1)/2, gc.getHeight()/2-g.getFont().getLineHeight());
			g.drawString(str2, gc.getWidth()/2 - g.getFont().getWidth(str2)/2, gc.getHeight()/2+g.getFont().getLineHeight());
		}
		public void init(GameContainer gc) throws SlickException {}
		public void update(GameContainer gc, int delta) throws SlickException {
			//menu.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				data.saveLocalImage();
				System.exit(1);
			}
			data.update((double) delta / 1000.0);
		}
		
	}
	
}
