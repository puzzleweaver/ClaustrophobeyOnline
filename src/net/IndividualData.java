package net;

import java.io.Serializable;
import java.net.InetAddress;

import main.Main;
import main.game.Player;

public class IndividualData implements Serializable {
	
	private static final long serialVersionUID = 8833302277659030921L;
	
	public InetAddress address;
	public int port;
	public InputData clientData;
	public Player player;
	
	public int sX, sY;
	
	public IndividualData(int index) {
		//PIDs can be {x | 0 < x < 8192}
		int x, y;
		do {
			x = Main.r.nextInt(Main.data.w);
			y = Main.r.nextInt(Main.data.h);
		}while(Main.data.state[x][y] >= -8192);
		player = new Player(x, y, (short) (index));
	}
	
	public void update(double dt) {
		player.update(clientData);
	}
	
}
