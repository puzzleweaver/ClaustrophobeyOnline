package client.menus;

import main.Menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import client.Button;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;

public class MainMenu implements Menu {
	
	private Button playButton, settingsButton;
	
	public void init(GameContainer gc) {
		playButton = new Button(gc.getWidth()/2, gc.getHeight()/2, "Play", ClientMain.font);
		settingsButton = new Button(gc.getWidth()/2, gc.getHeight()/2+ClientMain.font.getHeight(), "Settings", ClientMain.font);
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//title
		g.setColor(Colors.titleColor);
		g.setFont(ClientMain.font);
		g.drawString("Claustrophobey", gc.getWidth()/2 - ClientMain.font.getWidth("Claustrophobey")/2, gc.getHeight()/4);
		//play button
		playButton.render(g);
		settingsButton.render(g);
	}
	
	public void update(GameContainer gc) {
		boolean mouseDown = gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(mouseDown) {
			if(playButton.isHovered())
				ClientMain.menu = new TransitionMenu(this, ClientMain.serverManagerMenu);
			else if(settingsButton.isHovered())
				ClientMain.menu = new TransitionMenu(this, ClientMain.settingsMenu);
		}
	}
	
}
