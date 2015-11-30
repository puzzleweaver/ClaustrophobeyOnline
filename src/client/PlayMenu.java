package client;

import java.util.ArrayList;

import main.Menu;
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
	
	private int pw;
	private int rfw = ClientMain.WIDTH, rfh = ClientMain.HEIGHT;
	private double rf[][] = new double[rfw][rfh];
	
	public static final ArrayList<Double> R = new ArrayList<Double>(),
			G = new ArrayList<Double>(),
			B = new ArrayList<Double>();
	
	public void init(GameContainer gc) {
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
//		if(s == World.STATE_SPACE) return new Color((int) (140+65*t), 0, 0);
//		else if(s == World.STATE_WALL) return new Color((int) (32*t+64), 0, 0);
		int rs = (short) (((s+8192)%8192 + 8192)%8192);
		if(rs >= R.size()) {
			do {
				R.add(Math.cos(R.size())*127+128);
				G.add(Math.cos(G.size()+2.09439510239)*127+128);
				B.add(Math.cos(B.size()+4.18879020479)*127+128);
			} while(rs >= R.size());
		}
		if(s < -8192) {
			// conquered
			return new Color((int) (R.get(rs)*0.5*t),
					(int) (G.get(rs)*0.5*t),
					(int) (B.get(rs)*0.5*t));
		}else if(s < 0) {
			// normal
			return new Color((int) (R.get(rs)*t),
					(int) (G.get(rs)*t),
					(int) (B.get(rs)*t));
		}else if(s < 8192) {
			// defensive
			return new Color((int) (R.get(rs)*t*0.25),
					(int) (G.get(rs)*t*0.25),
					(int) (B.get(rs)*t*0.25));
		}else {
			// offensive
			return new Color((int) (R.get(rs)*t*1.5),
					(int) (G.get(rs)*t*1.5),
					(int) (B.get(rs)*t*1.5));
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		if(gameClient.loaded) {
			OutputData d = data;
			for(int i = 0; i < d.state.length; i++) {
				for(int j = 0; j < d.state[0].length; j++) {
					g.setColor(get(d.state[i][j], getRf(i, j, d)));
					g.fillRect((i-1)*pw-d.sX%pw, (j-1)*pw-d.sY%pw, pw, pw);
				}
			}
			//draw pie chart
			int sum = 0;
			int lastSum = 0;
			int total = 0;
			int size = pw*10;
			for(int i = 1; i < data.territory.length; i++)
				total += data.territory[i];
			for(int i = 1; i < data.territory.length; i++) {
				if(i >= R.size()) {
					do {
						R.add(Math.cos(R.size())*127+128);
						G.add(Math.cos(G.size()+2.09439510239)*127+128);
						B.add(Math.cos(B.size()+4.18879020479)*127+128);
					} while(i >= R.size());
				}
				sum += data.territory[i];
				g.setColor(new Color((int) (double) R.get(i), (int) (double) G.get(i), (int) (double) B.get(i)));
				g.fillArc(gc.getWidth()-size-pw, pw, size, size, (float) lastSum / (float) total * 360.0f, (float) sum/ (float) total * 360.0f);
				lastSum = sum;
			}
		}
	}
	
	public double getRf(int i, int j, OutputData d) {
		int x = ((i+d.sX/pw)%rfw+rfw)%rfw,
				y = ((j+d.sY/pw)%rfh+rfh)%rfh;
//		if(d.state[i][j] < -8192 /* conquered */) rf[x][y] += Math.random()*0.1-0.05;
//		else if(d.state[i][j] < 0 /* normal */) rf[x][y] = 1;
//		else if(d.state[i][j] < 8192 /* defense */) rf[x][y] = 1;
//		else {
		rf[x][y] += Math.random()*0.025-0.0125;
		rf[x][y] = Math.max(Math.min(rf[x][y], 1), 0.75);
//		}
		return rf[x][y];
	}
	
	public void update(GameContainer gc) {
		clientData.exited = ClientMain.exited;
		clientData.dx = (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D) ? 1 : 0) -
				(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A) ? 1 : 0);
		clientData.dy = (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S) ? 1 : 0) -
				(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W) ? 1 : 0);
		clientData.pixW = ClientMain.pixW;
		clientData.attack = Keyboard.isKeyDown(Keyboard.KEY_X);
		clientData.defend = Keyboard.isKeyDown(Keyboard.KEY_Z);
		clientData.slothShortcut = Keyboard.isKeyDown(Keyboard.KEY_1);
		clientData.greedShortcut = Keyboard.isKeyDown(Keyboard.KEY_2);
		try {
			gameClient.sendData(Serializer.serialize(clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
