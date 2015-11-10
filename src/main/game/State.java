package main.game;

import java.io.Serializable;

public class State implements Serializable {
	
	private static final long serialVersionUID = -8759406448009482535L;
	
	public int type;
	public double t = Math.random();
	
	public State(int type) {
		this.type = type;
	}
	
}
