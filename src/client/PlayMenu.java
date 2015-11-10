package client;

import main.Menu;
import main.game.State;
import net.GameClient;
import net.GameSocket;
import net.InputData;
import net.OutputData;
import net.Serializer;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import world.World;

public class PlayMenu implements Menu {
	
	private static GameClient gameClient;
	
	public static OutputData data = new OutputData();
	public static InputData clientData = new InputData();
	
	public static int pixW = 8;
	
	public PlayMenu() {
		gameClient = new GameClient();
		gameClient.start();
		clientData.w = ClientMain.WIDTH;
		clientData.h = ClientMain.HEIGHT;
	}
	
	public Color get(State s) {
		int id = s.type;
		if(id == World.STATE_SPACE) return new Color((int) (190+65*s.t), 0, 0);
		else if(id == World.STATE_WALL) return new Color((int) (65*s.t+64), 0, 0);
		else if(id < 0) {
			double col = (3.0+s.t)*0.25;
			return new Color((int) (col*(Math.cos(id)*127+128)),
				(int) (col*(Math.cos(id+2.09439510239)*127+128)),
				(int) (col*(Math.cos(id+4.18879020479)*127+128)));
		}
		return null;
	}
	
	public void render(GameContainer gc, Graphics g) {
		if(gameClient.loaded) {
			OutputData d = data;
			for(int i = 0; i < d.state.length; i++) {
				for(int j = 0; j < d.state[0].length; j++) {
					g.setColor(get(d.state[i][j]));
					g.fillRect(i*pixW-d.sX%pixW, j*pixW-d.sY%pixW, pixW, pixW);
				}
			}
		}
	}
	
	public void update(GameContainer gc) {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			ClientMain.exited = true;
		clientData.exited = ClientMain.exited;
		clientData.dx = (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 1 : 0) - (Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? 1 : 0);
		clientData.dy = (Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 1 : 0) - (Keyboard.isKeyDown(Keyboard.KEY_UP) ? 1 : 0);
		clientData.pixW = pixW;
		try {
			gameClient.sendData(Serializer.serialize(clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
