package client;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import main.Menu;

public class SettingsMenu implements Menu {
	
	private boolean isMusicSelected;
	
	public void init(GameContainer gc) {}
	public void render(GameContainer gc, Graphics g) {
		//background
		MenuBackground.render(gc, g);
		//title
		g.setColor(Menu.TITLE_COLOR);
		g.setFont(ClientMain.font);
		g.drawString("Settings", gc.getWidth()/2 - ClientMain.font.getWidth("Settings")/2, gc.getHeight()/8);
		//back button
		g.setColor(isBackButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
		g.drawString("Back", gc.getWidth()/4 - ClientMain.font.getWidth("Back")/2,  gc.getHeight()/8);
		//music volume
		g.setColor(Menu.TITLE_COLOR);
		g.setFont(ClientMain.fontSmall);
		g.drawString("Music Volume", gc.getWidth()/2 - ClientMain.fontSmall.getWidth("Music Volume")/2, gc.getHeight()/4);
		//draw scale of volume
		g.setColor(Color.gray);
		g.fillRect(gc.getWidth()/4, gc.getHeight()/4 + ClientMain.fontSmall.getHeight("Music Volume")+10, gc.getWidth()/2, 20);
		g.setColor(Color.darkGray);
		g.fillRect(gc.getWidth()/4 + Sounds.music.getVolume()*gc.getWidth()/2 - 10, gc.getHeight()/4 + ClientMain.fontSmall.getHeight("Music Volume"), 20, 40);
	}
	public void update(GameContainer gc) {
		boolean mouseDown = Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON);
		if(mouseDown) {
			if(isMusicHovered())
				isMusicSelected = true;
			if(isBackButtonHovered())
				ClientMain.menu = ClientMain.mainMenu;
		}else {
			isMusicSelected = false;
		}
		if(isMusicSelected) {
			float volume = ((float) (Mouse.getX()-gc.getWidth()/4)/((float) gc.getWidth()/2));
			if(volume > 1) volume = 1;
			if(volume < 0) volume = 0;
			Sounds.music.setVolume(volume);
		}
	}
	
	private boolean isMusicHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/4;
		int by = ClientMain.HEIGHT/4 + ClientMain.fontSmall.getHeight("Music Volume")+10;
		return mx >= bx && mx <= bx+ClientMain.WIDTH/2 && my >= by && my <= by+20;
	}
	private boolean isBackButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/4 - ClientMain.font.getWidth("Back")/2;
		int by = ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Back") && my > by && my < by+ClientMain.font.getHeight("Back");
	}
	
}
