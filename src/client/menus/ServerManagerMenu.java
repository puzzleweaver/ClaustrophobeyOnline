package client.menus;

import main.Menu;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import client.Button;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;
import client.Settings;

public class ServerManagerMenu implements Menu {
	
	private Image image;
	private Graphics g2;
	private int sy = 0;
	
	private int selectedServer = -1;
	
	private Button addButton, editButton, backButton, playButton, removeButton;
	
	public ServerManagerMenu() {
		selectedServer = -1;
		sy = 0;
	}
	
	public void init(GameContainer gc) {
		addButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Add Server", ClientMain.font);
		editButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Edit", ClientMain.font);
		backButton = new Button(gc.getWidth()/8, gc.getHeight()/8, "Back", ClientMain.font);
		playButton = new Button(gc.getWidth()/8, 7*gc.getHeight()/8, "Play", ClientMain.font);
		removeButton = new Button(7*gc.getWidth()/8, 7*gc.getHeight()/8, "Remove", ClientMain.font);
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
		g.setColor(Colors.titleColor);
		g.drawString("Select Server", gc.getWidth()/2 - ClientMain.font.getWidth("Select Server")/2, gc.getHeight()/8);
		//back button
		backButton.render(g);
		//server list
		g2.clear();
		g2.setFont(ClientMain.fontSmall);
		for(int i = 0; i < Settings.name.size(); i++) {
			g2.setColor(isServerHovered(i) || selectedServer == i ? Colors.selectedColor : Colors.textColor);
			g2.drawString(Settings.name.get(i), image.getWidth()/2 - ClientMain.fontSmall.getWidth(Settings.name.get(i))/2, sy + i*(ClientMain.fontSmall.getHeight()+1));
		}
		g.drawImage(image, 0, gc.getHeight()/4);
		//botton buttons
		if(selectedServer == -1) {
			addButton.render(g);
		}else {
			playButton.render(g);
			editButton.render(g);
			removeButton.render(g);
		}
	}
	
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(mousePressed) {
			if(selectedServer == -1) {
				if(addButton.isHovered()) {
					AddServerMenu menu = (AddServerMenu) ClientMain.addServerMenu;
					menu.editing = false;
					menu.nameTextField.setFocus(true);
					menu.ipTextField.setFocus(false);
					ClientMain.menu = menu;
				}
			}else {
				if(editButton.isHovered()) {
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
			if(backButton.isHovered()) {
				ClientMain.menu = new TransitionMenu(ClientMain.serverManagerMenu, ClientMain.mainMenu);
			}
			if(selectedServer >= 0) {
				if(removeButton.isHovered()) {
					Settings.name.remove(selectedServer);
					Settings.ip.remove(selectedServer);
					selectedServer = -1;
				}
				if(playButton.isHovered()) {
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
	
	private boolean isServerHovered(int i) {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.fontSmall.getWidth(Settings.name.get(i))/2;
		int by = ClientMain.HEIGHT/4 + sy + i*(ClientMain.fontSmall.getHeight()+1);
		return my > ClientMain.HEIGHT/4 && my < 3*ClientMain.HEIGHT/4 &&
				mx > bx && mx < bx+ClientMain.fontSmall.getWidth(Settings.name.get(i)) && my > by && my < by+ClientMain.fontSmall.getHeight(Settings.name.get(i));
	}
	
}
