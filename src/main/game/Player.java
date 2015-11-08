package main.game;

import java.util.ArrayList;

import main.Main;

public class Player {

	public int PID;
	public ArrayList<Integer> x = new ArrayList<Integer>(),
			y = new ArrayList<Integer>();
	
	public Player(int ix, int iy, int PID) {
		moveTo(-1, ix, iy);
		moveTo(-1, ix, iy+1);
		moveTo(-1, ix-1, iy);
		moveTo(-1, ix+1, iy);
		moveTo(-1, ix, iy-1);
		moveTo(-1, ix+1, iy+1);
		moveTo(-1, ix-1, iy+1);
		moveTo(-1, ix+1, iy-1);
		moveTo(-1, ix, iy-1);
		this.PID = PID;
		Main.gameMenu.state[ix][iy].type = PID;
	}
	
	public void update(InputData d) {
		if(d.dx == 0 && d.dy == 0) return;
		ArrayList<Integer> fx = new ArrayList<Integer>(), fy = new ArrayList<Integer>();
		int nsX = 0, nsY = 0, pred;
		for(int i = 0; i < x.size(); i++) {
			nsX += x.get(i);
			nsY += y.get(i);
			pred = Main.r.nextInt(3)-1;
			if(Main.gameMenu.freeAt(x.get(i)+d.dx, y.get(i)+d.dy)) {
				fx.add(x.get(i)+d.dx);
				fy.add(y.get(i)+d.dy);
			}else if(Main.r.nextInt(3)==0 && Main.gameMenu.freeAt(x.get(i)+(d.dx == 0 ? pred:d.dx), y.get(i)+(d.dy == 0 ? pred:d.dy))) {
				fx.add(x.get(i)+(d.dx == 0 ? pred:d.dx));
				fy.add(y.get(i)+(d.dy == 0 ? pred:d.dy));
			}
		}
		Main.gameMenu.sX = (Main.gameMenu.sX+8*nsX/x.size())/2;
		Main.gameMenu.sY = (Main.gameMenu.sY+8*nsY/x.size())/2;
		System.out.println(fx.size());
		if(fx.size() == 0) return;
		int id = getFurthestID(d.dx, d.dy), rid = Main.r.nextInt(fx.size());
		moveTo(id, fx.get(rid), fy.get(rid));
	}
	
	public void moveTo(int id, int nx, int ny) {
		Main.gameMenu.state[nx][ny].type = PID;
		if(Main.r.nextInt(10)!=0 && id != -1) {
			Main.gameMenu.state[x.get(id)][y.get(id)].type = GameMenu.STATE_SPACE;
			x.set(id, nx);
			y.set(id, ny);
		} else {
			x.add(nx);
			y.add(ny);
		}
	}
	
	public int getFurthestID(double dx, double dy) {
		int minID = 0;
		double minDot = Double.MAX_VALUE, dot;
		for(int i = 0; i < x.size(); i++) {
			dot = x.get(i)*dx+y.get(i)*dy;
			if(dot < minDot) {
				minDot = dot;
				minID = i;
			}
		}
		return minID;
	}
	
}
