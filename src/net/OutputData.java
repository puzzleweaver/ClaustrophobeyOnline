package net;

import java.io.Serializable;
import java.util.ArrayList;

public class OutputData implements Serializable {

	private static final long serialVersionUID = 26822778913446936L;

	public short[][] state;
	public int sX, sY;
	public int[] nodeX, nodeY, nodeR;
	
	public ArrayList<Integer> territory = new ArrayList<>();
	
}
