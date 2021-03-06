package net;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import server.game.AmoebaHandler;
import server.game.Leaderboard;
import world.World;

public class ServerData implements Serializable {
	
	private static final long serialVersionUID = 584460438362147743L;
	
	public ArrayList<IndividualData> indieData = new ArrayList<>();
	public AmoebaHandler amoebas = new AmoebaHandler();
	
	public static final short MODE_TERR = 0, MODE_SD = 1, MODE_CTF = 2;
	public short gameMode = MODE_TERR;
	public int numTeams = 1; //1 team is free for all
	public int worldType = World.TYPE_NETWORK;
	public boolean[] buffs = new boolean[7];
	public Leaderboard leaderboard;
	public boolean started = false;
	
	public int index;
	
	public int w, h;
	public short[][] state;
	
	public ArrayList<Integer> terr = new ArrayList<>();
	
	public void init() {
		leaderboard = new Leaderboard();
		state = World.generateWorld(worldType);
		w = state.length; h = state[0].length;
		terr = new ArrayList<>();
		indieData = new ArrayList<>();
		terr.add(0); // because PIDs are shifted over (player.get(0).PID = 1)
	}
	
	public void startGame() {
		started = true;
		for(int i = 0; i < indieData.size(); i++) {
			indieData.get(i).startGame(i);
			leaderboard.add(i+1);
			terr.add(0);
		}
	}
	
	public boolean processData(InputData data, InetAddress address, int port) {
		//check if we have received data from this client before
		index = -1;
		for(int i = 0; i < indieData.size(); i++) {
			if(address.equals(indieData.get(i).address) && port == indieData.get(i).port) {
				index = i;
				indieData.get(i).clientData = data;
				if(indieData.get(i).clientData.exited)
					return false;
				break;
			}
		}
		//create space for new data if this is a new client
		if(index == -1) {
			index = indieData.size();
			IndividualData iData = new IndividualData();
			iData.address = address;
			iData.port = port;
			iData.clientData = data;
			indieData.add(iData);
			if(iData.clientData.exited)
				return false;
		}
		return true;
	}
	
	public OutputData getOutputData() {
		IndividualData id = indieData.get(index);
		OutputData d = new OutputData();
		
		//d.started
		d.started = started;
		
		// d.sX/Y
		d.sX = (int) (id.sX*id.clientData.pixW)-id.clientData.w/2;
		d.sY = (int) (id.sY*id.clientData.pixW)-id.clientData.h/2;
		
		// d.territory
		d.territory = new int[terr.size()];
		for(int i = 0; i < terr.size(); i++) {
			d.territory[i] = terr.get(i);
		}
		
		// d.state
		int pw = id.clientData.pixW;
		int i0 = ((int)(id.sX*pw)-id.clientData.w/2)/pw-1,
				j0 = ((int)(id.sY*pw)-id.clientData.h/2)/pw-1;
		int ie = ((int)(id.sX*pw)+id.clientData.w/2)/pw+1,
				je = ((int)(id.sY*pw)+id.clientData.h/2)/pw+1;
		d.state = new short[ie-i0][je-j0];
		for(int i = i0; i < ie; i++) {
			for(int j = j0; j < je; j++) {
				d.state[i-i0][j-j0] = getState(i /* ><> */, j);
			}
		}
		
		// d.leaderboard
		d.leaderboard = leaderboard.getTopTen();

		// d.gameMode
		d.gameMode = gameMode;
		
		// d.name
		d.names = new String[indieData.size()];
		
		//d.nameX/Y
		//players aren't instantiated until game starts
		d.nameX = new int[indieData.size()];
		d.nameY = new int[indieData.size()];
		for(int i = 0; i < d.names.length; i++) {
			d.names[i] = (started && indieData.get(i).player.ghost) ? "" : indieData.get(i).clientData.nickname;
			if(i == index) {
				d.nameX[i] = id.clientData.w/2;
				d.nameY[i] = id.clientData.h/2;
			}else {
				d.nameX[i] = ((int)indieData.get(i).sX*id.clientData.pixW)-d.sX;
				d.nameY[i] = ((int)indieData.get(i).sY*id.clientData.pixW)-d.sY;
			}
		}
		
		return d;
	}
	
	private short getState(double x, double y) {
		if(x < 0 || x > w || y < 0 || y > h)
			return World.STATE_BEDROCK;
		return state[(int) Math.max(Math.min(x, state.length-1),0)][(int) Math.max(Math.min(y, state[0].length-1),0)];
	}
	
	public void update() {
		amoebas.spawn();
		amoebas.update();
		for(int i = 0; i < indieData.size(); i++) {
			indieData.get(i).update();
		}
		leaderboard.sort();
	}
	
	public void removeExitedUsers() {
		for(int i = 0; i < indieData.size(); i++) {
			if(indieData.get(i).clientData.exited) {
				indieData.remove(i);
				i--;
			}
		}
	}
	
	// the following two methods are temporary and for debugging purposes. they will be gone later.
	public Color get(short s) {
		double t = 1.0;
		if(s == World.STATE_SPACE) return new Color((int) (140+65*t), 0, 0);
		else if(s == World.STATE_WALL) return new Color((int) (32*t+64), 0, 0);
		t *= s < -8192 ? 0.5 : (s < 0 ? 1.0 : (s < 8192 ? 0.2 : 0.0));
		s = (short) (((s+8192)%8192 + 8192)%8192);
		return new Color((int) (t*(Math.cos(s)*127+128)),
				(int) (t*(Math.cos(s+2.09439510239)*127+128)),
				(int) (t*(Math.cos(s+4.18879020479)*127+128)));
	}
	public void saveLocalImage() {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				g.setColor(get(state[i][j]));
				g.fillRect(i, j, 1, 1);
			}
		}
		try {
			ImageIO.write(img, "png", new File("word.png"));
		} catch(Exception e) {}
	}
	
}
