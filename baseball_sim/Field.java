/* 	Field class that simulates the events of a baseball field for BaseballSim
	by Ian Zapolsky 6/13/12 */

import java.util.Random;
import java.io.*;

public class Field {

	Player[] field, basepaths;
	int outs;
	final int WALK = 0;
	final int SINGLE = 1;
	final int DOUBLE = 2;
	final int TRIPLE = 3;
	final int HOMERUN = 4;
	final int HBP = 5;
	final int STRIKEOUT = 6;
	final int OUT = 7;
	PrintWriter w;
	
	public Field(PrintWriter init_w) {
		w = init_w;
		basepaths = new Player[4];
		outs = 0;
	}

	public void resetField(Player[] init_field) {
		field = init_field;
		clearBasepaths();
		outs = 0;
	}
		
	private void clearBasepaths() {
		for (int i = 0; i < 4; i++)
			basepaths[i] = null;
	}

	public int updateField(int result, Player player) {
		if (result == WALK) {
			printLog(player+" draws a walk.\n");
			calcPitchCount();
			return updateWalk(player);
		}
		else if (result == HBP) {
			printLog(player+" is hit by pitch.\n");
			calcPitchCount();
			return updateHBP(player);
		}
		else if (result == STRIKEOUT) {
			printLog(player+" strikes out.\n");
			calcPitchCount();
			return updateStrikeout(player);	
		}
		else if (result == HOMERUN) {
			printLog(player+" hits a home run.\n");
			calcPitchCount();
			return updateHomerun(player);
		}
		else if (result == TRIPLE) {
			printLog(player+" hits a triple.\n");
			calcPitchCount();
			return updateTriple(player);
		}
		else if (result == DOUBLE) {
			printLog(player+" hits a double.\n");
			calcPitchCount();
			return updateDouble(player);
		}
		else if (result == SINGLE) {
			printLog(player+" hits a single.\n");
			calcPitchCount();
			return updateSingle(player);
		}
		else {
			printLog(player+" hits a fieldable ball to ");
			calcPitchCount();
			return updateOut(player);
		}
	}

	private int updateOut(Player player) {
		int runs = 0;
		field[0].to++;
		boolean sac = false;
		Random rand = new Random();
		int hitDestination = rand.nextInt(9);
		double error = rand.nextDouble();		
		printLog((hitDestination+1)+".\n");
		if (error > field[hitDestination].fldPCT) {
			printLog(field[hitDestination]+" commits a fielding error!\n");
			field[hitDestination].e++;
			if (manOnThird())
				runs += advanceFrom(3,1);
			if (manOnSecond())
				runs += advanceFrom(2,1);
			if (manOnFirst())
				runs += advanceFrom(1,1);
			placeRunner(1,player);	
		}
		else if (outs < 2 && hitDestination > 5 && (manOnThird() || manOnSecond())) {
			if (manOnThird()) {
				printLog("The hit is a sac fly.\n");
				runs += advanceFrom(3,1);
				player.rbi++;
				sac = true;
			}
			if (manOnSecond() && hitDestination == 8) {
				printLog("The hit is a sac fly.\n");
				runs += advanceFrom(2,1);
				sac = true;
			}
			outs++;
			field[0].to++;
		}
		/*	TODO: Implement a better double play model */
		else if (outs < 2 && hitDestination > 1 && hitDestination < 6 && manOnFirst()) {
			boolean scored = false;
			printLog("The infield turns a double play!\n");
			if (outs + 2 == 3) {
				outs += 2;
				field[0].to += 2;
			}
			else {
				if (manOnThird()) {
					runs += advanceFrom(3,1);
					scored = true;
				}
				if (manOnSecond()) 
					runs += advanceFrom(2,1);
				clearBase(2);
				clearBase(1);
				field[0].to += 2;
				outs += 2;
			}
		}
		else
			outs++;
		if (sac == true)
			/* do nothing */;
		else
			player.ab++;
		return runs;
	}		

	private int updateSingle(Player player) {
		int runs = 0;
		field[0].h++;
		if (manOnThird()) {
			runs += advanceFrom(3,1);
			player.rbi++;
			field[0].er++;
		}
		if (manOnSecond()) {
			if (player.isFast()) {
				runs += advanceFrom(2,2);
				player.rbi++;
				field[0].er++;
			}
			else
				runs += advanceFrom(2,1);
		}
		if (manOnFirst()) {
			if (!manOnThird() && player.isFast())
				runs += advanceFrom(1,2);
			else
				runs += advanceFrom(1,1);
		}
		placeRunner(1,player);
		player.ab++;
		player.hit++;
		return runs;
	}

	private int updateDouble(Player player) {
		int runs = 0;
		field[0].h++;
		if (manOnThird()) {
			runs += advanceFrom(3,2);
			player.rbi++;
			field[0].er++;
		}
		if (manOnSecond()) {
			runs += advanceFrom(2,2);
			player.rbi++;
			field[0].er++;
		}
		if (manOnFirst()) {
			if (player.isFast()) {
				runs += advanceFrom(1,3);
				player.rbi++;
				field[0].er++;
			}
			else
				runs += advanceFrom(1,2);
		}
		placeRunner(2,player);
		player.ab++;
		player.hit++;
		return runs;
	}
		
