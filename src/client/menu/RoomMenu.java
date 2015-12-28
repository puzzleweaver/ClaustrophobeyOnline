package client.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import client.Button;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;
import net.GameClient;
import net.GameSocket;
import net.Serializer;
import server.Menu;
import server.game.Player;

public class RoomMenu implements Menu {
	
	private Button exitButton;
	
	public void init(GameContainer gc) {
		ClientMain.gameClient = new GameClient();
		ClientMain.gameClient.start();
		ClientMain.clientData.w = gc.getWidth();
		ClientMain.clientData.h = gc.getHeight();
		ClientMain.clientData.pixW = ClientMain.pixW;
		ClientMain.clientData.keys = new boolean[Player.NUM_KEYS];
		ClientMain.clientData.exited = false;
		
		exitButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Exit", ClientMain.font);
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		if(ClientMain.gameClient.loaded) {
			//title
			g.setColor(Colors.titleColor);
			g.setFont(ClientMain.font);
			g.drawString("Open Room", gc.getWidth()/2-ClientMain.font.getWidth("Open Room")/2, gc.getHeight()/8);
			//list of names
			g.setFont(ClientMain.fontSmall);
			for(int i = 0; i < ClientMain.data.names.length; i++) {
				String name = ClientMain.data.names[i];
				g.drawString(name, gc.getWidth()/2-ClientMain.fontSmall.getWidth(name)/2, gc.getHeight()/4 + i * ClientMain.fontSmall.getHeight());
			}
			//exit button
			exitButton.render(gc, g);
		}else {
			//connecting...
			g.setColor(Colors.titleColor);
			g.setFont(ClientMain.font);
			g.drawString("Connecting...", gc.getWidth()/2-ClientMain.font.getWidth("Connecting...")/2, gc.getHeight()/2);
		}
	}
	public void update(GameContainer gc) {
		//start play menu if server started game
		if(ClientMain.data.started) {
			ClientMain.playMenu.init(gc);
			ClientMain.menu = ClientMain.playMenu;
		}
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		//handle exiting
		if(mousePressed && exitButton.isHovered(gc)) {
			ClientMain.menu = ClientMain.serverManagerMenu;
			ClientMain.clientData.exited = true;
		}
		if(ClientMain.exited)
			ClientMain.clientData.exited = true;
		//TODO: RoomMenu scrolling functionality
		try {
			ClientMain.gameClient.sendData(Serializer.serialize(ClientMain.clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
