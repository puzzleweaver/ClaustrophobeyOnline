package main.game;

import main.Main;

public class State {

	public int type;
	public int t;
	
	public State(int type) {
		this.type = type;
	}
	
	public void update() {
		t += Main.r.nextInt(3)-1;
	}
	
}
