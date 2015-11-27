package net;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import world.World;

public class ServerData implements Serializable {
	
	private static final long serialVersionUID = 584460438362147743L;

	public ArrayList<IndividualData> indieData = new ArrayList<IndividualData>();
	
	public int index;
	
	public int w, h;
	public short[][] state;
	
	public ServerData() {
		state = World.generateTestWorld();
		w = state.length; h = state[0].length;
	}
	
	public boolean processData(InputData data, InetAddress address, int port) {
		//check if we have received data from this client before
		index = -1;
		for(int i = 0; i < indieData.size(); i++) {
			if(address.equals(indieData.get(i).address) && port == indieData.get(i).port) {
				index = i;
				indieData.get(i).clientData = data;
				break;
			}
		}
		//create space for new data if this is a new client
		if(index == -1) {
			index = indieData.size();
			IndividualData iData = new IndividualData(index+1);
			iData.address = address;
			iData.port = port;
			iData.clientData = data;
			indieData.add(iData);
		}
		//exit if the user exits
//		if(data.exited) {
//			indieData.remove(index);
//			return false;
//		}
		return true;
	}
	
	public OutputData getOutputData() {
		IndividualData id = indieData.get(index);
		OutputData d = new OutputData();
		d.sX = id.sX-id.clientData.w/2;
		d.sY = id.sY-id.clientData.h/2;
		int pw = id.clientData.pixW;
		int i0 = (id.sX-id.clientData.w/2)/pw-1, j0 = (id.sY-id.clientData.h/2)/pw-1;
		int ie = (id.sX+id.clientData.w/2)/pw+1, je = (id.sY+id.clientData.h/2)/pw+1;
		d.state = new short[ie-i0][je-j0];
		d.territory = new int[indieData.size()];
		for(int i = 0; i < indieData.size(); i++) {
			d.territory[i] = (int) Math.floor(Math.random()*100);
		}
		for(int i = i0; i < ie; i++) {
			for(int j = j0; j < je; j++) {
				d.state[i-i0][j-j0] = getState(i /* ><> */, j);
			}
		}
		return d;
	}
	
	private short getState(double x, double y) {
		return state[(int) Math.max(Math.min(x, state.length-1),0)][(int) Math.max(Math.min(y, state[0].length-1),0)];
	}
	
	public void update(double dt) {
		for(int i = 0; i < indieData.size(); i++) {
			indieData.get(i).update(dt);
		}
	}
	
	public boolean freeAt(int x, int y) {
		// conquered: {id | -16384 <= id < -8192}
		// normal: {id | -8192 <= id < 0}
		// defensive: {id | 0 <= id < 8192}
		// offensive: {id | 8192 <= id < 16384}
		if(x < 0 || y < 0 || x >= w || y >= h)
			return false;
		return state[x][y] < -8192;
	}
	
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
