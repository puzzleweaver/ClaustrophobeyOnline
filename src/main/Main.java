package main;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.URL;
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
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import client.Button;
import client.Colors;
import client.MenuBackground;

public class Main extends GameSocket {
	
	public static Random r = new Random();
	
	public static ServerData data = new ServerData();
	
	private static String str1, str2;
	
	public static final int TERRITORIAL = 0, SUDDEN_DEATH = 1;
	public static int gameType = TERRITORIAL;
	public static int numTeams = 0; //0 teams is free for all
	
	private static boolean started = false;
	
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
		
		public TrueTypeFont font, fontSmall;
		
		private Button suddenDeathButton, territorialButton, ffaButton, twoButton, threeButton, startButton;
		
		public ServerScreen() {
			super("CLASTERFOOBRY");
			try {
				str1 = "Private IP: " + Inet4Address.getLocalHost().getHostAddress();
				URL whatismyip = new URL("http://checkip.amazonaws.com");
				BufferedReader in = new BufferedReader(new InputStreamReader(
		                whatismyip.openStream()));
				str2 = "Public IP: " + in.readLine();
			} catch (Exception e) {
				// computer is offline
				if(str1 == null) {
					str1 = "PRIVATE IP NOT AVAILABLE";
				}
				if(str2 == null) {
					str2 = "PUBLIC IP NOT AVAILABLE";
				}
			}
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
			suddenDeathButton = new Button(gc.getWidth()/4, gc.getHeight()/8+font.getHeight(), "Sudden Death", fontSmall);
			territorialButton = new Button(3*gc.getWidth()/4, gc.getHeight()/8+font.getHeight(), "Territorial", fontSmall);
			ffaButton = new Button(gc.getWidth()/4, gc.getHeight()/4+font.getHeight(), "FFA", fontSmall);
			twoButton = new Button(gc.getWidth()/2, gc.getHeight()/4+font.getHeight(), "2", fontSmall);
			threeButton = new Button(3*gc.getWidth()/4, gc.getHeight()/4+font.getHeight(), "3", fontSmall);
			
			startButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Start", font);
			startButton.enabled = false;
			MenuBackground.init();
		}
		public void render(GameContainer gc, Graphics g) throws SlickException {
			if(started) {
				g.setColor(Colors.titleColor);
				g.setFont(font);
				g.drawString(str1, gc.getWidth()/2 - font.getWidth(str1)/2, gc.getHeight()/2-font.getHeight());
				g.drawString(str2, gc.getWidth()/2 - font.getWidth(str2)/2, gc.getHeight()/2+font.getHeight());
			}else {
				MenuBackground.render(gc, g);
				//game mode
				g.setColor(Colors.titleColor);
				g.setFont(font);
				g.drawString("Game Mode", gc.getWidth()/2 - font.getWidth("Game mode")/2, gc.getHeight()/8);
				suddenDeathButton.render(gc, g);
				territorialButton.render(gc, g);
				//teams
				g.setColor(Colors.titleColor);
				g.setFont(font);
				g.drawString("Teams", gc.getWidth()/2 - font.getWidth("Teams")/2, gc.getHeight()/4);
				ffaButton.render(gc, g);
				twoButton.render(gc, g);
				threeButton.render(gc, g);
				//start button
				startButton.render(gc, g);
			}
		}
		public void update(GameContainer gc, int delta) throws SlickException {
			Input input = gc.getInput();
			boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
			if(started) {
				if(input.isKeyDown(Input.KEY_ESCAPE)) {
					data.saveLocalImage();
					System.exit(1);
				}
				data.update((double) delta / 1000.0);
			}else {
				if((suddenDeathButton.selected || territorialButton.selected) &&
						(ffaButton.selected || twoButton.selected || threeButton.selected)) {
					startButton.enabled = true;
				}
				if(mousePressed) {
					if(suddenDeathButton.isHovered(gc)) {
						suddenDeathButton.selected = true;
						territorialButton.selected = false;
					}if(territorialButton.isHovered(gc)) {
						suddenDeathButton.selected = false;
						territorialButton.selected = true;
					}if(ffaButton.isHovered(gc)) {
						ffaButton.selected = true;
						twoButton.selected = false;
						threeButton.selected = false;
					}if(twoButton.isHovered(gc)) {
						ffaButton.selected = false;
						twoButton.selected = true;
						threeButton.selected = false;
					}if(threeButton.isHovered(gc)) {
						ffaButton.selected = false;
						twoButton.selected = false;
						threeButton.selected = true;
					}if(startButton.isHovered(gc) && startButton.enabled) {
						started = true;
						gameType = suddenDeathButton.selected ? SUDDEN_DEATH : TERRITORIAL;
						numTeams = ffaButton.selected ? 0 : (twoButton.selected ? 2 : 3);
						new Main().start();
					}
				}
			}
		}
		
	}
	
}
