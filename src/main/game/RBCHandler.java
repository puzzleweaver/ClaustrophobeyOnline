package main.game;

import java.util.ArrayList;

import main.Main;
import world.World;

public class RBCHandler {

	public ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>();
	public ArrayList<Short> pastID = new ArrayList<>();
	
	public void update() {
		int nx, ny;
		for(int i = 0; i < x.size(); i++) {
			if(Main.data.state[x.get(i)][y.get(i)] != World.STATE_FOOD) {
				x.remove(i);
				y.remove(i);
				pastID.remove(i);
				i--;
				continue;
			}
			if(Main.r.nextInt(3)!=0)
				continue;
			nx = Main.r.nextInt(3)-1;
			ny = Main.r.nextInt(3)-1;
			if(nx == 0 && ny == 0)
				continue;
			nx += x.get(i);
			ny += y.get(i);
			if(Main.data.state[nx][ny] < -8192) {
				Main.data.state[x.get(i)][y.get(i)] = pastID.get(i);
				pastID.set(i, Main.data.state[nx][ny]);
				Main.data.state[nx][ny] = World.STATE_FOOD;
				x.set(i, nx);
				y.set(i, ny);
			}
		}
	}
	
	public void spawn() {
		int nx = Main.data.w/2, ny = Main.data.h/2;
		x.add(nx);
		y.add(ny);
		pastID.add(Main.data.state[nx][ny]);
		Main.data.state[nx][ny] = World.STATE_FOOD;
	}
	
}
