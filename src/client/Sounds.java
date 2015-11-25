package client;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Sounds {
	
	public static Music music;
	
	public static void load() {
		try {
			music = new Music("res/music.ogg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
}
