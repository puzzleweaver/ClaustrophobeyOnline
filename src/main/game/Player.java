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
		for(int i = 0; i < 100; i++)
			moveTo(-1, ix, iy);
		this.PID = PID;
		Main.data.state[ix][iy] = PID;
	}
	
	public void update(InputData d) {
		if(d.dx == 0 && d.dy == 0) return;
		if(d.dx != 0 && d.dy != 0) {
			preference = !preference;
			d.dx = preference?d.dx:0;
			d.dy = preference?0:d.dy;
		}
		for(int i = 0; i < 3; i++)
			move(d);
	}
	
	public void move(InputData d) {
		ArrayList<Integer> fx = new ArrayList<Integer>(), fy = new ArrayList<Integer>();
		int nsX = 0, nsY = 0, pred;
		for(int i = 0; i < x.size(); i++) {
			nsX += x.get(i);
			nsY += y.get(i);
			pred = Main.r.nextInt(3)-1;
			if(Main.data.freeAt(x.get(i)+d.dx, y.get(i)+d.dy)) {
				fx.add(x.get(i)+d.dx);
				fy.add(y.get(i)+d.dy);
			}else if(Main.r.nextInt(3)==0 && Main.data.freeAt(x.get(i)+(d.dx == 0 ? pred:d.dx), y.get(i)+(d.dy == 0 ? pred:d.dy))) {
				fx.add(x.get(i)+(d.dx == 0 ? pred:d.dx));
				fy.add(y.get(i)+(d.dy == 0 ? pred:d.dy));
			}
		}
		IndividualData data = Main.data.indieData.get(-PID-1);
		data.sX = (data.sX+data.clientData.pixW*nsX/x.size())/2;
		data.sY = (data.sY+data.clientData.pixW*nsY/x.size())/2;
		if(fx.size() == 0) return;
		int id = getFurthestID(d.dx, d.dy), rid = Main.r.nextInt(fx.size());
		moveTo(id, fx.get(rid), fy.get(rid));
	}
	
	public void moveTo(int id, int nx, int ny) {
		Main.data.state[nx][ny] = PID;
		if(id != -1) {
			Main.data.state[x.get(id)][y.get(id)] = World.STATE_SPACE;
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
	
	public void destroy() {
		for(int i = 0; i < x.size(); i++) {
			Main.data.state[x.get(i)][y.get(i)] = World.STATE_SPACE;
		}
	}
	
}
