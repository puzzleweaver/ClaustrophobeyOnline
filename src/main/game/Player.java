package main.game;

import java.util.ArrayList;

import main.Main;
import net.IndividualData;
import net.InputData;
import world.World;

public class Player {

	public static final int MAX_SIZE = 300, MIN_SIZE = 20;
	public short PID;
	public ArrayList<Integer> x = new ArrayList<Integer>(),
			y = new ArrayList<Integer>();
	public boolean preference;
	public int ldx, ldy, cost = 0;
	public boolean att;

	public Player(int ix, int iy, short PID) {
		for(int i = 0; i < MIN_SIZE; i++)
			moveTo(-1, ix, iy); //-1 means no cell is being moved, the position is simply added to the list
		this.PID = PID;
	}

	public void update(InputData d) {
		
		if(d.greedShortcut) {
			for(int i = MAX_SIZE-x.size(); i > 0; i--) {
				moveTo(-1, x.get(0), y.get(0));
			}
		}
		
		double l = (d.slothShortcut ? 3:1)*Math.max(1, 0.4*Math.sqrt(x.size()));
		att = x.size() > MIN_SIZE && d.attack; // attack if large enough
		if(x.size() > MIN_SIZE) {
			if(!att && d.defend) { // defend if not attacking
				for(int i = 0; i < l; i++)
					hardenID(getFurthestID(ldx, ldy, false));
			}
		}

		if(d.dx != 0 || d.dy != 0) {
			if(att)
				delete(getFurthestID(ldx, ldy, true));
			for(int i = 0; i < l; i++)
				move(d);
			if(PID != 0) {
				IndividualData data = Main.data.indieData.get(PID-1);
				int nsX = 0, nsY = 0;
				for(int i = 0; i < x.size(); i++) {
					nsX += x.get(i);
					nsY += y.get(i);
				}
				data.sX = (3.0*data.sX+nsX/x.size())*0.25;
				data.sY = (3.0*data.sY+nsY/x.size())*0.25;
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
		int id = getFurthestID(dx, dy, true), rid = Main.r.nextInt(fx.size()), s = Main.data.state[fx.get(rid)][fy.get(rid)];
		if(s < 0 && s > -8192) {
			Main.data.indieData.get(s+8191).player.remove(fx.get(rid), fy.get(rid));
		}
		moveTo(id, fx.get(rid), fy.get(rid));
	}

	public boolean freeAt(int nx, int ny) {
		if(nx < 0 || ny < 0 || nx >= Main.data.w || ny >= Main.data.h)
			return false;
		short state = Main.data.state[nx][ny];
		if(state == World.STATE_FOOD)
			return true;
		if(att && Main.r.nextBoolean() && state != PID-8192 && state >= -8192 && state < 0 && Main.data.indieData.get(state+8191).player.x.size() > MIN_SIZE) {
			return true;
		}
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
	
	public void remove(int nx, int ny) {
		for(int i = 0; i < x.size(); i++) {
			if(x.get(i) == nx && y.get(i) == ny) {
				System.out.println(x.get(i) + ", " + y.get(i));
				x.remove(i);
				y.remove(i);
				i--;
			}
		}
	}

	public int getFurthestID(double dx, double dy, boolean curved) {
		int minID = 0;
		double minDot = Double.MAX_VALUE, dot;
		if(curved)
			if(dx != 0) dy = Main.r.nextDouble()*2-1;
			else dx = Main.r.nextDouble()*2-1;
		for(int i = 0; i < x.size(); i++) {
			dot = x.get(i)*dx+y.get(i)*dy;
			if(minDot > dot) {
				minDot = dot;
				minID = i;
			}
		}
		return minID;
	}

}
