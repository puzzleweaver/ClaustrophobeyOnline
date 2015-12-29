package net;

import java.io.Serializable;
import java.net.InetAddress;

import server.ServerMain;
import server.game.Player;

public class IndividualData implements Serializable {
	
	private static final long serialVersionUID = 8833302277659030921L;
	
	public InetAddress address;
	public int port;
	public InputData clientData;
	public Player player;
	
	public double sX, sY;
	
	public void startGame(int index) {
		//PIDs can be {x | 0 < x < 8192}
		int x, y;
		do {
			x = ServerMain.r.nextInt(ServerMain.data.w);
			y = ServerMain.r.nextInt(ServerMain.data.h);
		}while(ServerMain.data.state[x][y] >= -8192);
		player = new Player(x, y, (short) (index+1)); //PIDs are shifted over (player.get(0).PID = 1)
	}
	
	public void update() {
		player.update(clientData);
	}
	
}
