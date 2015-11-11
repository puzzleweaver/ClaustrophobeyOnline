package client;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import main.Menu;
import net.GameClient;
import net.GameSocket;
import net.InputData;
import net.OutputData;
import net.Serializer;
import world.World;

public class PlayMenu implements Menu {
	
	private static GameClient gameClient;
	
	public static OutputData data = new OutputData();
	public static InputData clientData = new InputData();
	
	private int pw;
	private int rfw = ClientMain.WIDTH, rfh = ClientMain.HEIGHT;
	private double rf[][] = new double[rfw][rfh];
	
	public static final ArrayList<Double> R = new ArrayList<Double>(),
			G = new ArrayList<Double>(),
			B = new ArrayList<Double>();
	
	public PlayMenu() {
		gameClient = new GameClient();
		gameClient.start();
		clientData.w = ClientMain.WIDTH;
		clientData.h = ClientMain.HEIGHT;
		for(int i = 0; i < rfw; i++) {
			for(int j = 0; j < rfh; j++) {
				rf[i][j] = Math.random()*0.25+0.75;
			}
		}
		pw = ClientMain.pixW;
	}
	
	public Color get(short s, double t) {
		if(s == World.STATE_SPACE) return new Color((int) (140+65*t), 0, 0);
		else if(s == World.STATE_WALL) return new Color((int) (65*t+64), 0, 0);
		else {
			if(-s-1 >= R.size()) {
				do {
					R.add(Math.cos(R.size())*127+128);
					G.add(Math.cos(G.size()+2.09439510239)*127+128);
					B.add(Math.cos(B.size()+4.18879020479)*127+128);
				} while(-s-1 >= R.size());
			}
			return new Color((int) (t*R.get(-s-1)),
				(int) (t*G.get(-s-1)),
				(int) (t*B.get(-s-1)));
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		if(gameClient.loaded) {
			OutputData d = data;
			for(int i = 0; i < d.state.length; i++) {
				for(int j = 0; j < d.state[0].length; j++) {
					g.setColor(get(d.state[i][j], getRf(i, j, d)));
					g.fillRect(i*pw-d.sX%pw, j*pw-d.sY%pw, pw, pw);
				}
			}
		}
	}
	
	public double getRf(int i, int j, OutputData d) {
		int x = ((i+d.sX/pw)%rfw+rfw)%rfw,
				y = ((j+d.sY/pw)%rfh+rfh)%rfh;
		if(rf[x][y] < 0) rf[x][y] += Math.random()*0.1-0.05;
		else rf[x][y] += Math.random()*0.025-0.0125;
		rf[x][y] = Math.max(Math.min(rf[x][y], 1), 0.75);
		return rf[x][y];
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
