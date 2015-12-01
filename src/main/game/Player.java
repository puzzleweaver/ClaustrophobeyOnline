package main.game;

import java.util.ArrayList;

import main.Main;
import net.IndividualData;
import net.InputData;
import world.World;

public class Player {

	public static final int MAX_SIZE = 400, MIN_SIZE = 20;
	public short PID;
	public ArrayList<Integer> x = new ArrayList<Integer>(),
			y = new ArrayList<Integer>();
	public boolean preference;
	public int ldx, ldy, cost = 0;
	public boolean att;

	public Player(int ix, int iy, short PID) {
		for(int i = 0; i < 20 /* initial mass */; i++)
			moveTo(-1, ix, iy);
		this.PID = PID;
	}

	public void update(InputData d) {
		
		if(d.greedShortcut) {
			for(int i = MAX_SIZE-x.size(); i > 0; i--) {
				moveTo(-1, x.get(0), y.get(0));
			}
		}
		
		double l = (d.slothShortcut ? 3:1)*Math.max(1, 0.4*Math.sqrt(x.size()));
		att = x.size() > MIN_SIZE && d.attack;
		if(x.size() > MIN_SIZE) {
			if(!att && d.defend) {
				for(int i = 0; i < l; i++)
					hardenID(getFurthestID(ldx, ldy));
			}
		}

		if(d.dx != 0 || d.dy != 0) {
			if(att)
				delete(getFurthestID(ldx, ldy));
			for(int i = 0; i < l; i++)
				move(d);
			if(PID != 0) {
				IndividualData data = Main.data.indieData.get(PID-1);
				int nsX = 0, nsY = 0;
				for(int i = 0; i < x.size(); i++) {
					nsX += x.get(i);
					nsY += y.get(i);
				}
				data.sX =  (int) ((3.0*data.sX+data.clientData.pixW*nsX/x.size())*0.25);
				data.sY = (int) ((3.0*data.sY+data.clientData.pixW*nsY/x.size())*0.25);
				ldx = d.dx;
				ldy = d.dy;
			}
		}
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
			if(freeAt(x.get(i)+dx, y.get(i)+dy)) {
				fx.add(x.get(i)+dx);
				fy.add(y.get(i)+dy);
			}else if(Main.r.nextInt(3)==0 && freeAt(x.get(i)+(dx == 0 ? pred:dx), y.get(i)+(dy == 0 ? pred:dy))) {
				fx.add(x.get(i)+(dx == 0 ? pred:dx));
				fy.add(y.get(i)+(dy == 0 ? pred:dy));
			}
		}
		if(fx.size() == 0) return;
		int id = getFurthestID(dx, dy), rid = Main.r.nextInt(fx.size());
		moveTo(id, fx.get(rid), fy.get(rid));
	}

	public boolean freeAt(int nx, int ny) {
		if(nx < 0 || ny < 0 || nx >= Main.data.w || ny >= Main.data.h)
			return false;
		short state = Main.data.state[nx][ny];
		if(state == World.STATE_FOOD)
			return true;
		if(att && Main.r.nextInt(50) == 0 && state >= 0 && state < 8192) {
			return true;
		}
		return state < -8192;
	}

	public void moveTo(int id, int nx, int ny) {
		id = (Main.data.state[nx][ny] == World.STATE_FOOD && x.size() < MAX_SIZE) ? -1:id;
		int oID = (Main.data.state[nx][ny]%8192+8192)%8192;
		Main.data.state[nx][ny] = (short) (PID+(att ? 8192:-8192));
		if(oID != 0) Main.data.terr.set(oID, Main.data.terr.get(oID)-1);
		Main.data.terr.set(PID, Main.data.terr.get(PID)+1);
		if(id != -1) {
			Main.data.state[x.get(id)][y.get(id)] = (short) (PID-16384);
			x.set(id, nx);
			y.set(id, ny);
		} else {
			x.add(nx);
			y.add(ny);
		}
	}

	public void hardenID(int id) {
		Main.data.state[x.get(id)][y.get(id)] = PID;
		x.remove(id);
		y.remove(id);
	}

	public void delete(int id) {
		Main.data.state[x.get(id)][y.get(id)] = (short) (PID-16384);
		x.remove(id);
		y.remove(id);
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

}
