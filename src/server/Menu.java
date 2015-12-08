package server;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Menu {
	
	public abstract void init(GameContainer gc);
	public abstract void update(GameContainer gc);
	public abstract void render(GameContainer gc, Graphics g);
	
}
