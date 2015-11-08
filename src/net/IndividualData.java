package net;

import java.io.Serializable;
import java.net.InetAddress;

import main.game.Player;

public class IndividualData implements Serializable {
	
	private static final long serialVersionUID = 8833302277659030921L;
	
	public InetAddress address;
	public int port;
	public InputData clientData;
	public Player player;
	
	public int sX, sY;
	
	public IndividualData(int index) {
		//identifying a blob by index + 2 because 1 is wall, i think
		player = new Player(25, 25, index + 2);
	}
	
	public void update(double dt) {
		player.update(clientData);
	}
	
}
