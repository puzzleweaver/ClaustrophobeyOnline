package client;

import org.newdawn.slick.Color;

public class Colors {
	
	public static Color textColor = Color.green.darker(),
			selectedColor = Color.green,
			titleColor = Color.gray,
			typeColor = Color.gray,
			backgroundColor = Color.red;
	public static String scheme = "Classic";
	
	public static void setClassic() {
		textColor = Color.green.darker();
		selectedColor = Color.green;
		titleColor = Color.gray;
		typeColor = Color.gray;
		backgroundColor = Color.red;
		scheme = "Classic";
	}
	public static void setDark() {
		textColor = Color.green.darker();
		selectedColor = Color.green;
		titleColor = Color.white;
		typeColor = Color.red;
		backgroundColor = Color.darkGray;
		scheme = "Dark";
	}
	public static void setBlue() {
		textColor = Color.green.darker();
		selectedColor = Color.green;
		titleColor = Color.gray;
		typeColor = Color.gray;
		backgroundColor = Color.blue;
		scheme = "Blue";
	}
	
	public static void setScheme(String scheme) {
		if(scheme.equals("Blue")) {
			setBlue();
		}else if(scheme.equals("Dark")) {
			setDark();
		}else {
			setClassic();
		}
	}
	
}
