package server.menu;

import net.ServerData;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import server.Menu;
import server.ServerMain;
import world.World;
import client.Button;
import client.Colors;
import client.MenuBackground;

public class ServerSetupMenu implements Menu {
	
	private Button suddenDeathButton, territorialButton, ffaButton, twoButton, threeButton, roundButton, networkButton, startButton;
	private Button[][] buffButtons = new Button[2][7];
	private String[] buffNames = {"Attack", "Defense", "Drones", "Ghost", "Max Up", "Food Up", "Attack Speed"};
	
	public void init(GameContainer gc) {
		suddenDeathButton = new Button(gc.getWidth()/4, gc.getHeight()/8+ServerMain.font.getHeight(), "Sudden Death", ServerMain.fontSmall);
		territorialButton = new Button(3*gc.getWidth()/4, gc.getHeight()/8+ServerMain.font.getHeight(), "Territorial", ServerMain.fontSmall);
		ffaButton = new Button(gc.getWidth()/4, gc.getHeight()/4+ServerMain.font.getHeight(), "FFA", ServerMain.fontSmall);
		twoButton = new Button(gc.getWidth()/2, gc.getHeight()/4+ServerMain.font.getHeight(), "2", ServerMain.fontSmall);
		threeButton = new Button(3*gc.getWidth()/4, gc.getHeight()/4+ServerMain.font.getHeight(), "3", ServerMain.fontSmall);

		roundButton = new Button(gc.getWidth()/4, 3*gc.getHeight()/4+ServerMain.font.getHeight(), "Round", ServerMain.fontSmall);
		networkButton = new Button(3*gc.getWidth()/4, 3*gc.getHeight()/4+ServerMain.font.getHeight(), "Network", ServerMain.fontSmall);

		for(int i = 0; i < buffButtons[0].length; i++) {
			buffButtons[0][i] = new Button(gc.getWidth()/2, 3*gc.getHeight()/8+ServerMain.fontSmall.getHeight()*i, "Disable", ServerMain.fontSmall);
			buffButtons[1][i] = new Button(13*gc.getWidth()/16, 3*gc.getHeight()/8+ServerMain.fontSmall.getHeight()*i, "Enable", ServerMain.fontSmall);
			buffButtons[1][i].selected = true; //disabled by default
		}

		startButton = new Button(gc.getWidth()/2, 7*gc.getHeight()/8, "Start", ServerMain.font);
		startButton.enabled = false;
	}
	public void render(GameContainer gc, Graphics g) {
		MenuBackground.render(gc, g);
		//game mode
		g.setColor(Colors.titleColor);
		g.setFont(ServerMain.font);
		g.drawString("Game Mode", gc.getWidth()/2 - ServerMain.font.getWidth("Game mode")/2, gc.getHeight()/8);
		suddenDeathButton.render(gc, g);
		territorialButton.render(gc, g);
		//teams
		g.setColor(Colors.titleColor);
		g.setFont(ServerMain.font);
		g.drawString("Teams", gc.getWidth()/2 - ServerMain.font.getWidth("Teams")/2, gc.getHeight()/4);
		ffaButton.render(gc, g);
		twoButton.render(gc, g);
		threeButton.render(gc, g);
		//buffs
		g.setColor(Colors.titleColor);
		g.setFont(ServerMain.fontSmall);
		for(int i = 0; i < buffNames.length; i++) {
			g.drawString(buffNames[i], 3*gc.getWidth()/16 - ServerMain.fontSmall.getWidth(buffNames[i])/2, 3*gc.getHeight()/8 + ServerMain.fontSmall.getHeight()*i);
		}
		for(int i = 0; i < buffButtons.length; i++) {
			for(int j = 0; j < buffButtons[0].length; j++) {
				buffButtons[i][j].render(gc, g);
			}
		}
		//world type
		g.setColor(Colors.titleColor);
		g.setFont(ServerMain.font);
		g.drawString("World", gc.getWidth()/2 - ServerMain.font.getWidth("World")/2, 3*gc.getHeight()/4);
		roundButton.render(gc, g);
		networkButton.render(gc, g);
		//start button
		startButton.render(gc, g);
	}
	public void update(GameContainer gc) {
		Input input = gc.getInput();
		boolean mousePressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		if((suddenDeathButton.selected || territorialButton.selected) &&
				(ffaButton.selected || twoButton.selected || threeButton.selected) &&
				(roundButton.selected || networkButton.selected)) {
			startButton.enabled = true;
		}
		if(mousePressed) {
			//if one mode is clicked, select it and deselect the others
			if(suddenDeathButton.isHovered(gc)) {
				suddenDeathButton.selected = true;
				territorialButton.selected = false;
			}if(territorialButton.isHovered(gc)) {
				suddenDeathButton.selected = false;
				territorialButton.selected = true;
			}if(ffaButton.isHovered(gc)) {
				ffaButton.selected = true;
				twoButton.selected = false;
				threeButton.selected = false;
			}if(twoButton.isHovered(gc)) {
				ffaButton.selected = false;
				twoButton.selected = true;
				threeButton.selected = false;
			}if(threeButton.isHovered(gc)) {
				ffaButton.selected = false;
				twoButton.selected = false;
				threeButton.selected = true;
			}if(roundButton.isHovered(gc)) {
				roundButton.selected = true;
				networkButton.selected = false;
			}if(networkButton.isHovered(gc)) {
				roundButton.selected = false;
				networkButton.selected = true;
			}
			//check buff buttons for selection
			for(int i = 0; i < buffButtons[0].length; i++) {
				if(buffButtons[0][i].isHovered(gc)) {
					buffButtons[0][i].selected = true;
					buffButtons[1][i].selected = false;
				}if(buffButtons[1][i].isHovered(gc)) {
					buffButtons[0][i].selected = false;
					buffButtons[1][i].selected = true;
				}
			}
			if(startButton.isHovered(gc) && startButton.enabled) {
				ServerMain.menu = ServerMain.serverPlayMenu;
				ServerMain.data = new ServerData();
				ServerMain.data.gameType = suddenDeathButton.selected ? ServerData.MODE_SD : ServerData.MODE_TERR;
				ServerMain.data.numTeams = ffaButton.selected ? 0 : (twoButton.selected ? 2 : 3);
				for(int i = 0; i < ServerMain.data.buffs.length; i++) {
					ServerMain.data.buffs[i] = buffButtons[1][i].selected;
				}
				ServerMain.data.worldType = roundButton.selected ? World.TYPE_CIRCLE : World.TYPE_NETWORK;
				ServerMain.data.init();
				ServerMain.server = new ServerMain();
				ServerMain.server.start();
			}
		}
	}
	
}
