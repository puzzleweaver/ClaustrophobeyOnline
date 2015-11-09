package main.game;

import java.io.Serializable;

import main.Main;

public class State implements Serializable {
	
	private static final long serialVersionUID = -8759406448009482535L;
	
	public int type;
	public double t = Math.random();
	
	public State(int type) {
		this.type = type;
	}
	
	public void update() {
		if(type == 0) t += 0.01*Math.random()-0.005;
		else if(type == 1) t += 0.02*Math.random()-0.01;
		else t = t += 0.04*Math.random()-0.02;
		t = Math.max(Math.min(t, 1), 0);
	}
	
}
