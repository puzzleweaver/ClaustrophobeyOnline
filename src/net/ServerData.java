package net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import main.game.State;

public class ServerData implements Serializable {
	
	private static final long serialVersionUID = 584460438362147743L;

	public ArrayList<IndividualData> indieData = new ArrayList<IndividualData>();
	
	public int index;
	
	public static final int STATE_SPACE = 0, STATE_WALL = 1;
	public int w = 50, h = 50;
	public State[][] state = new State[w][h];
	
	public ServerData() {
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				state[i][j] = new State(Math.hypot(i-w/2, j-h/2)<w/2 ? STATE_SPACE:STATE_WALL);
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
	
	public void update(double dt) {
		for(int i = 0; i < indieData.size(); i++) {
			indieData.get(i).update(dt);
		}
	}
	
	public boolean freeAt(int x, int y) {
		if(x < 0 || y < 0 || x >= w || y >= h)
			return false;
		return state[x][y].type == STATE_SPACE;
	}
	
}
