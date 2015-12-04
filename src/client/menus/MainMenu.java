package client.menus;

import main.Menu;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import client.ClientMain;
import client.MenuBackground;

public class MainMenu implements Menu {
	
	public void init(GameContainer gc) {}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//title
		g.setColor(Menu.TITLE_COLOR);
		g.setFont(ClientMain.font);
		g.drawString("Claustrophobey", gc.getWidth()/2 - ClientMain.font.getWidth("Claustrophobey")/2, gc.getHeight()/4);
		//play button
		g.setColor(isPlayButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
		g.drawString("Play", gc.getWidth()/2 - ClientMain.font.getWidth("Play")/2, gc.getHeight()/2);
		//settings button
		g.setColor(isSettingsButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
		g.drawString("Settings", gc.getWidth()/2 - ClientMain.font.getWidth("Settings")/2, gc.getHeight()/2 + ClientMain.font.getHeight("Play") + 1);
	}
	
	public void update(GameContainer gc) {
		boolean mouseDown = gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(mouseDown) {
			if(isPlayButtonHovered())
				ClientMain.menu = ClientMain.serverManagerMenu;
			else if(isSettingsButtonHovered())
				ClientMain.menu = ClientMain.settingsMenu;
		}
	}
	
	private boolean isPlayButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("Play")/2;
		int by = ClientMain.HEIGHT/2;
		return mx >= bx && mx <= bx+ClientMain.font.getWidth("Play") && my >= by && my <= by+ClientMain.font.getHeight("Play");
	}
	private boolean isSettingsButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("Settings")/2;
		int by = ClientMain.HEIGHT/2 + ClientMain.font.getHeight("Play") + 1;
		return mx >= bx && mx <= bx+ClientMain.font.getWidth("Settings") && my >= by && my <= by+ClientMain.font.getHeight("Settings");
	}
	
}
