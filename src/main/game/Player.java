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
	public int ldx, ldy;
	
	public Player(int ix, int iy, short PID) {
		for(int i = 0; i < 20 /* initial mass */; i++)
			moveTo(-1, ix, iy);
		this.PID = PID;
	}
	
	public void update(InputData d) {
		if(d.dx == 0 && d.dy == 0) return;
		int pred;
		if(x.size() > 20 && d.defend && (ldx != 0 || ldy != 0)) {
			for(int i = 0; i < x.size(); i++) {
				pred = Main.r.nextInt(3)-1;
				if(Main.data.freeAt(x.get(i)+ldx, y.get(i)+ldy) ||
						Main.r.nextInt(3)==0 && Main.data.freeAt(x.get(i)+(ldx == 0 ? pred:ldx), y.get(i)+(ldy == 0 ? pred:ldy))) {
					hardenID(i);
				}
			}
		}
		double l = Math.max(1, 0.4*Math.sqrt(x.size()));
		for(int i = 0; i < l; i++)
			move(d);
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
		moveTo(Math.random() < 0.01 ? -1:id, fx.get(rid), fy.get(rid));
	}
	
	public void moveTo(int id, int nx, int ny) {
		int oID = (Main.data.state[nx][ny]%8192+8192)%8192;
		Main.data.state[nx][ny] = (short) (PID-8192);
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
