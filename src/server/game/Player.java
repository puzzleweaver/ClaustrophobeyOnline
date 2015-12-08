package server.game;

import java.util.ArrayList;

import net.IndividualData;
import net.InputData;
import server.ServerMain;
import world.World;

public class Player {

	// statics
	private static final int MAX_SIZE = 300, DRONE_SIZE = 30, SIZE_BUFF_BONUS = 100, MIN_SIZE = 20;
	public static final int BUFF_ATT = 0, BUFF_DEF = 1, BUFF_DRONE = 2, BUFF_GHOST = 3, BUFF_MAXUP = 4, BUFF_FOODUP = 5, BUFF_ATTSPEED = 6;
	public static final int KEY_ATT = 0, KEY_DEF = 1, KEY_DRONE = 2, KEY_GHOST = 3, KEY_MAXIMIZE_MASS = 4, KEY_FF = 5;
	public static final int NUM_KEYS = 6;
	
	// identity vars
	public ArrayList<Integer> x = new ArrayList<Integer>(),
			y = new ArrayList<Integer>();
	public short PID;

	// movement vars
	public boolean ghost;
	public int ldx, ldy;
	public boolean dronePressed, preference;
	public short defState;
	InputData input;
	
	public Player(int ix, int iy, short PID) {
		for(int i = 0; i < MIN_SIZE; i++)
			moveTo(-1, ix, iy); //-1 means no cell is being moved, the position is simply added to the list
		this.PID = PID;
	}

	public void update(InputData d) {
		
		input = d;

		d.keys[KEY_ATT] &= ServerMain.data.buffs[BUFF_ATT];
		d.keys[KEY_DEF] &= ServerMain.data.buffs[BUFF_DEF];
		d.keys[KEY_DRONE] &= ServerMain.data.buffs[KEY_DRONE];
		d.keys[KEY_GHOST] &= ServerMain.data.buffs[KEY_GHOST];
		
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
			ServerMain.data.amoebas.add(nx, ny, ldx, ldy, PID);
		}
		if(!d.keys[KEY_DRONE]) dronePressed = false;
		
		// set ghost boolean
		// this is necessary so that the ghost players' names cannot be
		//   seen from other players' screens
		ghost = x.size() > MIN_SIZE && d.keys[KEY_GHOST];
		
		// determine defState
		if(x.size() > MIN_SIZE && d.keys[KEY_ATT])
			defState = (short) (PID+8192);
		else if(ghost)
			defState = World.STATE_SPACE;
		else
			defState = (short) (PID-8192);
		
		// move player and use off/def mechanics
		double l = (d.keys[KEY_FF] ? 3:1)*Math.max(1, 0.4*Math.sqrt(x.size()));
		if(x.size() > MIN_SIZE) {
			int rid;
			if(d.keys[KEY_ATT] || d.keys[KEY_GHOST]) // lose mass if attacking
				delete(getFurthestID(ldx, ldy));
			else if(d.keys[KEY_DEF]) { // defend if not attacking
				for(int i = 0; i < l; i++) {
					rid = getFurthestID(ldx, ldy);
					ServerMain.data.state[x.get(rid)][y.get(rid)] = PID;
					x.remove(rid);
					y.remove(rid);
				}
			}
		}
		
		//set player gradually to default state
		int rid = 0;
		for(int i = 0; i < 4; i++) {
			if(!ghost) {
				rid = ServerMain.r.nextInt(x.size());
				ServerMain.data.state[x.get(rid)][y.get(rid)] = defState;
			}
		}
		
		if(d.dx != 0 || d.dy != 0) {
			for(int i = 0; i < l; i++)
				move(d);
			ldx = d.dx;
			ldy = d.dy;
		}

