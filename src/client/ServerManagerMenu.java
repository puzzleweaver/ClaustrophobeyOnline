package client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.Menu;
import net.GameSocket;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.TextField;

public class ServerManagerMenu implements Menu {
	
	private Image image;
	private Graphics g2;
	private int sy = 0;
	private boolean enterIP = false;
	
	private TextField nameTextField, ipTextField;
	
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> serverIPList = new ArrayList<>();
	private int selectedServer = -1;
	
	public void init(GameContainer gc) {
		//possible servers we will use - feel free to add any
		//in the future, this should be a text file
		serverNameList.add("This Computer");
		serverNameList.add("Riley Local");
		serverNameList.add("Riley Public");
		serverNameList.add("Austin Public");
		
		serverIPList.add("localhost");
		serverIPList.add("192.168.1.115");
		serverIPList.add("71.46.93.12");
		serverIPList.add("50.90.116.229");
		try {
			nameTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/2 + ClientMain.font.getWidth("Name: ")/2 - gc.getWidth()/8, gc.getHeight()/2-24, gc.getWidth()/4, 48);
			ipTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/2 + ClientMain.font.getWidth("Name: ")/2 - gc.getWidth()/8, gc.getHeight()/2+24, gc.getWidth()/4, 48);
			image = new Image(gc.getWidth(), gc.getHeight()/2);
			g2 = image.getGraphics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		if(enterIP) {
			//labels for name and ip
			g.setFont(ClientMain.font);
			g.setColor(Color.black);
			g.drawString("Name: ", gc.getWidth()/2 - ClientMain.font.getWidth("Name: ")/2 - nameTextField.getWidth()/2, gc.getHeight()/2-24);
			g.drawString("IP: ", gc.getWidth()/2 - ClientMain.font.getWidth("IP: ")/2 - ipTextField.getWidth()/2, gc.getHeight()/2+24);
			//text fields
			nameTextField.render(gc, g);
			ipTextField.render(gc, g);
			//ok button
			g.setColor(isOkButtonHovered() ? Color.green : Color.green.darker());
			g.drawString("OK", gc.getWidth()/2 - ClientMain.font.getWidth("OK")/2, gc.getHeight()/2+72);
		}else {
			//title
			g.setFont(ClientMain.font);
			g.setColor(Color.black);
			g.drawString("Select Server", gc.getWidth()/2 - ClientMain.font.getWidth("Select Server")/2, gc.getHeight()/8);
			//back button
			g.setColor(isBackButtonHovered() ? Color.green : Color.green.darker());
			g.drawString("Back", gc.getWidth()/4 - ClientMain.font.getWidth("Back")/2,  gc.getHeight()/8);
			//server list
			g2.clear();
//			g2.setColor(Color.black);
//			g2.fillRect(0, 0, image.getWidth(), image.getHeight());
			g2.setFont(ClientMain.fontSmall);
			for(int i = 0; i < serverNameList.size(); i++) {
				g2.setColor(isServerHovered(i) || selectedServer == i ? Color.green : Color.green.darker());
				g2.drawString(serverNameList.get(i), image.getWidth()/2 - ClientMain.fontSmall.getWidth(serverNameList.get(i))/2, sy + i*46);
			}
//			g2.setColor(Color.green);
//			g2.setLineWidth(4);
//			g2.drawRect(0, 0, image.getWidth(), image.getHeight());
			g.drawImage(image, 0, gc.getHeight()/4);
			//add server button
			g.setColor(isAddButtonHovered() ? Color.green : Color.green.darker());
			g.drawString("Add Server", gc.getWidth()/2 - ClientMain.font.getWidth("Add Server")/2, 7*gc.getHeight()/8);
			//other buttons
			if(selectedServer >= 0) {
				g.setColor(isPlayButtonHovered() ? Color.green : Color.green.darker());
				g.drawString("Play", gc.getWidth()/4 - ClientMain.font.getWidth("Play")/2, 7*gc.getHeight()/8);
				g.setColor(isRemoveButtonHovered() ? Color.green : Color.green.darker());
				g.drawString("Remove", 3*gc.getWidth()/4 - ClientMain.font.getWidth("Remove")/2, 7*gc.getHeight()/8);
			}
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
			if(mousePressed) {
				if(isAddButtonHovered())
					enterIP = true;
				if(isBackButtonHovered()) {
					ClientMain.menu = ClientMain.mainMenu;
				}
				if(selectedServer >= 0) {
					if(isRemoveButtonHovered()) {
						serverNameList.remove(selectedServer);
						serverIPList.remove(selectedServer);
						selectedServer = -1;
					}
					if(isPlayButtonHovered()) {
						try {
							GameSocket.serverIP = InetAddress.getByName(serverIPList.get(selectedServer));
							ClientMain.menu = new PlayMenu();
							ClientMain.menu.init(gc);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}
				}
				boolean didSelect = false;
				for(int i = 0; i < serverNameList.size(); i++) {
					if(isServerHovered(i)) {
						if(selectedServer != i) selectedServer = i;
						else selectedServer = -1;
						didSelect = true;
					}
				}
				if(!didSelect) selectedServer = -1;
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
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("Add Server")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Add Server") && my > by && my < by+ClientMain.font.getHeight("Add Server");
	}
	private boolean isServerHovered(int i) {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.fontSmall.getWidth(serverNameList.get(i))/2;
		int by = ClientMain.HEIGHT/4 + sy + i*46;
		return my > ClientMain.HEIGHT/4 && my < 3*ClientMain.HEIGHT/4 &&
				mx > bx && mx < bx+ClientMain.fontSmall.getWidth(serverNameList.get(i)) && my > by && my < by+ClientMain.fontSmall.getHeight(serverNameList.get(i));
	}
	private boolean isBackButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/4 - ClientMain.font.getWidth("Back")/2;
		int by = ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Back") && my > by && my < by+ClientMain.font.getHeight("Back");
	}
	
	private boolean isOkButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("OK")/2;
		int by = ClientMain.HEIGHT/2+72;
		return mx > bx && mx < bx+ClientMain.font.getWidth("OK") && my > by && my < by+ClientMain.font.getHeight("OK");
	}
	
	private boolean isPlayButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/4 - ClientMain.font.getWidth("Play")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Play") && my > by && my < by+ClientMain.font.getHeight("Play");
	}
	private boolean isRemoveButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = 3*ClientMain.WIDTH/4 - ClientMain.font.getWidth("Remove")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Remove") && my > by && my < by+ClientMain.font.getHeight("Remove");
	}
	
}
