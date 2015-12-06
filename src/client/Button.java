package client;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

public class Button {
	
	private int x, y, w, h;
	private String text;
	private TrueTypeFont font;
	
	public boolean selected = false, enabled = true;
	
	public Button(int x, int y, String text, TrueTypeFont font) {
		this.text = text;
		this.font = font;
		this.x = x - font.getWidth(text)/2;
		this.y = y;
		w = font.getWidth(text);
		h = font.getHeight();
	}
	
	public void render(GameContainer gc, Graphics g) {
		g.setFont(font);
		g.setColor(enabled ? (isHovered(gc) || selected ? Colors.selectedColor : Colors.textColor) : Colors.titleColor);
		g.drawString(text, x, y);
	}
	
	public boolean isHovered(GameContainer gc) {
		Input input = gc.getInput();
		int mx = input.getMouseX();
		int my = input.getMouseY();
		return mx > x && mx < x+w && my > y && my < y+h;
	}
	
}
