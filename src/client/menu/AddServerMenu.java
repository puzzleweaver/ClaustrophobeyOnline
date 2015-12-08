package client.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.TextField;

import server.Menu;
import client.Button;
import client.ClientMain;
import client.Colors;
import client.MenuBackground;
import client.Settings;

public class AddServerMenu implements Menu {
	
	public TextField nameTextField, ipTextField;
	
	private Button okButton, cancelButton;
	
	public boolean editing = false;
	public int selectedServer = -1;
	
	public void init(GameContainer gc) {
		okButton = new Button(gc.getWidth()/2, gc.getHeight()/2 + ClientMain.font.getHeight()*2, "OK", ClientMain.font);
		cancelButton = new Button(gc.getWidth()/2, gc.getHeight()/2 + ClientMain.font.getHeight()*3, "Cancel", ClientMain.font);
		nameTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/2 + ClientMain.font.getWidth("Name: ")/2 - gc.getWidth()/4, gc.getHeight()/2-ClientMain.fontSmall.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
		ipTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/2 + ClientMain.font.getWidth("Name: ")/2 - gc.getWidth()/4, gc.getHeight()/2+ClientMain.fontSmall.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
	}
	
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//labels for name and ip
		g.setFont(ClientMain.font);
		g.setColor(Colors.titleColor);
		g.drawString("Name: ", gc.getWidth()/2 - ClientMain.font.getWidth("Name: ")/2 - nameTextField.getWidth()/2, gc.getHeight()/2-ClientMain.fontSmall.getHeight()/2);
		g.drawString("IP: ", gc.getWidth()/2 - ClientMain.font.getWidth("IP: ")/2 - ipTextField.getWidth()/2, gc.getHeight()/2+ClientMain.fontSmall.getHeight()/2);
		//text fields
		g.setColor(Colors.typeColor);
		nameTextField.setBorderColor(Color.black);
		nameTextField.render(gc, g);
		ipTextField.setBorderColor(Color.black);
		ipTextField.render(gc, g);
		//buttons
		okButton.render(gc,g);
		cancelButton.render(gc, g);
	}
	
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		if(nameTextField.hasFocus())
			nameTextField.setCursorPos(nameTextField.getText().length());
		else if(ipTextField.hasFocus())
			ipTextField.setCursorPos(ipTextField.getText().length());
		else {
			nameTextField.setFocus(true);
			nameTextField.setCursorPos(nameTextField.getText().length());
		}
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if(input.isKeyPressed(Input.KEY_TAB)) {
			if(nameTextField.hasFocus()) {
				nameTextField.setFocus(false);
				ipTextField.setFocus(true);
			}else {
				nameTextField.setFocus(true);
				ipTextField.setFocus(false);
			}
		}
		if((okButton.isHovered(gc) && mousePressed) || input.isKeyPressed(Input.KEY_ENTER)) {
			if(nameTextField.getText().length() > 0 && ipTextField.getText().length() > 0) {
				if(editing) {
					Settings.name.set(selectedServer, nameTextField.getText());
					Settings.ip.set(selectedServer, ipTextField.getText());
				}else {
					Settings.name.add(nameTextField.getText());
					Settings.ip.add(ipTextField.getText());
				}
			}
			ClientMain.menu = ClientMain.serverManagerMenu;
			nameTextField.setText("");
			ipTextField.setText("");
			selectedServer = -1;
		}
		if(cancelButton.isHovered(gc) && mousePressed) {
			ClientMain.menu = ClientMain.serverManagerMenu;
			nameTextField.setText("");
			ipTextField.setText("");
			selectedServer = -1;
		}
	}
	
}
