package main.game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import main.Menu;

public class GameMenu implements Menu {

	public int sX, sY;
	
	public static final int STATE_SPACE = 0, STATE_WALL = 1;
	public int w = 50, h = 50;
	public State[][] state = new State[w][h];
	public ArrayList<InputData> data = new ArrayList<InputData>();
	public ArrayList<Player> player = new ArrayList<Player>();
	
	public GameMenu() {
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				state[i][j] = new State(Math.hypot(i-w/2, j-h/2)<w/2 ? STATE_SPACE:STATE_WALL);
			}
		}
	}
	
	public void init() {
		player.add(new Player(25, 25, -1));
		data.add(new InputData());
	}
	
	public void update() {
		for(int i = 0; i < data.size(); i++) {
			data.get(i).update();
			player.get(i).update(data.get(i));
		}
	}

	public void render(Graphics g) {
		int k = 400/w;
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				g.setColor(state[i][j].type == 0 ? Color.white:(state[i][j].type == 1 ? Color.black:Color.red));
				g.fillRect(i*k-sX+200, j*k-sY+200, k, k);
			}
		}
		g.setColor(Color.green);
		g.drawLine(200, 100, 200, 300);
		g.drawLine(100, 200, 300, 200);
	}
	
	public boolean freeAt(int x, int y) {
		if(x < 0 || y < 0 || x >= w || y >= h)
			return false;
		return state[x][y].type == STATE_SPACE;
	}

}
