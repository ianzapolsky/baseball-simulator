/*	Team class for BaseballSim
	by Ian Zapolsky 6/4/13 */

import java.util.ArrayList;

public class Team {

	String name;
	int year;
	ArrayList<Player> roster;
	ArrayList<Player> bullpen;
		
	public Team() {
		roster = new ArrayList<Player>();
		bullpen = new ArrayList<Player>();
	}

	public void setInfo(String info) {
		year = Integer.valueOf(info.substring(4,8));
		name = info.substring(9,(info.length()-5));
	}

	public void addPlayer(Player input) {	
		roster.add(input);
		if (input.position.equals("P"))
			bullpen.add(input);
	}

	public void zeroOut() {
		for (Player p : roster)
			p.clearGameStats();
	}

	public Player getPlayer(String name) {
		Player target = roster.get(0);
		for (Player p : roster) {
			if (p.name.equals(name))
				target = p;
		}
		return target;
	}

	public void setAverages() {
		for (Player p : roster)
			p.setAvg();
	}

	public String printRoster() {
		String result = ""+year+" "+name+"\n";
		result += "ROSTER:\n";
		for (Player i : roster)
			result += i+" ("+i.position+")\n";
		return result;
	}

	public String toString() {
		return ""+name+" ("+year+")";
	}
}
