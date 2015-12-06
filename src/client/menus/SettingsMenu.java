package client.menus;

import main.Menu;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import client.Button;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;
import client.Sounds;

public class SettingsMenu implements Menu {
	
	private Button backButton, classicButton, darkButton, blueButton;
	private boolean isMusicSelected;
	
	public void init(GameContainer gc) {
		backButton = new Button(gc.getWidth()/4, gc.getHeight()/8, "Back", ClientMain.fontSmall);
		classicButton = new Button(gc.getWidth()/4, gc.getHeight()/2+ClientMain.fontSmall.getHeight(), "Classic", ClientMain.fontSmall);
		darkButton = new Button(gc.getWidth()/2, gc.getHeight()/2+ClientMain.fontSmall.getHeight(), "Dark", ClientMain.fontSmall);
		blueButton = new Button(3*gc.getWidth()/4, gc.getHeight()/2+ClientMain.fontSmall.getHeight(), "Blue", ClientMain.fontSmall);
	}
	public void render(GameContainer gc, Graphics g) {
		//background
		MenuBackground.render(gc, g);
		//title
		g.setColor(Colors.titleColor);
		g.setFont(ClientMain.font);
		g.drawString("Settings", gc.getWidth()/2 - ClientMain.font.getWidth("Settings")/2, gc.getHeight()/8);
		//back button
		backButton.render(g);
		//music volume
		g.setColor(Colors.titleColor);
		g.setFont(ClientMain.fontSmall);
		g.drawString("Music Volume", gc.getWidth()/2 - ClientMain.fontSmall.getWidth("Music Volume")/2, gc.getHeight()/4);
		//draw scale of volume
		g.setColor(Color.gray);
		g.fillRect(gc.getWidth()/4, gc.getHeight()/4 + ClientMain.fontSmall.getHeight()+10, gc.getWidth()/2, 20);
		g.setColor(Color.darkGray);
		g.fillRect(gc.getWidth()/4 + Sounds.music.getVolume()*gc.getWidth()/2 - 10, gc.getHeight()/4 + ClientMain.fontSmall.getHeight("Music Volume"), 20, 40);
		//color scheme
		g.setColor(Colors.titleColor);
		g.drawString("Color Scheme", gc.getWidth()/2 - ClientMain.fontSmall.getWidth("Color Scheme")/2, gc.getHeight()/2);
		classicButton.render(g);
		darkButton.render(g);
		blueButton.render(g);
	}
	public void update(GameContainer gc) {
		boolean mouseDown = Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON);
		if(mouseDown) {
			if(isMusicHovered())
				isMusicSelected = true;
			if(backButton.isHovered())
				ClientMain.menu = new TransitionMenu(this, ClientMain.mainMenu);
			if(classicButton.isHovered()) {
				classicButton.selected = true;
				darkButton.selected = false;
				blueButton.selected = false;
				Colors.setClassic();
			}
			if(darkButton.isHovered()) {
				darkButton.selected = true;
				classicButton.selected = false;
				blueButton.selected = false;
				Colors.setDark();
			}
			if(blueButton.isHovered()) {
				blueButton.selected = true;
				classicButton.selected = false;
				darkButton.selected = false;
				Colors.setBlue();
			}
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
	
}
