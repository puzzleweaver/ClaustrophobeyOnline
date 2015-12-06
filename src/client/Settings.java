package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Settings {
	
	public static ArrayList<String> name = new ArrayList<>(), ip = new ArrayList<>();
	public static float musicVolume = 0.5f;
	
	public static void read() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("settings.txt")));
			String line;
			Colors.setScheme(reader.readLine());
			musicVolume = Float.parseFloat(reader.readLine());
			int num = 0;
			while((line = reader.readLine()) != null) {
				if(num % 2 == 0) {
					name.add(line);
				}else {
					ip.add(line);
				}
				num++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void write() {
		try {
			PrintWriter writer = new PrintWriter(new File("settings.txt"));
			writer.println(Colors.scheme);
			writer.println(Sounds.music.getVolume());
			for(int i = 0; i < name.size(); i++) {
				writer.println(name.get(i));
				writer.println(ip.get(i));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
