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
	
	public int sX, sY, w, h, pixW;
	
	public IndividualData(int index) {
		//identifying a blob by -1-index because static (non-player states) are >= 0
		player = new Player(Main.data.w/2, Main.data.h/2, -1-index);
	}
	
	public void update(double dt) {
		player.update(clientData);
	}
	
}
