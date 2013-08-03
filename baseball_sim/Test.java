import java.io.*;

public class Test {
	
	public static void main(String[] args) {
		try {	
		GUI g = new GUI();

		/*

		TeamBuilder n = new TeamBuilder();
		Team h = n.buildTeam("http://www.baseball-reference.com/teams/SEA/1995.shtml");
		Team a = n.buildTeam("http://www.baseball-reference.com/teams/NYY/1995.shtml");

		LeagueBuilder m = new LeagueBuilder();
		League l = m.buildLeague(h.year);
		
		Game g = new Game();
		g.playGame(h, 1, a, 1, l);

		/*
		double totalRuns = 0;
		Game g = new Game();
		for (int i = 0; i < 100; i++) {
			g.playGame(h, 1, a, 3, l);
			totalRuns += (g.aRuns + g.hRuns);
		}

		double avgRuns = Double.valueOf(totalRuns)/100;
		System.out.println(avgRuns);
		*/

		}
		catch (IOException e) {
			System.out.println("FUCK");
		}
	}


}
		
