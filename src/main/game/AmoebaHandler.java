package main.game;

import java.util.ArrayList;

import main.Main;
import net.InputData;
import world.World;

public class AmoebaHandler {

	public static final int SIZE = 20;
	public ArrayList<Amoeba> p = new ArrayList<>();
	
	public void update() {
		for(int i = 0; i < p.size(); i++) {
			if(p.get(i).x.size() == 0) {
				p.remove(i);
				i--;
			} else
				p.get(i).update();
		}
	}

	public void spawn() {
		if(Main.r.nextInt(100)==0) p.add(new Amoeba(Main.data.w/2, Main.data.h/2));
	}

	public class Amoeba {

		public ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>();
		public ArrayList<Short> pastID = new ArrayList<>();
		
		public Amoeba(int ix, int iy) {
			for(int i = 0; i < SIZE; i++) {
				x.add(ix);
				y.add(iy);
				pastID.add(Main.data.state[ix][iy]);
				Main.data.state[ix][iy] = World.STATE_FOOD;
			}
		}
		
		public void update() {
			int nx = 0, ny = 0, oid = 0;
			Main.data.state[x.get(oid)][y.get(oid)] = pastID.get(oid);
		}
		
	}

}
