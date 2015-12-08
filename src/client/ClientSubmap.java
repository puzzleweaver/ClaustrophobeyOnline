package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import server.ServerMain;

public class ClientSubmap {
	
	private short UNKNOWN = 0, EMPTY = 1, WALL = 2, YOU = 3;
	
	private int x1, y1, pixW, w, h;
	private short[][] world;
	private Image img;
	private Graphics g;
	
	public ClientSubmap(int x1, int y1, int pixW, int w, int h) {
		this.x1 = x1;
		this.y1 = y1;
		this.w = w;
		this.h = h;
		this.pixW = pixW;
	}
	
	public void set(int w, int h) {
		world = new short[w][h];
		try {
			img = new Image(w, h);
			g = img.getGraphics();
		} catch (SlickException e) {
			// why, java?
		}
	}

	private short get(short s) {
		return s >= 0 && s < 8192 ? WALL: (s < 0 && s >= -8192 ? YOU:EMPTY);
	}
	
	public void addData(int sX, int sY, short[][] states) {
		for(int i = 0; i < states.length; i++) {
			for(int j = 0; j < states[0].length; j++) {
				if(!(i+sX < 0 || j+sY < 0 || i+sX >= world.length || j+sY >= world[0].length))
					world[i+sX][j+sY] = get(states[i][j]);
			}
		}
	}
	
	public Color getColor(int i, int j) {
		if(i < 0 || j < 0 || i >= world.length || j >= world[0].length)
			return Color.black;
		return world[i][j] == UNKNOWN ? new Color(ServerMain.r.nextInt(50)+205) : (world[i][j] == EMPTY ? Color.red:world[i][j] == YOU ? Color.blue:new Color(128, 0, 0));
	}
	
	public void render(Graphics g1, int sX, int sY) {
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				g.setColor(getColor(i*2*pixW+sX-w, j*2*pixW+sY-h));
				g.fillRect(pixW*i, pixW*j, pixW, pixW);
			}
		}
		g1.drawImage(img, x1, y1, new Color(255, 255, 255, 200));
	}
	
}
