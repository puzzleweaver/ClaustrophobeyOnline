package main.game;

import java.io.Serializable;

import main.Main;

public class State implements Serializable {
	
	private static final long serialVersionUID = -8759406448009482535L;
	
	public int type;
	public int t = 127;
	
	public State(int type) {
		this.type = type;
	}
	
	public void update() {
		t += Main.r.nextInt(3)-1;
	}
	
}
