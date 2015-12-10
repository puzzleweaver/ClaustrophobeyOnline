package client;

import org.newdawn.slick.Color;

public class Colors {
	
	public static Color textColor = Color.green.darker(),
			selectedColor = Color.green,
			titleColor = Color.gray,
			typeColor = Color.gray,
			backgroundColor = Color.red,
			worldSpace = new Color(200, 50, 50),
			worldWall = worldSpace.darker(),
			worldBedrock = worldWall.darker();
	public static String scheme = "Classic";
	
	public static void setClassic() {
		textColor = Color.green.darker();
		selectedColor = Color.green;
		titleColor = Color.gray;
		typeColor = Color.gray;
		backgroundColor = Color.red;
		worldSpace = new Color(200, 0, 0);
		worldWall = worldSpace.darker();
		worldBedrock = worldWall.darker();
		scheme = "Classic";
	}
	public static void setDark() {
		textColor = Color.green.darker();
		selectedColor = Color.green;
		titleColor = Color.white;
		typeColor = Color.red;
		backgroundColor = Color.darkGray;
		worldSpace = new Color(130, 130, 130);
		worldWall = worldSpace.darker();
		worldBedrock = worldWall.darker();
		scheme = "Dark";
	}
	public static void setBlue() {
		textColor = Color.green.darker();
		selectedColor = Color.green;
		titleColor = Color.gray;
		typeColor = Color.gray;
		backgroundColor = Color.blue;
		worldSpace = new Color(0, 0, 200);
		worldWall = worldSpace.darker();
		worldBedrock = worldWall.darker();
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
