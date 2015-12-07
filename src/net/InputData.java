package net;

import java.io.Serializable;

public class InputData implements Serializable {
	
	private static final long serialVersionUID = -2642169204067399606L;

	public int dx, dy, w, h, pixW;
	public boolean attack, defend, slothShortcut, greedShortcut, exited;
	public boolean[] buffs;
	
	public String nickname;
	
}
