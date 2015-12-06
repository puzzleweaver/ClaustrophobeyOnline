package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MenuBackground {
	
	private static int pw;
	private static int rfw = ClientMain.WIDTH, rfh = ClientMain.HEIGHT;
	private static double rf[][] = new double[rfw][rfh];
	
	public static void init() {
		for(int i = 0; i < rfw; i++) {
			for(int j = 0; j < rfh; j++) {
				rf[i][j] = Math.random()*0.25+0.75;
			}
		}
		pw = ClientMain.pixW;
	}
	public static void render(GameContainer gc, Graphics g) {
		for(int i = 0; i < gc.getWidth()/pw+1; i++) {
			for(int j = 0; j < gc.getHeight()/pw+1; j++) {
				g.setColor(get(getRf(i, j)));
				g.fillRect(i*pw, j*pw, pw, pw);
			}
		}
	}
	
	public static Color get(double t) {
		return Colors.backgroundColor.darker(1-(65*(float) t+64)/256);
	}
	
	public static double getRf(int i, int j) {
		int x = (i%rfw+rfw)%rfw,
				y = (j%rfh+rfh)%rfh;
		rf[x][y] += Math.random()*0.1-0.05;
		rf[x][y] = Math.max(Math.min(rf[x][y], 1), 0.75);
		return rf[x][y];
	}
	
}
