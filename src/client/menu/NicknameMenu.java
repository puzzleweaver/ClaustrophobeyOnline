package client.menu;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.GameSocket;

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

public class NicknameMenu implements Menu {
	
	public TextField nicknameTextField;
	
	public int selectedServer = -1;
	
	private Button okButton, cancelButton;
	
	public void init(GameContainer gc) {
		okButton = new Button(gc.getWidth()/2, gc.getHeight()/2 + ClientMain.font.getHeight()*2, "OK", ClientMain.font);
		cancelButton = new Button(gc.getWidth()/2, gc.getHeight()/2 + ClientMain.font.getHeight()*3, "Cancel", ClientMain.font);
		nicknameTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/4, gc.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		g.setFont(ClientMain.font);
		g.setColor(Colors.titleColor);
		g.drawString("Nickname:", gc.getWidth()/2 - ClientMain.font.getWidth("Nickname:")/2, gc.getHeight()/2-ClientMain.font.getHeight());
		g.setColor(Colors.typeColor);
		nicknameTextField.setBorderColor(Color.black);
		nicknameTextField.render(gc, g);
		//buttons
		okButton.render(gc, g);
		cancelButton.render(gc, g);
	}
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		nicknameTextField.setFocus(true);
		//enforce text limit
		if(nicknameTextField.getText().length() > 20)
			nicknameTextField.setText(nicknameTextField.getText().substring(0, 20));
		if((okButton.isHovered(gc) && mousePressed) || input.isKeyPressed(Input.KEY_ENTER)) {
			try {
				GameSocket.serverIP = InetAddress.getByName(Settings.ip.get(selectedServer));
				PlayMenu.clientData.nickname = nicknameTextField.getText();
				ClientMain.menu = ClientMain.playMenu;
				ClientMain.menu.init(gc);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		if(cancelButton.isHovered(gc) && mousePressed) {
			ClientMain.menu = ClientMain.serverManagerMenu;
		}
	}
	
}
