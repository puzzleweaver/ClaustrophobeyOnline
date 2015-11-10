package main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

import net.GameSocket;
import net.InputData;
import net.Serializer;
import net.ServerData;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends GameSocket {
	
	public static Random r = new Random();
	
	public static ServerData data = new ServerData();
	
	public static void main(String[] args) {
		new Main().start();
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ServerScreen());
			app.setDisplayMode(400, 400, false);
			app.setMinimumLogicUpdateInterval(15);
			app.setAlwaysRender(true);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public Main() {
		super();
		try {
			this.socket = new DatagramSocket(PORT);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readData(DatagramPacket packet) {
		try {
			if(data.processData((InputData) Serializer.deserialize(packet.getData()), packet.getAddress(), packet.getPort())) {
				sendData(Serializer.serialize(data.getOutputData()), packet.getAddress(), packet.getPort());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class ServerScreen extends BasicGame {
		
		public ServerScreen() {
			super("YA TOOCHECLOONK");
		}
		
		public void render(GameContainer gc, Graphics g) throws SlickException {
			//GameMenu render method pasted here
			
//			g.setColor(new Color(190, 160, 0));
//			g.fillRect(0, 0, w*8, h*8);
//			int k = 400/w;
//			for(int i = 0; i < w; i++) {
//				for(int j = 0; j < h; j++) {
//					g.setColor(state[i][j].type == 0 ? new Color(250, 200, 0):(state[i][j].type == 1 ? new Color(190, 160, 0):Color.green));
//					g.fillRect(i*k-sX+200, j*k-sY+200, k, k);
//				}
//			}
		}
		public void init(GameContainer gc) throws SlickException {}
		public void update(GameContainer gc, int delta) throws SlickException {
			//menu.update();
			data.update((double) delta / 1000.0);
			for(int i = 0; i < data.w; i++) {
				for(int j = 0; j < data.h; j++) {
					data.state[i][j].update();
				}
			}
		}
		
	}
	
}
