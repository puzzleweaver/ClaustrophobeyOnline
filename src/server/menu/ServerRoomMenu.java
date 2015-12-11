package server.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import client.Button;
import client.Colors;
import client.MenuBackground;
import server.Menu;
import server.ServerMain;

public class ServerRoomMenu implements Menu {
	
	private Button startButton;
	
	public void init(GameContainer gc) {
		startButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Start", ServerMain.font);
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		g.setFont(ServerMain.font);
		g.setColor(Colors.titleColor);
		for(int i = 0; i < ServerMain.data.indieData.size(); i++) {
			String nickname = ServerMain.data.indieData.get(i).clientData.nickname;
			g.drawString(nickname, gc.getWidth()/2 - ServerMain.font.getWidth(nickname)/2, gc.getHeight()/4 + i*ServerMain.font.getHeight());
		}
		startButton.render(gc, g);
	}
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(startButton.isHovered(gc) && mousePressed) {
			ServerMain.menu = ServerMain.serverPlayMenu;
		}
	}
	
}
