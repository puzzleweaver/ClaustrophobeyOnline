package net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import main.game.State;
import world.World;

public class ServerData implements Serializable {
	
	private static final long serialVersionUID = 584460438362147743L;

	public ArrayList<IndividualData> indieData = new ArrayList<IndividualData>();
	
	public int index;
	
	public int w = 400, h = 400;
	// message too long error!! NEED TO FIX!!
	public State[][] state = new State[w][h];
	
	public ServerData() {
		int[][] world = World.generateWorld(w, h);
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				state[i][j] = new State(world[i][j]);
			}
		}
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
			IndividualData iData = new IndividualData(index);
			iData.address = address;
			iData.port = port;
			iData.clientData = data;
			indieData.add(iData);
		}
		//exit if the user exits
		if(data.exited) {
			indieData.get(index).player.destroy();
			indieData.remove(index);
			return false;
		}
		return true;
	}
	
	public OutputData getOutputData() {
		IndividualData id = indieData.get(index);
		OutputData d = new OutputData();
		d.sX = id.sX;
		d.sY = id.sY;
		int pw = id.clientData.pixW, dsW = id.clientData.w/pw+1, dsH = id.clientData.h/pw+1;
		d.state = new State[(int)dsW][(int)dsH];
		for(int i = 0; i < dsW; i++) {
			for(int j = 0; j < dsH; j++) {
				d.state[i][j] = getState((id.sX-id.clientData.w/2)/pw+i /* ><> */, (id.sY-id.clientData.h/2)/pw+j);
			}
		}
		return d;
	}
	
	private State getState(double x, double y) {
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
		return state[x][y].type == World.STATE_SPACE;
	}
	
}
