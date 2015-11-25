package net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import world.World;

public class ServerData implements Serializable {
	
	private static final long serialVersionUID = 584460438362147743L;

	public ArrayList<IndividualData> indieData = new ArrayList<IndividualData>();
	
	public int index;
	
	public int w, h;
	public short[][] state;
	
	public ServerData() {
		state = World.generateWorld();
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
		if(data.exited) {
			indieData.remove(index);
			return false;
		}
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
		if(x < 0 || y < 0 || x >= w || y >= h)
			return false;
		return state[x][y] < -8192;
	}
	
}
