package main.game;

import java.util.ArrayList;

import main.Main;
import net.IndividualData;
import net.InputData;
import world.World;

public class Player {

	// statics
	private static final int MAX_SIZE = 300, DRONE_SIZE = 30, SIZE_BUFF_BONUS = 100, MIN_SIZE = 20;
	public static final int BUFF_DRONES = 0, BUFF_GHOST = 1, BUFF_MAXUP = 2, BUFF_FOODUP = 3, BUFF_ATTSPEED = 4, BUFF_ATTACK = 5, BUFF_DEFENSE = 6;
	public static final int KEY_ATT = 0, KEY_DEF = 1, KEY_GHOST = 2, KEY_DRONE = 3, KEY_MAXIMIZE_MASS = 4, KEY_FF = 5;
	public static final int NUM_KEYS = 6;
	
	// identity vars
	public ArrayList<Integer> x = new ArrayList<Integer>(),
			y = new ArrayList<Integer>();
	public short PID;

	// movement vars
	public int ldx, ldy;
	public boolean dronePressed;
	public boolean preference;
	public short defState;
	InputData input;
	
	public Player(int ix, int iy, short PID) {
		for(int i = 0; i < MIN_SIZE; i++)
			moveTo(-1, ix, iy); //-1 means no cell is being moved, the position is simply added to the list
		this.PID = PID;
	}

	public void update(InputData d) {
		
		input = d;
		
		// maximize mass if dev key is used
		if(d.keys[KEY_MAXIMIZE_MASS]) {
			for(int i = MAX_SIZE-x.size(); i > 0; i--) {
				moveTo(-1, x.get(0), y.get(0));
			}
		}
		
		// create drone if key is pressed
		if(!dronePressed && !d.keys[KEY_ATT] && d.keys[KEY_DRONE] && x.size()-DRONE_SIZE > MIN_SIZE) {
			dronePressed = true;
			int rid;
			ArrayList<Integer> nx = new ArrayList<>(), ny = new ArrayList<>();
			for(int i = 0; i < DRONE_SIZE; i++) {
				rid = this.getFurthestID(-ldx, -ldy);
				nx.add(x.get(rid));
				ny.add(y.get(rid));
				x.remove(rid);
				y.remove(rid);
			}
			Main.data.amoebas.add(nx, ny, ldx, ldy, PID);
		}
		if(!d.keys[KEY_DRONE]) dronePressed = false;
		
		// determine defState
		if(x.size() > MIN_SIZE && d.keys[KEY_ATT])
			defState = (short) (PID+8192);
		else
			defState = (short) (PID-8192);
		
		// move player and use off/def mechanics
		double l = (d.keys[KEY_FF] ? 3:1)*Math.max(1, 0.4*Math.sqrt(x.size()));
		if(x.size() > MIN_SIZE) {
			int rid;
			if(d.keys[KEY_ATT]) { // lose mass if attacking
				delete(getFurthestID(ldx, ldy));
				for(int i = 0; i < l; i++) { // gradually set all constituent states to attacking
					rid = Main.r.nextInt(x.size());
					Main.data.state[x.get(rid)][y.get(rid)] = (short) (PID+8192);
				}
			}else if(d.keys[KEY_DEF]) { // defend if not attacking
				for(int i = 0; i < l; i++) {
					rid = getFurthestID(ldx, ldy);
					Main.data.state[x.get(rid)][y.get(rid)] = PID;
					x.remove(rid);
					y.remove(rid);
				}
			}
		}
		
		//set player back to normal states if not attacking
		if(!d.keys[KEY_ATT]) {
			for(int i = 0; i < 4; i++) {
				int rid = 0;
				rid = Main.r.nextInt(x.size());
				Main.data.state[x.get(rid)][y.get(rid)] = (short) (PID-8192);
			}
		}
		if(d.dx != 0 || d.dy != 0) {
			for(int i = 0; i < l; i++)
				move(d);
			ldx = d.dx;
			ldy = d.dy;
		}

		// scroll
		IndividualData data = Main.data.indieData.get(PID-1);
		int nsX = 0, nsY = 0;
		for(int i = 0; i < x.size(); i++) {
			nsX += x.get(i);
			nsY += y.get(i);
		}
		data.sX = (3.0*data.sX+nsX/x.size())*0.25;
		data.sY = (3.0*data.sY+nsY/x.size())*0.25;
	}

	public void move(InputData d) {
		ArrayList<Integer> fx = new ArrayList<Integer>(), fy = new ArrayList<Integer>();
		int pred;

		// switch between horizontal and vertical movement if moving diagonally
		preference = !preference;
		int dx = d.dx, dy = d.dy; // necessary because inputdata is sometimes recycled
		if(d.dx != 0 && d.dy != 0) {
			dx = preference?d.dx:0;
			dy = preference?0:d.dy;
		}
		
		// determine free spaces in front of cells
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
		
		// return if no free spaces exist
		if(fx.size() == 0) return;
		
		// else move to a random free space, removing it from another player if necessary
		int id = getFurthestID(dx, dy), rid = Main.r.nextInt(fx.size()), s = Main.data.state[fx.get(rid)][fy.get(rid)];
		if(s < 0 && s > -8192) {
			Main.data.indieData.get(s+8191).player.remove(fx.get(rid), fy.get(rid));
		}
		moveTo(id, fx.get(rid), fy.get(rid));
	}

	public boolean freeAt(int nx, int ny) {
		
		short state = Main.data.state[nx][ny];
		// return false if the coord is outside of the world
		if(nx < 0 || ny < 0 || nx >= Main.data.w || ny >= Main.data.h)
			return false;
		// return true if the coord is food
		else if(state == World.STATE_FOOD)
			return true;
		// return true if attacking and it is owned by another player
		else if(defState == PID+8192 && Main.r.nextBoolean() && state != PID-8192 && state >= -8192 && state < 0 &&
				Main.data.indieData.get(state+8191).player.x.size() > MIN_SIZE)
			return true;
		// return true if you are attacking and it is a wall
		else if(defState == PID+8192 && Main.r.nextInt(50) == 0 && state >= 0 && state < 8192)
			return true;
		// return true if it is a free space
		else
			return state < -8192;
	}

	public void moveTo(int id, int nx, int ny) {
		id = (Main.data.state[nx][ny] == World.STATE_FOOD && x.size() < MAX_SIZE) ? -1:id;
		int oID = (Main.data.state[nx][ny]%8192+8192)%8192;
		Main.data.state[nx][ny] = defState;
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

	public void delete(int id) {
		
		Main.data.state[x.get(id)][y.get(id)] = (short) (PID-16384);
		x.remove(id);
		y.remove(id);
		
	}

	public void remove(int nx, int ny) {
		
		// this method is for when a player is attacked
		for(int i = 0; i < x.size(); i++) {
			if(x.get(i) == nx && y.get(i) == ny) {
				x.remove(i);
				y.remove(i);
				i--;
			}
		}
		
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
