package client.menus;

import java.net.InetAddress;
import java.net.UnknownHostException;

import main.Menu;
import net.GameSocket;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.TextField;

import client.ClientMain;
import client.MenuBackground;
import client.Settings;

public class NicknameMenu implements Menu {
	
	public TextField nicknameTextField;
	
	public int selectedServer = -1;
	
	public void init(GameContainer gc) {
		nicknameTextField = new TextField(gc, ClientMain.fontSmall, gc.getWidth()/4, gc.getHeight()/2, gc.getWidth()/2, ClientMain.fontSmall.getHeight());
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
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
	}
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		nicknameTextField.setFocus(true);
		//enforce text limit
		if(nicknameTextField.getText().length() > 20)
			nicknameTextField.setText(nicknameTextField.getText().substring(0, 20));
		if(isOkButtonHovered() && mousePressed) {
			try {
				GameSocket.serverIP = InetAddress.getByName(Settings.ip.get(selectedServer));
				PlayMenu.clientData.nickname = nicknameTextField.getText();
				ClientMain.menu = ClientMain.playMenu;
				ClientMain.menu.init(gc);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		if(isCancelButtonHovered() && mousePressed) {
			ClientMain.menu = ClientMain.serverManagerMenu;
		}
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
