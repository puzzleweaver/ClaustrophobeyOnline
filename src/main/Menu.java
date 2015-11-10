package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Menu {
	
	public abstract void update(GameContainer gc);
	public abstract void render(GameContainer gc, Graphics g);
	
}
