package client;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;

import main.Menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class ServerManagerMenu implements Menu {
	
	private static TrueTypeFont fipps;
	private static TrueTypeFont fippsSmall;
	
	private ArrayList<String> serverList = new ArrayList<>();
	
	public void init(GameContainer gc) {
		serverList.add("localhost");
		serverList.add("192.168.1.115");
		serverList.add("71.46.93.12");
		serverList.add("50.90.116.229");
		Font font;
		InputStream inputStream = ResourceLoader.getResourceAsStream("res/Fipps-Regular.ttf");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			fipps = new TrueTypeFont(font.deriveFont(24f), false);
			fippsSmall = new TrueTypeFont(font.deriveFont(18f), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		//background - again, something fancier later
		g.setColor(Color.red);
		g.fillRect(0, 0, ClientMain.WIDTH, ClientMain.HEIGHT);
		//title
		g.setFont(fipps);
		g.setColor(Color.black);
		g.drawString("Select Server", ClientMain.WIDTH/2 - fipps.getWidth("Select Server")/2, ClientMain.HEIGHT/8);
		//server list
		g.setFont(fippsSmall);
		g.setColor(Color.blue.darker());
		for(int i = 0; i < serverList.size(); i++) {
			g.drawString(serverList.get(i), ClientMain.WIDTH/2 - fippsSmall.getWidth(serverList.get(i))/2, ClientMain.HEIGHT/4 + i*46);
		}
	}
	
	public void update(GameContainer gc) {
		
	}
	
}
