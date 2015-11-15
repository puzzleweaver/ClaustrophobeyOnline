package client;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;

import main.Menu;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.util.ResourceLoader;

public class ServerManagerMenu implements Menu {
	
	private static TrueTypeFont fipps;
	private static TrueTypeFont fippsSmall;
	
	private Image image;
	private Graphics g2;
	private int sy = 0;
	private boolean enterIP = false;
	
	private TextField nameTextField, ipTextField;
	
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> serverIPList = new ArrayList<>();
	
	public void init(GameContainer gc) {
		serverNameList.add("This Computer");
		serverNameList.add("Riley Local");
		serverNameList.add("Riley Public");
		serverNameList.add("Austin Public");
		
		serverIPList.add("localhost");
		serverIPList.add("192.168.1.115");
		serverIPList.add("71.46.93.12");
		serverIPList.add("50.90.116.229");
		Font font;
		InputStream inputStream = ResourceLoader.getResourceAsStream("res/Fipps-Regular.ttf");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			fipps = new TrueTypeFont(font.deriveFont(24f), false);
			fippsSmall = new TrueTypeFont(font.deriveFont(18f), false);
			image = new Image(gc.getWidth(), gc.getHeight()/2);
			g2 = image.getGraphics();
			nameTextField = new TextField(gc, fipps, gc.getWidth()/2, gc.getHeight()/2-24, gc.getWidth()/4, 48);
			ipTextField = new TextField(gc, fipps, gc.getWidth()/2, gc.getHeight()/2+24, gc.getWidth()/4, 48);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		if(enterIP) {
			//background
			g.setColor(Color.red);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			//labels for name and ip
			g.setFont(fipps);
			g.setColor(Color.black);
			g.drawString("Name: ", gc.getWidth()/2 - fipps.getWidth("Name: "), gc.getHeight()/2-24);
			g.drawString("IP: ", gc.getWidth()/2 - fipps.getWidth("IP: "), gc.getHeight()/2+24);
			//text fields
			nameTextField.render(gc, g);
			ipTextField.render(gc, g);
			//ok button
			g.setColor(isOkButtonHovered() ? Color.white : Color.blue.darker());
			g.drawString("OK", gc.getWidth()/2 - fipps.getWidth("OK")/2, gc.getHeight()/2+72);
		}else {
			//background - again, something fancier later
			g.setColor(Color.red);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			//title
			g.setFont(fipps);
			g.setColor(Color.black);
			g.drawString("Select Server", gc.getWidth()/2 - fipps.getWidth("Select Server")/2, gc.getHeight()/8);
			//server list
			g2.setColor(Color.black);
			g2.fillRect(0, 0, image.getWidth(), image.getHeight());
			g2.setFont(fippsSmall);
			for(int i = 0; i < serverNameList.size(); i++) {
				g2.setColor(isServerHovered(i) ? Color.green : Color.blue);
				g2.drawString(serverNameList.get(i), image.getWidth()/2 - fippsSmall.getWidth(serverNameList.get(i))/2, sy + i*46);
			}
			g2.setColor(Color.white);
			g2.setLineWidth(4);
			g2.drawRect(0, 0, image.getWidth(), image.getHeight());
			g.drawImage(image, 0, gc.getHeight()/4);
			//add server button
			g.setColor(isAddButtonHovered() ? Color.white : Color.blue.darker());
			g.drawString("Add Server", gc.getWidth()/2 - fipps.getWidth("Add Server")/2, 7*gc.getHeight()/8);
		}
	}
	
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(enterIP) {
			if(isOkButtonHovered() && mousePressed) {
				if(nameTextField.getText().length() > 0 && ipTextField.getText().length() > 0) {
					serverNameList.add(nameTextField.getText());
					serverIPList.add(ipTextField.getText());
				}
				enterIP = false;
				nameTextField.setText("");
				ipTextField.setText("");
			}
		}else {
			if(isAddButtonHovered() && mousePressed) {
				enterIP = true;
			}
			sy += Mouse.getDWheel()/4;
			if(image.getHeight() < serverNameList.size()*46 && sy < image.getHeight()-serverNameList.size()*46)
				sy = image.getHeight()-serverNameList.size()*46;
			if(image.getHeight() > serverNameList.size()*46 || sy > 0)
				sy = 0;
		}
	}
	
	private boolean isAddButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - fipps.getWidth("Add Server")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+fipps.getWidth("Add Server") && my > by && my < by+fipps.getHeight("Add Server");
	}
	private boolean isServerHovered(int i) {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - fippsSmall.getWidth(serverNameList.get(i))/2;
		int by = ClientMain.HEIGHT/4 + sy + i*46;
		return my > ClientMain.HEIGHT/4 && my < 3*ClientMain.HEIGHT/4 &&
				mx > bx && mx < bx+fippsSmall.getWidth(serverNameList.get(i)) && my > by && my < by+fippsSmall.getHeight(serverNameList.get(i));
	}
	private boolean isOkButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - fipps.getWidth("OK")/2;
		int by = ClientMain.HEIGHT/2+72;
		return mx > bx && mx < bx+fipps.getWidth("OK") && my > by && my < by+fipps.getHeight("OK");
	}
	
}
