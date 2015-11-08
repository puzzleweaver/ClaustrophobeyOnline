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
		if(type == 0) t = (99.0*t+Math.random())*0.01;
		else if(type == 1) t = (49.0*t+Math.random())*0.02;
		else t = (9.0*t+Math.random())*0.1;
	}
	
}
