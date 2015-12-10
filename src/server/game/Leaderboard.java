package server.game;

import java.util.ArrayList;
import java.util.Collections;

import server.ServerMain;

public class Leaderboard {

	public ArrayList<LBItem> items = new ArrayList<>();
	
	public void sort() {
		Collections.sort(items);
	}
	
	public void add(int index) {
		items.add(new LBItem(index));
	}
	
	public String[] getTopTen() {
		
		String[] names = new String[10];
		for(int i = 0; i < 10; i++) {
			if(i >= items.size())
				names[i] = "?";
			else
				names[i] = ServerMain.data.indieData.get(items.get(i).id-1).clientData.nickname + ": " + ServerMain.data.terr.get(items.get(i).id);
		}
		return names;
	
	}
	
	private class LBItem implements Comparable<LBItem> {

		int id;
		
		public LBItem(int id) {
			this.id = id;
		}
		
		public int compareTo(LBItem a) {
			int k = -ServerMain.data.terr.get(id)+ServerMain.data.terr.get(a.id);
			return (k == 0) ? 1:k;
		}

	}
	
}
