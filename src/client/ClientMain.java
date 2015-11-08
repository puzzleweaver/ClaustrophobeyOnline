package client;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import main.game.State;
import net.GameClient;
import net.GameSocket;
import net.InputData;
import net.Serializer;
import net.ServerData;

public class ClientMain extends BasicGame {
	
	private static GameClient gameClient;
	
	public static ServerData data = new ServerData();
	public static InputData clientData = new InputData();
	
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
		if(s.type == 0) return new Color((int) (128+s.t)-(int)Math.min(0, 125), (int)Math.min(0+s.t*0*0.1, 128), 0);
		else if(s.type == 1) return new Color((int) (63+s.t/10)-(int)Math.min(0, 70), (int)Math.min(0+s.t*0*0.1, 70), 0);
		else return new Color(0, (int)Math.min(0+s.t*0*0.1, 128), (int) (128+s.t)-(int)Math.min(0, 125));
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		ServerData data = ClientMain.data;
		if(gameClient.loaded) {
			g.setColor(new Color(190, 160, 0));
			g.fillRect(0, 0, data.w*8, data.h*8);
			int k = 400/data.w;
			for(int i = 0; i < data.w; i++) {
				for(int j = 0; j < data.h; j++) {
					g.setColor(get(data.state[i][j]));
					g.fillRect(i*k-data.indieData.get(data.index).sX+200, j*k-data.indieData.get(data.index).sY+200, k, k);
				}
			}
		}
	}
	public void init(GameContainer gc) throws SlickException {}
	public void update(GameContainer gc, int delta) throws SlickException {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			clientData.exited = true;
		clientData.dx = (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 1 : 0) - (Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? 1 : 0);
		clientData.dy = (Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 1 : 0) - (Keyboard.isKeyDown(Keyboard.KEY_UP) ? 1 : 0);
		try {
			gameClient.sendData(Serializer.serialize(clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(clientData.exited) {
			gc.exit();
		}
	}
	
}
