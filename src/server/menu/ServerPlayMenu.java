package server.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;

import net.ServerData;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import server.Menu;
import server.ServerMain;
import world.World;
import client.Button;
import client.Colors;

public class ServerPlayMenu implements Menu {
	
	private Button exitButton;
	
	private static String str1, str2;
	
	public void init(GameContainer gc) {
		exitButton = new Button(gc.getWidth()/2, 3*gc.getHeight()/4, "Exit", ServerMain.font);
		try {
			str1 = "Private IP: " + Inet4Address.getLocalHost().getHostAddress();
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
	                whatismyip.openStream()));
			str2 = "Public IP: " + in.readLine();
		} catch (Exception e) {
			// computer is offline
			if(str1 == null) {
				str1 = "PRIVATE IP NOT AVAILABLE";
			}
			if(str2 == null) {
				str2 = "PUBLIC IP NOT AVAILABLE";
			}
		}
	}
	public void render(GameContainer gc, Graphics g) {
		//draw ips
		g.setColor(Colors.titleColor);
		g.setFont(ServerMain.font);
		g.drawString(str1, gc.getWidth()/2 - ServerMain.font.getWidth(str1)/2, gc.getHeight()/2-ServerMain.font.getHeight());
		g.drawString(str2, gc.getWidth()/2 - ServerMain.font.getWidth(str2)/2, gc.getHeight()/2+ServerMain.font.getHeight());
		//exit button
		exitButton.render(gc, g);
	}
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(input.isKeyDown(Input.KEY_ESCAPE)) {
			ServerMain.data.saveLocalImage();
			System.exit(1);
		}
		if(mousePressed && exitButton.isHovered(gc)) {
			ServerMain.menu = ServerMain.serverSetupMenu;
			ServerMain.server.socket.close();
		}
		ServerMain.data.update(); //(double) delta / 1000.0
	}
	
}
