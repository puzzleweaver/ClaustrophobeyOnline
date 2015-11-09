package net;

import java.io.Serializable;

import main.game.State;

public class OutputData implements Serializable {

	private static final long serialVersionUID = 26822778913446936L;

	public State[][] state;
	public int sX, sY;
	
}
