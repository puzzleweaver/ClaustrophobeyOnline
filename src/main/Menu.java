package main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Menu {
	
	public static final Color TEXT_COLOR = Color.green.darker(), SELECTED_COLOR = Color.green, TITLE_COLOR = Color.gray, TYPE_COLOR = Color.gray;
	
	public abstract void init(GameContainer gc); //this is actually necessary, don't delete it Austin
	public abstract void update(GameContainer gc);
	public abstract void render(GameContainer gc, Graphics g);
	
}
