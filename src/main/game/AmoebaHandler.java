package main.game;

import java.util.ArrayList;

import main.Main;
import net.InputData;
import world.World;

public class AmoebaHandler {

	public static final int SIZE = 20, MIN_SIZE = 9;
	public ArrayList<Amoeba> p = new ArrayList<>();
	
	public void update() {
		for(int i = 0; i < p.size(); i++) {
			if(p.get(i).x.size() < MIN_SIZE) {
				p.remove(i);
				i--;
			} else
				p.get(i).update();
		}
	}
	
	public void add(ArrayList<Integer> x, ArrayList<Integer> y, int dx, int dy, short PID) {
		p.add(new Amoeba(x, y, dx, dy, PID));
	}

	public void spawn() {
		if(Main.r.nextInt(100)==0) {
			int x, y;
			do {
				x = Main.r.nextInt(Main.data.w);
				y = Main.r.nextInt(Main.data.h);
			}while(Main.data.state[x][y] >= -8192);
			p.add(new Amoeba(x, y, (short) 0));
		}
	}

	public class Amoeba {

		public ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>();
		public ArrayList<Short> pastID = new ArrayList<>();
		public int dx, dy;
		public boolean preference;
		public short PID;
		
		public Amoeba(int ix, int iy, short PID) {
			this.PID = PID;
			for(int i = 0; i < SIZE; i++) {
				x.add(ix+i%5);
				y.add(iy+i/5);
				pastID.add(Main.data.state[ix+i%5][iy+i/5]);
				Main.data.state[ix+i%5][iy+i/5] = World.STATE_FOOD;
			}
		}
		
		public Amoeba(ArrayList<Integer> nx, ArrayList<Integer> ny, int dx, int dy, short PID) {
			this.x = nx;
			this.y = ny;
			this.dx = dx;
			this.dy = dy;
			this.PID = PID;
			for(int i = 0; i < x.size(); i++) { // in case the states are offensive or ghost for some reason
				Main.data.state[x.get(i)][y.get(i)] = (short) (PID-8192);
			}
			// pastID need not be initialized as it is not used for owned amoebas
		}

		public void update() {
			
			// if probability and not an owned amoeba
			if(PID == 0 && Main.r.nextInt(40) == 0) {
				do {
					dx = Main.r.nextInt(3)-1;
					dy = Main.r.nextInt(3)-1;
				} while(dx == 0 && dy == 0);
			}

			// diagonal thing
			preference = !preference;
			
			// move the amoeba
			for(int i = 0; i < (PID == 0 ? 1:4); i++)
				move();
			
		}
		
		public void move() {
			
			// find the free spaces in front of the amoeba and the furthest ones in the back
			int nx = 0, ny = 0, oid = 0;
			ArrayList<Integer> fx = new ArrayList<>(), fy = new ArrayList<>();
			for(int i = 0; i < x.size(); i++) {
				if(Main.data.state[x.get(i)][y.get(i)] != (short) (PID-8192)) {
					x.remove(i);
					y.remove(i);
					if(PID == 0)
						pastID.remove(i);
					i--;
					continue;
				}
				nx = x.get(i)+(preference?Main.r.nextInt(3)-1:dx);
				ny = y.get(i)+(preference?dy:Main.r.nextInt(3)-1);
				if(freeAt(nx, ny)) {
					fx.add(nx);
					fy.add(ny);
				}
			}

			// return if no free spaces exist to be moved to
			if(fx.size() == 0) {
				if(Main.r.nextInt(20) == 0 && PID != 0) { // amoeba may become free if previously owned
					for(int i = 0; i < x.size(); i++) {
						pastID.add((short) (PID-16384));
						Main.data.state[x.get(i)][y.get(i)] = World.STATE_FOOD;
					}
					PID = 0;
				}
				return;
			}

			int rid = Main.r.nextInt(fx.size());
			nx = fx.get(rid);
			ny = fy.get(rid);
			oid = getFurthestID(dx, dy);

			// set states of new and old positions
			Main.data.state[x.get(oid)][y.get(oid)] = (PID == 0 ? pastID.get(oid) : (short) (PID-16384));
			if(PID == 0)
				pastID.set(oid, Main.data.state[nx][ny]);
			x.set(oid, nx);
			y.set(oid, ny);
			Main.data.state[nx][ny] = (short) (PID-8192);
			
		}
		
		public boolean freeAt(int x, int y) {
			if(x < 0 || y < 0 || x >= Main.data.w || y >= Main.data.h)
				return false;
			return Main.data.state[x][y] < -8192;
		}
		
		public int getFurthestID(double dx, double dy) {
			int minID = 0;
			double minDot = Double.MAX_VALUE, dot;
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

}
