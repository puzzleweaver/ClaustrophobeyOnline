package server.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import client.MenuBackground;
import server.Menu;
import server.ServerMain;

public class ServerRoomMenu implements Menu {
	
	public void init(GameContainer gc) {}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		g.setFont(ServerMain.font);
		for(int i = 0; i < ServerMain.data.indieData.size(); i++) {
			String nickname = ServerMain.data.indieData.get(i).clientData.nickname;
			g.drawString(nickname, gc.getWidth()/2 - ServerMain.font.getWidth(nickname)/2, gc.getHeight()/4 + i*ServerMain.font.getHeight());
		}
	}
	public void update(GameContainer gc) {}
	
}
