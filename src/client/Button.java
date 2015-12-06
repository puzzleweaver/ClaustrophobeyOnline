package client;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class Button {
	
	private int x, y, w, h;
	private String text;
	private TrueTypeFont font;
	
	public boolean selected = false;
	
	public Button(int x, int y, String text, TrueTypeFont font) {
		this.text = text;
		this.font = font;
		this.x = x - font.getWidth(text)/2;
		this.y = y;
		w = font.getWidth(text);
		h = font.getHeight();
	}
	
	public void render(Graphics g) {
		g.setFont(font);
		g.setColor(isHovered() || selected ? Colors.selectedColor : Colors.textColor);
		g.drawString(text, x, y);
	}
	
	public boolean isHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		return mx > x && mx < x+w && my > y && my < y+h;
	}
	
}