	private int updateTriple(Player player) {
		int runs = 0;
		field[0].h++;
		if (manOnThird()) {
			runs += advanceFrom(3,3);
			player.rbi++;
			field[0].er++;
		}
		if (manOnSecond()) {
			runs += advanceFrom(2,3);
			player.rbi++;
			field[0].er++;
		}
		if (manOnFirst()) {
			runs += advanceFrom(1,3);
			player.rbi++;
			field[0].er++;
		}
		placeRunner(3,player);
		player.ab++;
		player.hit++;
		return runs;
	}


	private int updateHomerun(Player player) {
		int runs = 0;
		field[0].h++;
		field[0].er++;
		if (manOnThird()) {
			runs += advanceFrom(3,4);
			player.rbi++;
			field[0].er++;
		}
		if (manOnSecond()) {
			runs += advanceFrom(2,4);
			player.rbi++;
			field[0].er++;
		}
		if (manOnFirst()) {
			runs += advanceFrom(1,4);
			player.rbi++;
			field[0].er++;
		}
		player.rbi++;
		player.ab++;
		player.r++;
		printLog("\t"+player+" scores.\n");
		player.hit++;
		runs++;
		return runs;
	}

	private int updateStrikeout(Player player) {
		int runs = 0;
		field[0].so++;
		field[0].to++;
		Random rand = new Random();
		double error = rand.nextDouble();
		if (error > field[1].fldPCT) {
			// catcher makes an error: passed ball
			field[1].e++;
			printLog(field[1]+" drops the third strike!\n");
			if (manOnThird())
				runs += advanceFrom(3,1);
			if (manOnSecond())
				runs += advanceFrom(2,1);
			if (manOnFirst())
				runs += advanceFrom(1,1);
			placeRunner(1,player);	
		}
		else 
			outs++;
		player.ab++;
		return runs;
	}
		
	private int updateWalk(Player player) {
		int runs = 0;
		field[0].bb++;
		if (manOnThird())
			if (manOnSecond() && manOnFirst()) {
				runs += advanceFrom(3,1);
				player.rbi++;
				field[0].er++;
			}
		if (manOnSecond()) {
			if (manOnFirst()) {
				runs += advanceFrom(2,1);
			}
		}
		if (manOnFirst()) {
			runs += advanceFrom(1,1);
		}
		placeRunner(1,player);
		return runs;
	}

	private int updateHBP(Player player) {
		int runs = 0;
		field[0].hbp++;
		if (manOnThird())
			if (manOnSecond() && manOnFirst()) {
				runs += advanceFrom(3,1);
				player.rbi++;
				field[0].er++;
			}
		if (manOnSecond())
			if (manOnFirst()) {
				runs += advanceFrom(2,1);
			}
		if (manOnFirst()) {
			runs += advanceFrom(1,1);
		}
		placeRunner(1,player);
		return runs;
	}

	private int advanceFrom(int base, int bases) {
		int runs = 0;
		if ((base+bases) > 3) {
			basepaths[0] = basepaths[base];
			basepaths[base] = null;
			runs++;
			basepaths[0].r++;
			printLog("\t"+basepaths[0]+" scores.\n");
		}
		else {
			basepaths[base+bases] = basepaths[base];
			basepaths[base] = null;
			printLog("\t"+basepaths[base+bases]+" advances to "+(base+bases)+"B.\n");
		}
		return runs;
	}

	private void calcPitchCount() {
		field[0].pitchCount = ((4.81*field[0].so) + (5.14*field[0].bb) + (3.27*(field[0].h+field[0].hbp)) + (3.16*(field[0].to - field[0].so)));
	}	


	private void printLog(String log) {
		//System.out.print(log);
		w.print(log);
	}

	private String stateOfBasepaths() {
		String state = "";
		if (!manOnFirst() && !manOnSecond() && !manOnThird())
			state = "Nobody on base.\n";
		else if (manOnFirst() && manOnSecond() && manOnThird())
			state = "Man on 1B, 2B, and 3B\n";
		else if (manOnFirst() && !manOnSecond() && manOnThird())
			state = "Man on 1B and 3B.\n";
		else if (manOnFirst() && manOnSecond() && !manOnThird())
			state = "Man on 1B and 2B.\n";	
		else if (manOnFirst() && !manOnSecond() && !manOnThird())
			state = "Man on 1B.\n";
		else if (!manOnFirst() && manOnSecond() && !manOnThird())
			state = "Man on 2B.\n";
		else if (!manOnFirst() && !manOnSecond() && manOnThird())
			state = "Man on 3B.\n";
		else if (!manOnFirst() && manOnSecond() && manOnThird())
			state = "Man on 2B and 3B.\n";
		return state;
	}
	
	private void clearBase(int base) {
		basepaths[base] = null;
	}

	private void placeRunner(int base, Player player) {
		basepaths[base] = player;
		printLog("\t"+player+" reaches "+base+"B.\n");
	}

	private boolean manOnFirst() {
		if (basepaths[1] != null)
			return true;
		else
			return false;
	}
	private boolean manOnSecond() {
		if (basepaths[2] != null)
			return true;
		else
			return false;
	}
	private boolean manOnThird() {
		if (basepaths[3] != null)
			return true;
		else
			return false;
	}
	
	public int getOuts() { 
		return outs; 
	}
		 		
}		

	

