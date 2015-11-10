package net;

import java.io.Serializable;
import java.net.InetAddress;

import main.Main;
import main.game.Player;
import world.World;

public class IndividualData implements Serializable {
	
	private static final long serialVersionUID = 8833302277659030921L;
	
	public InetAddress address;
	public int port;
	public InputData clientData;
	public Player player;
	
	public int sX, sY;
	
	public IndividualData(int index) {
		//identifying a blob by -1-index because static (non-player states) are >= 0
		int x, y;
		do {
			x = Main.r.nextInt(Main.data.w);
			y = Main.r.nextInt(Main.data.h);
		}while(Main.data.state[x][y].type != World.STATE_SPACE);
		player = new Player(x, y, -1-index);
	}
	
	public void update(double dt) {
		player.update(clientData);
	}
	
}
