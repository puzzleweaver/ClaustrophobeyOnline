package client;

import main.Menu;
import main.game.State;
import net.GameClient;
import net.GameSocket;
import net.InputData;
import net.OutputData;
import net.Serializer;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import world.World;

public class PlayMenu implements Menu {
	
	private static GameClient gameClient;
	
	public static OutputData data = new OutputData();
	public static InputData clientData = new InputData();
	
	public double rf[][] = new double[100][100];
	
	public static final ArrayList<Double> R = new ArrayList<Double>(),
			G = new ArrayList<Double>(),
			B = new ArrayList<Double>();
	
	public PlayMenu() {
		gameClient = new GameClient();
		gameClient.start();
		clientData.w = ClientMain.WIDTH;
		clientData.h = ClientMain.HEIGHT;
		for(int i = 0; i < 100; i++) {
			for(int j = 0; j < 100; j++) {
				rf[i][j] = 1;//Math.random();
			}
		}
	}
	
	public Color get(short s, double t) {
		if(s == World.STATE_SPACE) return new Color((int) (190+65*t), 0, 0);
		else if(s == World.STATE_WALL) return new Color((int) (65*t+64), 0, 0);
		else if(s < 0) {
			if(-s-1 >= R.size()) {
				do {
					R.add(Math.cos(R.size())*127+128);
					G.add(Math.cos(G.size()+2.09439510239)*127+128);
					B.add(Math.cos(B.size()+4.18879020479)*127+128);
				} while(-s-1 >= R.size());
			}
			double col = (3.0+t)*0.25;
			return new Color((int) (col*R.get(-s-1)),
				(int) (col*G.get(-s-1)),
				(int) (col*B.get(-s-1)));
		}
		return null;
	}
	
	public void render(GameContainer gc, Graphics g) {
		if(gameClient.loaded) {
			OutputData d = data;
			for(int i = 0; i < d.state.length; i++) {
				for(int j = 0; j < d.state[0].length; j++) {
					g.setColor(get(d.state[i][j], 1));//rf[(i+d.sX/ClientMain.pixW)%rf.length][(j+d.sY/ClientMain.pixW)%rf[0].length]));
					g.fillRect(i*ClientMain.pixW-d.sX%ClientMain.pixW, j*ClientMain.pixW-d.sY%ClientMain.pixW, ClientMain.pixW, ClientMain.pixW);
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
		clientData.pixW = ClientMain.pixW;
		try {
			gameClient.sendData(Serializer.serialize(clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
