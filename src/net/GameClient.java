package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import client.menu.PlayMenu;

public class GameClient extends GameSocket {
	
	public boolean loaded = false;
	
	public GameClient() {
		super();
		try {
			this.socket = new DatagramSocket();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readData(DatagramPacket packet) {
		try {
			PlayMenu.data = (OutputData) Serializer.deserialize(packet.getData());
			loaded = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
