package client;

import main.game.State;
import net.GameClient;
import net.GameSocket;
import net.InputData;
import net.OutputData;
import net.Serializer;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import world.World;

public class ClientMain extends BasicGame {
	
	private static GameClient gameClient;
	
	public static OutputData data = new OutputData();
	public static InputData clientData = new InputData();
	
	public static int pixW = 8;
	
	public ClientMain() {
		super("GISOLKUQ");
	}
	
	public static void main(String[] args) {
		gameClient = new GameClient();
		gameClient.start();
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ClientMain());
			app.setDisplayMode(400, 400, false);
			app.setMinimumLogicUpdateInterval(15);
			app.setAlwaysRender(true);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
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
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		OutputData data = ClientMain.data;
		if(gameClient.loaded) {
			for(int i = 0; i < data.state.length; i++) {
				for(int j = 0; j < data.state[0].length; j++) {
					g.setColor(get(data.state[i][j]));
					g.fillRect(i*pixW-data.sX%pixW, j*pixW-data.sY%pixW, pixW, pixW);
				}
			}
		}
	}
	public void init(GameContainer gc) throws SlickException {
		clientData.w = gc.getWidth();
		clientData.h = gc.getHeight();
	}
	public void update(GameContainer gc, int delta) throws SlickException {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			clientData.exited = true;
		clientData.dx = (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 1 : 0) - (Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? 1 : 0);
		clientData.dy = (Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 1 : 0) - (Keyboard.isKeyDown(Keyboard.KEY_UP) ? 1 : 0);
		clientData.pixW = pixW;
		try {
			gameClient.sendData(Serializer.serialize(clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(clientData.exited) {
			gc.exit();
		}
	}
	
	public boolean closeRequested() {
		clientData.exited = true;
		return false;
	}
	
}
