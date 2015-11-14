package client;

import java.awt.Font;
import java.io.InputStream;

import main.Main;
import main.Menu;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class MainMenu implements Menu {

	private static TrueTypeFont fippsTTF;
	
	public void init(GameContainer gc) {
		Font fipps;
		InputStream inputStream = ResourceLoader.getResourceAsStream("res/Fipps-Regular.ttf");
		try {
			fipps = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			fippsTTF = new TrueTypeFont(fipps.deriveFont(24f), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void render(GameContainer gc, Graphics g) {
		//maybe a background scrolling map or glitter effect - not sure how to do this yet
		//-Riley
		g.setColor(Color.red);
		g.fillRect(0, 0, ClientMain.WIDTH, ClientMain.HEIGHT);
		//title
		g.setColor(Color.black);
		g.setFont(fippsTTF);
		g.drawString("Claustrophobey", ClientMain.WIDTH/2 - fippsTTF.getWidth("Claustrophobey")/2, ClientMain.HEIGHT/4);
		//play button
		g.setColor(isPlayButtonHovered() ? Color.white : Color.blue.darker());
		g.drawString("Play", ClientMain.WIDTH/2 - fippsTTF.getWidth("Play")/2, ClientMain.HEIGHT/2);
	}
	
	public void update(GameContainer gc) {
		if(Mouse.isButtonDown(0) && isPlayButtonHovered()) {
			ClientMain.menu = new ServerManagerMenu();
			ClientMain.menu.init(gc);
		}
	}
	
	private boolean isPlayButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - fippsTTF.getWidth("Play")/2;
		int by = ClientMain.HEIGHT/2;
		return mx >= bx && mx <= bx+fippsTTF.getWidth("Play") && my >= by && my <= by+fippsTTF.getHeight("Play");
	}
	
}
