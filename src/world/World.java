package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.Main;
import net.ServerData;

public class World {

	public static final int STATE_SPACE = 1, STATE_WALL = 0;
	
	// world test - saves what is generated to an image
	public static void main(String[] args) {
		int w = 500, h = 500;
		int[][] world = generateWorld(w, h);
		System.out.println("Generation Completed, Drawing Image");
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				g.setColor(world[i][j] == STATE_SPACE ? Color.red:Color.green);
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
	
	public static int[][] generateWorld(int w, int h) {
		int[][] world = new int[w][h];
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				world[i][j] = STATE_WALL;
			}
		}
		world = startDendrite(world, 0.5*w, 0.5*h);
		return world;
	}

	private static int[][] startDendrite(int[][] world, double x, double y) {
		double r0 = 5, t0 = (Main.r.nextDouble()*2.0-1.0)*6.28;
		world = dendrite(world, x, y, r0, t0, 5);
		world = dendrite(world, x, y, r0, t0+2.09, 5);
		world = dendrite(world, x, y, r0, t0+4.18, 5);
//		world = dendrite(world, 250, 500, 5, -Math.PI*0.5, 5);
		return world;
	}
	
	private static int[][] dendrite(int[][] world, double x, double y, double r0, double t0, int recurs) {
		double t = t0;
		if(x < 0 || y < 0 || x > world.length || y > world[0].length) return world;
		for(double r = r0; r > 1; r *= 0.995) {
			world = circ(world, x, y, r);
			t += Main.r.nextDouble()-0.5;
			x += Math.cos(t)*r*0.5;
			y += Math.sin(t)*r*0.5;
			if(recurs >= 0)  {
				if(Main.r.nextDouble() < 0.005) {
					double diff = Main.r.nextDouble()*0.5;
					world = dendrite(world, x, y, r, t+diff, recurs-1);
					world = dendrite(world, x, y, r, t-diff, recurs-1);
					break;
				}else if(Main.r.nextDouble() < 0.01) {
					world = dendrite(world, x, y, r, t+Main.r.nextDouble()-Math.PI*0.5, recurs-1);
				}
			}
		}
		return world;
	}
	
	private static int[][] circ(int[][] world, double x, double y, double r) {
		for(int i = (int) Math.max(0, x-r); i < (int) Math.min(world.length, x+r); i++) {
			for(int j = (int) Math.max(0, y-r); j < (int) Math.min(world[0].length-1, y+r); j++) {
				if(world[i][j] == STATE_WALL)
					if((x-i)*(x-i)+(y-j)*(y-j) < r*r)
						world[i][j] = STATE_SPACE;
			}
		}
		return world;
	}
	
}
