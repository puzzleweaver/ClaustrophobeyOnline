package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Main;

public class World {

	public static final short STATE_SPACE = 1, STATE_WALL = 0;
	public static int border = 10;
	public static double turnAngle = 0.2 /*0<x<pi*/,
			branchAngle = 0.4 /*0<x<pi/2*/, 
			taper = 0.998 /*0.5<x<0.999*/,
			branchProb = 0.0025 /*0<x<0.025*/,
			splitProb = 0.0035/*0<x<0.025*/,
			nodeProb = 0.01 /*0<x<0.06*/;
	
	// world test - saves what is generated to an image
	public static void main(String[] args) {
		short[][] world = generateWorld();
		int w = world.length, h = world[0].length;
		System.out.println("Generation Completed, Drawing Image");
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				g.setColor(world[i][j] == STATE_SPACE ? Color.red:new Color(128, 0, 0));
				g.fillRect(i, j, 1, 1);
			}
		}
		System.out.println("Drawing Completed, Saving Image");
		try {
			ImageIO.write(img, "png", new File("map.png"));
		} catch(Exception e) {
			System.out.println("ERRRORRSR");
		}
	}
	
	public static short[][] generateWorld() {
		ArrayList<Circ> c = new ArrayList<Circ>();
		startDendrite(c, 0, 0);
		double maxX = Double.MIN_VALUE, minX = Double.MAX_VALUE, maxY = Double.MIN_VALUE, minY = Double.MAX_VALUE;
		Circ circ;
		for(int i = 0; i < c.size(); i++) {
			circ = c.get(i);
			if(circ.x-circ.r < minX) minX = circ.x-circ.r;
			if(circ.x+circ.r > maxX) maxX = circ.x+circ.r;
			if(circ.y-circ.r < minY) minY = circ.y-circ.r;
			if(circ.y+circ.r > maxY) maxY = circ.y+circ.r;
		}
		short[][] world = new short[(int) (maxX-minX+border*2)][(int) (maxY-minY+border*2)];
		for(int i = 0; i < world.length; i++) {
			for(int j = 0; j < world[0].length; j++) {
				world[i][j] = STATE_WALL;
			}
		}
		for(int i = 0; i < c.size(); i++) {
			circ(world, c.get(i).x-minX+border, c.get(i).y-minY+border, c.get(i).r);
		}
		return world;
	}

	private static void startDendrite(ArrayList<Circ> c, double x, double y) {
		double r0 = 5, t0 = (Main.r.nextDouble()*2.0-1.0)*6.28;
		int branches = 5;
		for(int i = 0; i < branches; i++) {
			dendrite(c, x, y, r0, t0+6.282*i/branches, 5);
		}
	}
	
	private static void dendrite(ArrayList<Circ> c, double x, double y, double r0, double t0, int recurs) {
		double t = t0, nodeT = 0;
		for(double r = r0; r > 1; r *= taper) {
			if(nodeT <= 0 && recurs >= 0) {
				nodeT = 0;
				if(Main.r.nextDouble() < splitProb) {
					double diff = Main.r.nextDouble()*branchAngle;
					dendrite(c, x, y, r, t+diff, recurs);
					dendrite(c, x, y, r, t-diff, recurs);
					break;
				}if(Main.r.nextDouble() < branchProb) {
					dendrite(c, x, y, r, t+3.1415926*(Main.r.nextDouble()-0.5), recurs-1);
				}
			}else if(nodeT > 0) {
				nodeT -= 0.2;
			}
			if(Main.r.nextDouble() < nodeProb) nodeT = 2;
			c.add(new Circ(x, y, r*(1.0+2.0*nodeT*nodeT*(nodeT*nodeT-4*nodeT+4))));
			t += (Main.r.nextDouble()-0.5)*turnAngle;
			x += Math.cos(t)*r*0.5;
			y += Math.sin(t)*r*0.5;
		}
	}
	
	private static void circ(short[][] world, double x, double y, double r) {
		for(int i = (int) Math.max(0, x-r); i < (int) Math.min(world.length, x+r); i++) {
			for(int j = (int) Math.max(0, y-r); j < (int) Math.min(world[0].length-1, y+r); j++) {
				if((x-i)*(x-i)+(y-j)*(y-j) < r*r)
					world[i][j] = STATE_SPACE;
			}
		}
	}
	
	public static class Circ {
		public double x, y, r;
		public Circ(double x, double y, double r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}
	}
	
}
