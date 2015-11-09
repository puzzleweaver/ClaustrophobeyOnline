package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class GameSocket extends Thread {
	
	protected DatagramSocket socket;
	
	public static InetAddress serverIP;
	
	public static final int PORT = 7890;
	
	public GameSocket() {
		try {
//			serverIP = InetAddress.getByName("71.46.93.12"); //riley public
//			serverIP = InetAddress.getByName("192.168.1.115"); // riley
//			serverIP = InetAddress.getByName("192.168.1.109"); //austin
			serverIP = InetAddress.getByName("localhost"); // for consistency's sake
		} catch (Exception e) {}
	}	
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			byte[] data = new byte[65536];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
				readData(packet);
			} catch (Exception e) {}
		}
	}
	
	public abstract void readData(DatagramPacket packet);
	
}
