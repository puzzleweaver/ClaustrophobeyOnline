package client.menus;

import java.net.InetAddress;
import java.net.UnknownHostException;

import main.Menu;
import net.GameSocket;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.TextField;

import client.ClientMain;
import client.MenuBackground;
import client.Settings;

public class ServerManagerMenu implements Menu {
	
	private Image image;
	private Graphics g2;
	private int sy = 0;
	
	private int selectedServer = -1;
	
	public ServerManagerMenu() {
		selectedServer = -1;
		sy = 0;
	}
	
	public void init(GameContainer gc) {
		try {
			image = new Image(gc.getWidth(), gc.getHeight()/2);
			g2 = image.getGraphics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//title
		g.setFont(ClientMain.font);
		g.setColor(Menu.TITLE_COLOR);
		g.drawString("Select Server", gc.getWidth()/2 - ClientMain.font.getWidth("Select Server")/2, gc.getHeight()/8);
		//back button
		g.setColor(isBackButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
		g.drawString("Back", gc.getWidth()/8 - ClientMain.font.getWidth("Back")/2,  gc.getHeight()/8);
		//server list
		g2.clear();
//			g2.setColor(Color.black);
//			g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2.setFont(ClientMain.fontSmall);
		for(int i = 0; i < Settings.name.size(); i++) {
			g2.setColor(isServerHovered(i) || selectedServer == i ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g2.drawString(Settings.name.get(i), image.getWidth()/2 - ClientMain.fontSmall.getWidth(Settings.name.get(i))/2, sy + i*(ClientMain.fontSmall.getHeight()+1));
		}
//			g2.setColor(Color.green);
//			g2.setLineWidth(4);
//			g2.drawRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, gc.getHeight()/4);
		if(selectedServer == -1) {
			//add server button
			g.setColor(isAddButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("Add Server", gc.getWidth()/2 - ClientMain.font.getWidth("Add Server")/2, 7*gc.getHeight()/8);
		}else {
			//edit server button
			g.setColor(isEditButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("Edit", gc.getWidth()/2 - ClientMain.font.getWidth("Edit")/2, 7*gc.getHeight()/8);
		}
		//other buttons
		if(selectedServer >= 0) {
			g.setColor(isPlayButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("Play", gc.getWidth()/8 - ClientMain.font.getWidth("Play")/2, 7*gc.getHeight()/8);
			g.setColor(isRemoveButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("Remove", 7*gc.getWidth()/8 - ClientMain.font.getWidth("Remove")/2, 7*gc.getHeight()/8);
		}
	}
	
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(mousePressed) {
			if(selectedServer == -1) {
				if(isAddButtonHovered()) {
					AddServerMenu menu = (AddServerMenu) ClientMain.addServerMenu;
					menu.editing = false;
					menu.nameTextField.setFocus(true);
					menu.ipTextField.setFocus(false);
					ClientMain.menu = menu;
				}
			}else {
				if(isEditButtonHovered()) {
					AddServerMenu menu = (AddServerMenu) ClientMain.addServerMenu;
					menu.editing = true;
					menu.nameTextField.setFocus(true);
					menu.ipTextField.setFocus(false);
					menu.nameTextField.setText(Settings.name.get(selectedServer));
					menu.ipTextField.setText(Settings.ip.get(selectedServer));
					menu.nameTextField.setCursorPos(menu.nameTextField.getText().length());
					menu.ipTextField.setCursorPos(menu.ipTextField.getText().length());
					menu.selectedServer = selectedServer;
					ClientMain.menu = menu;
				}
			}
			if(isBackButtonHovered()) {
				ClientMain.menu = new TransitionMenu(ClientMain.serverManagerMenu, ClientMain.mainMenu);
			}
			if(selectedServer >= 0) {
				if(isRemoveButtonHovered()) {
					Settings.name.remove(selectedServer);
					Settings.ip.remove(selectedServer);
					selectedServer = -1;
				}
				if(isPlayButtonHovered()) {
					NicknameMenu menu = (NicknameMenu) ClientMain.nicknameMenu;
					menu.nicknameTextField.setText("");
					menu.selectedServer = selectedServer;
					ClientMain.menu = menu;
				}
			}
			boolean didSelect = false;
			for(int i = 0; i < Settings.name.size(); i++) {
				if(isServerHovered(i)) {
					if(selectedServer != i) selectedServer = i;
					else selectedServer = -1;
					didSelect = true;
				}
			}
			if(!didSelect) selectedServer = -1;
		}
		sy += Mouse.getDWheel()/4;
		if(image.getHeight() < Settings.name.size()*(ClientMain.fontSmall.getHeight()+1) && sy < image.getHeight()-Settings.name.size()*46)
			sy = image.getHeight()-Settings.name.size()*(ClientMain.fontSmall.getHeight()+1);
		if(image.getHeight() > Settings.name.size()*(ClientMain.fontSmall.getHeight()+1) || sy > 0)
			sy = 0;
	}
	
	private boolean isAddButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("Add Server")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Add Server") && my > by && my < by+ClientMain.font.getHeight("Add Server");
	}
	private boolean isEditButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("Edit")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Edit") && my > by && my < by+ClientMain.font.getHeight("Edit");
	}
	private boolean isServerHovered(int i) {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.fontSmall.getWidth(Settings.name.get(i))/2;
		int by = ClientMain.HEIGHT/4 + sy + i*(ClientMain.fontSmall.getHeight()+1);
		return my > ClientMain.HEIGHT/4 && my < 3*ClientMain.HEIGHT/4 &&
				mx > bx && mx < bx+ClientMain.fontSmall.getWidth(Settings.name.get(i)) && my > by && my < by+ClientMain.fontSmall.getHeight(Settings.name.get(i));
	}
	private boolean isBackButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/8 - ClientMain.font.getWidth("Back")/2;
		int by = ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Back") && my > by && my < by+ClientMain.font.getHeight("Back");
	}
	private boolean isPlayButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/8 - ClientMain.font.getWidth("Play")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Play") && my > by && my < by+ClientMain.font.getHeight("Play");
	}
	private boolean isRemoveButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = 7*ClientMain.WIDTH/8 - ClientMain.font.getWidth("Remove")/2;
		int by = 7*ClientMain.HEIGHT/8;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Remove") && my > by && my < by+ClientMain.font.getHeight("Remove");
	}
	
}
