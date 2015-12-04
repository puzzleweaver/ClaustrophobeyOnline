package client;

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

public class ServerManagerMenu implements Menu {
	
	private Image image;
	private Graphics g2;
	private int sy = 0;
	private boolean enterIP = false, editing = false, enterName = false;
	
	private TextField nameTextField, ipTextField, nicknameTextField;
	
	private int selectedServer = -1;
	
	public void init(GameContainer gc) {
		try {
			nameTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/2 + ClientMain.font.getWidth("Name: ")/2 - gc.getWidth()/4, gc.getHeight()/2-ClientMain.fontSmall.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
			ipTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/2 + ClientMain.font.getWidth("Name: ")/2 - gc.getWidth()/4, gc.getHeight()/2+ClientMain.fontSmall.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
			nicknameTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/4, gc.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
			image = new Image(gc.getWidth(), gc.getHeight()/2);
			g2 = image.getGraphics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		if(enterName) {
			g.setFont(ClientMain.font);
			g.setColor(Menu.TITLE_COLOR);
			g.drawString("Nickname:", gc.getWidth()/2 - ClientMain.font.getWidth("Nickname:")/2, gc.getHeight()/2-ClientMain.font.getHeight());
			nicknameTextField.setBorderColor(Color.black);
			nicknameTextField.render(gc, g);
			//ok button
			g.setColor(isOkButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("OK", gc.getWidth()/2 - ClientMain.font.getWidth("OK")/2, gc.getHeight()/2+ClientMain.font.getHeight()*2);
			//cancel button
			g.setColor(isCancelButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("Cancel", gc.getWidth()/2 - ClientMain.font.getWidth("Cancel")/2, gc.getHeight()/2+ClientMain.font.getHeight()*3);
		}else if(enterIP) {
			//labels for name and ip
			g.setFont(ClientMain.font);
			g.setColor(Menu.TITLE_COLOR);
			g.drawString("Name: ", gc.getWidth()/2 - ClientMain.font.getWidth("Name: ")/2 - nameTextField.getWidth()/2, gc.getHeight()/2-ClientMain.fontSmall.getHeight()/2);
			g.drawString("IP: ", gc.getWidth()/2 - ClientMain.font.getWidth("IP: ")/2 - ipTextField.getWidth()/2, gc.getHeight()/2+ClientMain.fontSmall.getHeight()/2);
			//text fields
			g.setColor(Menu.TYPE_COLOR);
			nameTextField.setBorderColor(Color.black);
			nameTextField.render(gc, g);
			ipTextField.setBorderColor(Color.black);
			ipTextField.render(gc, g);
			//ok button
			g.setColor(isOkButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("OK", gc.getWidth()/2 - ClientMain.font.getWidth("OK")/2, gc.getHeight()/2+ClientMain.font.getHeight()*2);
			//cancel button
			g.setColor(isCancelButtonHovered() ? Menu.SELECTED_COLOR : Menu.TEXT_COLOR);
			g.drawString("Cancel", gc.getWidth()/2 - ClientMain.font.getWidth("Cancel")/2, gc.getHeight()/2+ClientMain.font.getHeight()*3);
		}else {
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
	}
	
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(enterName) {
			nicknameTextField.setFocus(true);
			//enforce text limit
			if(nicknameTextField.getText().length() > 20)
				nicknameTextField.setText(nicknameTextField.getText().substring(0, 20));
			if(isOkButtonHovered() && mousePressed) {
				try {
					GameSocket.serverIP = InetAddress.getByName(Settings.ip.get(selectedServer));
					PlayMenu.clientData.nickname = nicknameTextField.getText();
					ClientMain.menu = new PlayMenu();
					ClientMain.menu.init(gc);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			if(isCancelButtonHovered() && mousePressed) {
				enterIP = false;
				editing = false;
				enterName = false;
				nameTextField.setText("");
				ipTextField.setText("");
				selectedServer = -1;
			}
		}else if(enterIP) {
			if(input.isKeyPressed(Input.KEY_TAB)) {
				if(nameTextField.hasFocus()) {
					nameTextField.setFocus(false);
					ipTextField.setFocus(true);
				}else {
					nameTextField.setFocus(true);
					ipTextField.setFocus(false);
				}
			}
			if((isOkButtonHovered() && mousePressed) || input.isKeyPressed(Input.KEY_ENTER)) {
				if(nameTextField.getText().length() > 0 && ipTextField.getText().length() > 0) {
					if(editing) {
						Settings.name.set(selectedServer, nameTextField.getText());
						Settings.ip.set(selectedServer, ipTextField.getText());
					}else {
						Settings.name.add(nameTextField.getText());
						Settings.ip.add(ipTextField.getText());
					}
				}
				enterIP = false;
				editing = false;
				nameTextField.setText("");
				ipTextField.setText("");
				selectedServer = -1;
			}
			if(isCancelButtonHovered() && mousePressed) {
				enterIP = false;
				editing = false;
				nameTextField.setText("");
				ipTextField.setText("");
				selectedServer = -1;
			}
		}else {
			if(mousePressed) {
				if(selectedServer == -1) {
					if(isAddButtonHovered()) {
						enterIP = true;
						nameTextField.setFocus(false);
						ipTextField.setFocus(false);
					}
				}else {
					if(isEditButtonHovered()) {
						enterIP = true;
						editing = true;
						nameTextField.setFocus(false);
						ipTextField.setFocus(false);
						nameTextField.setText(Settings.name.get(selectedServer));
						ipTextField.setText(Settings.ip.get(selectedServer));
						nameTextField.setCursorPos(nameTextField.getText().length());
						ipTextField.setCursorPos(ipTextField.getText().length());
						//return so that selectedServer doesn't become -1 so I know what to edit
						return;
					}
				}
				if(isBackButtonHovered()) {
					ClientMain.menu = ClientMain.mainMenu;
				}
				if(selectedServer >= 0) {
					if(isRemoveButtonHovered()) {
						Settings.name.remove(selectedServer);
						Settings.ip.remove(selectedServer);
						selectedServer = -1;
					}
					if(isPlayButtonHovered()) {
						enterName = true;
						nicknameTextField.setText("");
						//return so that selectedServer doesn't become -1 so I know what to edit
						return;
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
	
	private boolean isOkButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("OK")/2;
		int by = ClientMain.HEIGHT/2+ClientMain.font.getHeight()*2;
		return mx > bx && mx < bx+ClientMain.font.getWidth("OK") && my > by && my < by+ClientMain.font.getHeight("OK");
	}
	private boolean isCancelButtonHovered() {
		int mx = Mouse.getX();
		int my = ClientMain.HEIGHT - Mouse.getY();
		int bx = ClientMain.WIDTH/2 - ClientMain.font.getWidth("Cancel")/2;
		int by = ClientMain.HEIGHT/2+ClientMain.font.getHeight()*3;
		return mx > bx && mx < bx+ClientMain.font.getWidth("Cancel") && my > by && my < by+ClientMain.font.getHeight("Cancel");
	}
	
}
