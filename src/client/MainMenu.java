package client;

import java.awt.Font;

import main.Menu;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

public class MainMenu implements Menu {

	private static TrueTypeFont fipps;
	
	public void init(GameContainer gc) {
		Font font;
		//InputStream inputStream = ResourceLoader.getResourceAsStream("res/Fipps-Regular.ttf");
		try {
			font = new Font("Arial", Font.BOLD, 10);//Font.createFont(Font.TRUETYPE_FONT, inputStream);
			fipps = new TrueTypeFont(font.deriveFont(48f), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//title
		g.setColor(Color.black);
		g.setFont(fipps);
		g.drawString("Claustrophobey", ClientMain.WIDTH/2 - fipps.getWidth("Claustrophobey")/2, ClientMain.HEIGHT/4);
		//play button
		g.setColor(isPlayButtonHovered() ? Color.green : Color.green.darker());
		g.drawString("Play", ClientMain.WIDTH/2 - fipps.getWidth("Play")/2, ClientMain.HEIGHT/2);
	}
	
	public void update(GameContainer gc) {
		if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON) && isPlayButtonHovered()) {
			ClientMain.menu = ClientMain.serverManagerMenu;
		}
	}
	
	private boolean isPlayButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - fipps.getWidth("Play")/2;
		int by = ClientMain.HEIGHT/2;
		return mx >= bx && mx <= bx+fipps.getWidth("Play") && my >= by && my <= by+fipps.getHeight("Play");
	}
	
}
