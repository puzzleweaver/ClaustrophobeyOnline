package server.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import server.Menu;
import server.ServerMain;
import client.Button;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;
import client.Settings;

public class ServerRoomMenu implements Menu {
	
	private Image image;
	private Graphics g2;
	private int sy = 0;
	
	private Button startButton;
	
	public void init(GameContainer gc) {
		startButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Start", ServerMain.font);
		try {
			image = new Image(gc.getWidth(), gc.getHeight()/2);
			g2 = image.getGraphics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//list
		g2.clear();
		g2.setFont(ServerMain.fontSmall);
		g2.setColor(Colors.titleColor);
		for(int i = 0; i < ServerMain.data.indieData.size(); i++) {
			String nick = ServerMain.data.indieData.get(i).clientData.nickname;
			g2.drawString(nick, image.getWidth()/2 - ServerMain.fontSmall.getWidth(nick)/2, sy + i*(ServerMain.fontSmall.getHeight()+1));
		}
		g.drawImage(image, 0, gc.getHeight()/4);
		startButton.render(gc, g);
	}
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(mousePressed && startButton.isHovered(gc)) {
			ServerMain.menu = ServerMain.serverPlayMenu;
		}
	}
	
}
