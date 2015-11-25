package main.game;

import java.util.ArrayList;

import main.Main;
import net.IndividualData;
import net.InputData;
import world.World;

public class Player {
	
	public short PID;
	public ArrayList<Integer> x = new ArrayList<Integer>(),
			y = new ArrayList<Integer>();
	public boolean preference;
	
	public Player(int ix, int iy, short PID) {
		for(int i = 0; i < 100 /* initial mass */; i++)
			moveTo(-1, ix, iy);
		this.PID = PID;
		Main.data.state[ix][iy] = PID;
	}
	
	public void update(InputData d) {
		if(d.dx == 0 && d.dy == 0) return;
		for(int i = 0; i < 6; i++)
			move(d);
		IndividualData data = Main.data.indieData.get(PID-1);
		int nsX = 0, nsY = 0;
		for(int i = 0; i < x.size(); i++) {
			nsX += x.get(i);
			nsY += y.get(i);
		}
		data.sX =  (int) ((3.0*data.sX+data.clientData.pixW*nsX/x.size())*0.25);
		data.sY = (int) ((3.0*data.sY+data.clientData.pixW*nsY/x.size())*0.25);
	}
	
	public void move(InputData d) {
		preference = !preference;
		int dx = d.dx, dy = d.dy;
		if(d.dx != 0 && d.dy != 0) {
			dx = preference?dx:0;
			dy = preference?0:dy;
		}
		ArrayList<Integer> fx = new ArrayList<Integer>(), fy = new ArrayList<Integer>();
		int pred;
		for(int i = 0; i < x.size(); i++) {
			pred = Main.r.nextInt(3)-1;
			if(Main.data.freeAt(x.get(i)+dx, y.get(i)+dy)) {
				fx.add(x.get(i)+dx);
				fy.add(y.get(i)+dy);
			}else if(Main.r.nextInt(3)==0 && Main.data.freeAt(x.get(i)+(dx == 0 ? pred:dx), y.get(i)+(dy == 0 ? pred:dy))) {
				fx.add(x.get(i)+(dx == 0 ? pred:dx));
				fy.add(y.get(i)+(dy == 0 ? pred:dy));
			}
		}
		if(fx.size() == 0) return;
		int id = getFurthestID(dx, dy), rid = Main.r.nextInt(fx.size());
		moveTo(id, fx.get(rid), fy.get(rid));
	}
	
	public void moveTo(int id, int nx, int ny) {
		Main.data.state[nx][ny] = (short) (PID-8192);
		if(id != -1) {
			Main.data.state[x.get(id)][y.get(id)] = (short) (PID-16384);
			x.set(id, nx);
			y.set(id, ny);
		} else {
			x.add(nx);
			y.add(ny);
		}
	}
	
	public int getFurthestID(double dx, double dy) {
		int minID = 0;
		boolean taken = false;
		double minDot = Double.MAX_VALUE, dot;
		for(int i = 0; i < x.size(); i++) {
			dot = x.get(i)*dx+y.get(i)*dy;
			if(dot < minDot && (taken || Math.random() < 0.1)) {
				minDot = dot;
				minID = i;
				taken = true;
			}
		}
		return minID;
	}
	
	public void destroy() {
		for(int i = 0; i < x.size(); i++) {
			Main.data.state[x.get(i)][y.get(i)] = World.STATE_SPACE;
		}
	}
	
}
