package net;

import java.io.Serializable;

public class OutputData implements Serializable {
	
	private static final long serialVersionUID = 26822778913446936L;
	
	public short[][] state;
	public int sX, sY;
	public int[] territory;
	public String[] names, leaderboard;
	public int[] nameX, nameY;
	public short gameMode;
	public boolean started;
	
}
