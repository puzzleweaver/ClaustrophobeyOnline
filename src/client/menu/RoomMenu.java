package client.menu;

import net.GameClient;
import net.GameSocket;
import net.Serializer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import server.Menu;
import server.game.Player;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;

public class RoomMenu implements Menu {
	
	public void init(GameContainer gc) {
		ClientMain.gameClient = new GameClient();
		ClientMain.gameClient.start();
		ClientMain.clientData.w = gc.getWidth();
		ClientMain.clientData.h = gc.getHeight();
		ClientMain.clientData.pixW = ClientMain.pixW;
		ClientMain.clientData.keys = new boolean[Player.NUM_KEYS];
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		g.setColor(Colors.titleColor);
		g.setFont(ClientMain.font);
		g.drawString("Open Room", gc.getWidth()/2-ClientMain.font.getWidth("Open Room")/2, gc.getHeight()/8);
	}
	public void update(GameContainer gc) {
		if(ClientMain.data.started) {
			ClientMain.playMenu.init(gc);
			ClientMain.menu = ClientMain.playMenu;
		}
		try {
			ClientMain.gameClient.sendData(Serializer.serialize(ClientMain.clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
