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

	public void spawn() {
		if(Main.r.nextInt(100)==0) {
			int x, y;
			do {
				x = Main.r.nextInt(Main.data.w);
				y = Main.r.nextInt(Main.data.h);
			}while(Main.data.state[x][y] != World.STATE_SPACE);
			p.add(new Amoeba(x, y));
		}
	}

	public class Amoeba {

		public ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>();
		public ArrayList<Short> pastID = new ArrayList<>();
		public int dx, dy;
		public boolean df;
		
		public Amoeba(int ix, int iy) {
			for(int i = 0; i < SIZE; i++) {
				x.add(ix+i%5);
				y.add(iy+i/5);
				pastID.add(Main.data.state[ix+i%5][iy+i/5]);
				Main.data.state[ix+i%5][iy+i/5] = World.STATE_FOOD;
			}
		}
		
		private void changeDir() {
			if(Main.r.nextInt(40) == 0) {
				do {
					dx = Main.r.nextInt(3)-1;
					dy = Main.r.nextInt(3)-1;
				} while(dx == 0 && dy == 0);
			}
		}
		public void update() {
			changeDir();
			df = !df;
			int nx = 0, ny = 0, oid = 0;
			
			// calculate nx, ny and oid
			ArrayList<Integer> fx = new ArrayList<>(), fy = new ArrayList<>();
			for(int i = 0; i < x.size(); i++) {
				if(Main.data.state[x.get(i)][y.get(i)] != World.STATE_FOOD) {
					x.remove(i);
					y.remove(i);
					pastID.remove(i);
					i--;
					continue;
				}
				nx = x.get(i)+(df?Main.r.nextInt(3)-1:dx);
				ny = y.get(i)+(df?dy:Main.r.nextInt(3)-1);
				if(freeAt(nx, ny)) {
					fx.add(nx);
					fy.add(ny);
				}
			}
			
			if(fx.size() == 0)
				return;
			
			int rid = Main.r.nextInt(fx.size());
			nx = fx.get(rid);
			ny = fy.get(rid);
			
			oid = getFurthestID(dx, dy);
			
			Main.data.state[x.get(oid)][y.get(oid)] = pastID.get(oid);
			pastID.set(oid, Main.data.state[nx][ny]);
			x.set(oid, nx);
			y.set(oid, ny);
			Main.data.state[nx][ny] = World.STATE_FOOD;
		}
		
		public boolean freeAt(int x, int y) {
			return Main.data.state[x][y] < -8192;
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

}
