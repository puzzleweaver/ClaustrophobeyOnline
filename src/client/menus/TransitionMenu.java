package client.menus;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import client.ClientMain;
import client.Colors;
import main.Main;
import main.Menu;

public class TransitionMenu implements Menu {
	
	public ArrayList<Integer> fpx, fpy;
	private Menu preTrans, postTrans;
	private Image a, b, c;
	private Graphics g1;
	private int speed = 500;
	private boolean filling;
	private boolean indeces[][];
	
	public TransitionMenu(Menu preTrans, Menu postTrans) {
		this.preTrans = preTrans;
		this.postTrans = postTrans;
		try {
			a = new Image(ClientMain.WIDTH, ClientMain.HEIGHT);
			b = new Image(ClientMain.WIDTH, ClientMain.HEIGHT);
			c = new Image(ClientMain.WIDTH, ClientMain.HEIGHT);
			g1 = b.getGraphics();
			postTrans.render(ClientMain.defaultGC, g1);
			a.getGraphics().drawImage(b, 0, 0);
			preTrans.render(ClientMain.defaultGC, g1);
			c.getGraphics().drawImage(b, 0, 0);
		}catch(SlickException e) {}
		indeces = new boolean[ClientMain.WIDTH/ClientMain.pixW][ClientMain.HEIGHT/ClientMain.pixW];
		initialize();
	}

	public void init(GameContainer gc) {
		preTrans.render(gc, g1);
	}

	private void add(int x, int y) {
		if(x < 0 || x >= indeces.length || y < 0 || y >= indeces[0].length || indeces[x][y] != filling)
			return;
		for(int i = 0; i < fpx.size(); i++) {
			if(fpx.get(i) == x && fpy.get(i) == y)
				return;
		}
		fpx.add(x);
		fpy.add(y);
	}
	
	private void initialize() {
		fpx = new ArrayList<>();
		fpy = new ArrayList<>();
		for(int i = 0; i < indeces.length; i++) {
			fpx.add(i);
			fpy.add(0);
		}
	}

	public void update(GameContainer gc) {}

	public void render(GameContainer gc, Graphics g) {
		int rid, x, y;
		if(!filling)
			g.drawImage(c, 0, 0);
		for(int i = 0; i < speed; i++) {
			if(fpx.size() == 0) {
				// after this all of the data in this class will be written over
				if(filling) {
					ClientMain.menu = postTrans;
				}
				filling = true;
				initialize();
			}
			rid = Main.r.nextInt(fpx.size());
			x = fpx.get(rid);
			y = fpy.get(rid);
			fpx.remove(rid);
			fpy.remove(rid);
			indeces[x][y] = !indeces[x][y];
			if(filling) {
				g1.drawImage(a.getSubImage(x*ClientMain.pixW, y*ClientMain.pixW, ClientMain.pixW, ClientMain.pixW), x*ClientMain.pixW, y*ClientMain.pixW);
			}else {
				g1.setColor(Colors.backgroundColor.darker(1f-(Main.r.nextInt(55)+200f)/256f));
				g1.fillRect(x*ClientMain.pixW, y*ClientMain.pixW, ClientMain.pixW, ClientMain.pixW);
			}
			add(x+1, y);
			add(x-1, y);
			add(x, y+1);
			add(x, y-1);
		}
		g.drawImage(b, 0, 0);
	}

}
