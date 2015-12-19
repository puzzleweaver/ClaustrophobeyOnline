package client.menu;

import java.util.ArrayList;

import net.GameClient;
import net.GameSocket;
import net.OutputData;
import net.Serializer;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import server.Menu;
import server.game.Player;
import world.World;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;

public class PlayMenu implements Menu {
	
	public int gameMode;
	private int pw;
	private int rfw = ClientMain.WIDTH, rfh = ClientMain.HEIGHT;
	private double rf[][] = new double[rfw][rfh];
	
	public static final ArrayList<Double> R = new ArrayList<Double>(),
			G = new ArrayList<Double>(),
			B = new ArrayList<Double>();
	
	public void init(GameContainer gc) {
		for(int i = 0; i < rfw; i++) {
			for(int j = 0; j < rfh; j++) {
				rf[i][j] = Math.random()*0.25+0.75;
			}
		}
		pw = ClientMain.pixW;
	}
	
	public Color get(short s, double t) {
		
		// return set colors
		if(s == World.STATE_BEDROCK) return Colors.worldBedrock.darker(1-(65*(float) t+64)/256);
		else if(s == World.STATE_WALL) return Colors.worldWall.darker(1-(65*(float) t+64)/256);
		else if(s == World.STATE_SPACE) return Colors.worldSpace.darker(1-(65*(float) t+64)/256);
		// 
		
		// initialize colors if they don't already exist
		int pid = (short) (((s+8192)%8192 + 8192)%8192);
		if(pid >= R.size()) {
			do {
				R.add(Math.cos(R.size())*127+128);
				G.add(Math.cos(G.size()+2.09439510239)*127+128);
				B.add(Math.cos(B.size()+4.18879020479)*127+128);
			} while(pid >= R.size());
		}
		
		if(s < -8192) {
			// conquered
			return new Color((int) (R.get(pid)*0.5*t),
					(int) (G.get(pid)*0.5*t),
					(int) (B.get(pid)*0.5*t));
		}else if(s < 0) {
			// normal
			return new Color((int) (R.get(pid)*t),
					(int) (G.get(pid)*t),
					(int) (B.get(pid)*t));
		}else if(s < 8192) {
			// defensive
			return new Color((int) (R.get(pid)*t*0.25),
					(int) (G.get(pid)*t*0.25),
					(int) (B.get(pid)*t*0.25));
		}else {
			// offensive
			return new Color((int) (R.get(pid)*t*1.5),
					(int) (G.get(pid)*t*1.5),
					(int) (B.get(pid)*t*1.5));
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		g.setFont(ClientMain.font);
		if(ClientMain.gameClient.loaded && ClientMain.data.started) {
			
			OutputData d = ClientMain.data;
			for(int i = 0; i < d.state.length; i++) {
				for(int j = 0; j < d.state[0].length; j++) {
					g.setColor(get(d.state[i][j], getRf(i, j, d)));
					g.fillRect((i-1)*pw-d.sX%pw, (j-1)*pw-d.sY%pw, pw, pw);
				}
			}
			
			// draw names over players
			g.setColor(new Color(255, 255, 255, 192));
			for(int i = 0; i < d.names.length; i++) {
				g.drawString(d.names[i], d.nameX[i]-ClientMain.font.getWidth(d.names[i])/2, d.nameY[i]-ClientMain.font.getHeight()*2);
			}
			
			// draw pie chart
//			int sum = 0;
//			int lastSum = 0;
//			int total = 0;
//			int size = rfw/10;
//			for(int i = 1; i < data.territory.length; i++)
//				total += data.territory[i];
//			for(int i = 1; i < data.territory.length; i++) {
//				if(i >= R.size()) {
//					do {
//						R.add(Math.cos(R.size())*127+128);
//						G.add(Math.cos(G.size()+2.09439510239)*127+128);
//						B.add(Math.cos(B.size()+4.18879020479)*127+128);
//					} while(i >= R.size());
//				}
//				sum += data.territory[i];
//				g.setColor(new Color((int) (double) R.get(i), (int) (double) G.get(i), (int) (double) B.get(i)));
//				g.fillArc(gc.getWidth()-size-pw, pw, size, size, (float) lastSum / (float) total * 360.0f, (float) sum/ (float) total * 360.0f);
//				lastSum = sum;
//			}
			
			//draw leaderboard
			g.setFont(ClientMain.fontSmall);
			g.drawString("Leaderboard", 3*gc.getWidth()/4 - ClientMain.fontSmall.getWidth("Leaderboards")/2, ClientMain.fontSmall.getHeight());
//			data.leaderboard = new String[data.territory.length];
//			for(int i = 0; i < data.leaderboard.length; i++) {
//				data.leaderboard[i] = "TERRITORY: " + data.territory[i];
//			}
			if(ClientMain.data.leaderboard != null) {
				for(int i = 0; i < ClientMain.data.leaderboard.length; i++) {
					g.drawString((i+1) + ". " + ClientMain.data.leaderboard[i], 3*gc.getWidth()/4 - ClientMain.fontSmall.getWidth((i+1) + ". " + ClientMain.data.leaderboard[i])/2, ClientMain.fontSmall.getHeight()*(2+i));
				}
			}
		} else { // when not connected yet, draw something other than a black screen
			MenuBackground.render(gc, g);
			g.setColor(Colors.textColor);
			g.drawString("Connecting...", rfw/2-ClientMain.font.getWidth("Connecting...")/2, rfh/2-ClientMain.font.getHeight()/2);
		}
	}
	
	public double getRf(int i, int j, OutputData d) {
		int x = ((i+d.sX/pw)%rfw+rfw)%rfw,
				y = ((j+d.sY/pw)%rfh+rfh)%rfh;
		rf[x][y] += Math.random()*0.1-0.05;
		rf[x][y] = Math.max(Math.min(rf[x][y], 1), 0.75);
		return rf[x][y];
	}
	
	public void update(GameContainer gc) {
		ClientMain.clientData.exited = ClientMain.exited;
		ClientMain.clientData.dx = (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D) ? 1 : 0) -
				(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A) ? 1 : 0);
		ClientMain.clientData.dy = (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S) ? 1 : 0) -
				(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W) ? 1 : 0);
		ClientMain.clientData.pixW = ClientMain.pixW;
		ClientMain.clientData.keys = new boolean[Player.NUM_KEYS];
		ClientMain.clientData.keys[Player.KEY_ATT] = Keyboard.isKeyDown(Keyboard.KEY_X);
		ClientMain.clientData.keys[Player.KEY_DEF] = Keyboard.isKeyDown(Keyboard.KEY_Z);
		ClientMain.clientData.keys[Player.KEY_GHOST] = Keyboard.isKeyDown(Keyboard.KEY_C);
		ClientMain.clientData.keys[Player.KEY_DRONE] = Keyboard.isKeyDown(Keyboard.KEY_V);
		ClientMain.clientData.keys[Player.KEY_FF] = Keyboard.isKeyDown(Keyboard.KEY_1);
		ClientMain.clientData.keys[Player.KEY_MAXIMIZE_MASS] = Keyboard.isKeyDown(Keyboard.KEY_2);
		try {
			ClientMain.gameClient.sendData(Serializer.serialize(ClientMain.clientData), GameSocket.serverIP, GameSocket.PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
