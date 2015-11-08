package main.game;

import org.lwjgl.input.Keyboard;

import main.Main;

public class InputData {

	public int dx, dy;
	
	boolean preference;
	
	//completely temporary:
	public void update() {
		preference = !preference;
		dx = 0;
		dy = 0;
		if(preference) {
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				dy = -1;
				dx = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				dy = 1;
				dx = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				dx = 1;
				dy = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				dx = -1;
				dy = 0;
			}
		}else {
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				dx = 1;
				dy = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				dx = -1;
				dy = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				dy = -1;
				dx = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				dy = 1;
				dx = 0;
			}
		}
	}
	
}