		// scroll
		IndividualData data = ServerMain.data.indieData.get(PID-1);
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
			pred = ServerMain.r.nextInt(3)-1;
			if(freeAt(x.get(i)+dx, y.get(i)+dy)) {
				fx.add(x.get(i)+dx);
				fy.add(y.get(i)+dy);
			}else if(ServerMain.r.nextInt(3)==0 && freeAt(x.get(i)+(dx == 0 ? pred:dx), y.get(i)+(dy == 0 ? pred:dy))) {
				fx.add(x.get(i)+(dx == 0 ? pred:dx));
				fy.add(y.get(i)+(dy == 0 ? pred:dy));
			}
		}
		
		// return if no free spaces exist
		if(fx.size() == 0) return;
		
		// else move to a random free space, removing it from another player if necessary
		int id = getFurthestID(dx, dy), rid = ServerMain.r.nextInt(fx.size()), s = ServerMain.data.state[fx.get(rid)][fy.get(rid)];
		if(s < 0 && s > -8192) {
			ServerMain.data.indieData.get(s+8191).player.remove(fx.get(rid), fy.get(rid));
		}
		moveTo(id, fx.get(rid), fy.get(rid));
	}

	public boolean freeAt(int nx, int ny) {
		
		short state = ServerMain.data.state[nx][ny];
		
		// return false if the player is invisible and is intersecting itself
		if(ghost) 
			for(int i = 0; i < x.size(); i++)
				if(x.get(i) == nx && y.get(i) == ny)
					return false;
		
		// return false if the coord is outside of the world
		if(nx < 0 || ny < 0 || nx >= ServerMain.data.w || ny >= ServerMain.data.h)
			return false;
		// return true if the coord is food
		else if(state == World.STATE_FOOD)
			return true;
		// return true if attacking and it is owned by another player
		else if(defState == PID+8192 && ServerMain.r.nextBoolean() && state != PID-8192 && state >= -8192 && state < 0 &&
				ServerMain.data.indieData.get(state+8191).player.x.size() > MIN_SIZE)
			return true;
		// return true if you are attacking and it is a wall
		else if(defState == PID+8192 && ServerMain.r.nextInt(50) == 0 && state >= 0 && state < 8192)
			return true;
		// return true if it is a free space
		else
			return state < -8192;
	}

	public void moveTo(int id, int nx, int ny) {
		
		// if moving onto food, id = -1 (so that you gain mass)
		id = (ServerMain.data.state[nx][ny] == World.STATE_FOOD && x.size() < MAX_SIZE) ? -1:id;

		// add territorial changes to territory list
		changeTerr(nx, ny, PID);
		
		// set the new position to default state on the state array
		if(!ghost)
			ServerMain.data.state[nx][ny] = defState;
		
		if(id != -1) {
			// capture trailing territories
			if(!ghost || (ServerMain.data.state[x.get(id)][y.get(id)]%8192+8192)%8192 == PID)
				ServerMain.data.state[x.get(id)][y.get(id)] = (short) (PID-16384);
			x.set(id, nx);
			y.set(id, ny);
		} else {
			x.add(nx);
			y.add(ny);
		}
		
	}

	public void changeTerr(int nx, int ny, short nID) {

		int oID = (ServerMain.data.state[nx][ny]%8192+8192)%8192;
		if(ServerMain.data.state[nx][ny] != World.STATE_FOOD) {
			ServerMain.data.terr.set(oID, ServerMain.data.terr.get(oID)-1);
			if(nID != 0)
				ServerMain.data.terr.set(nID, ServerMain.data.terr.get(nID)+1);
		}
			
	}
	
	public void delete(int id) {
		if(!ghost || ServerMain.data.state[x.get(id)][y.get(id)] == PID-8192) {
			changeTerr(x.get(id), y.get(id), PID);
			ServerMain.data.state[x.get(id)][y.get(id)] = (short) (PID-16384);
		}
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
		if(dx != 0) dy = ServerMain.r.nextDouble()*2-1;
		else dx = ServerMain.r.nextDouble()*2-1;
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
