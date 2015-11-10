package main.game;

import java.io.Serializable;

public class State implements Serializable {
	
	private static final long serialVersionUID = -8759406448009482535L;
	
	public short type;
	
	public State(short type) {
		this.type = type;
	}
	
}
