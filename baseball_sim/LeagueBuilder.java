/* 	LeagueBuilder class for BaseballSim
	by Ian Zapolsky 6/6/13 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class LeagueBuilder {

	Scanner in;

	public LeagueBuilder() { }

	public League buildLeague(int year) throws IOException {

		League l = new League();
		l.setYear(year);
		String url = "http://www.baseball-reference.com/leagues/MLB/"+year+".shtml";

		URL u = new URL(url);
		in = new Scanner(new InputStreamReader(u.openStream()));
		String nextLine, temp;

		in.findWithinHorizon("stat_total", 0);
		setStats(l);
		l.setLeagueOVA();
	
		return l;

	}	

	private void setStats(League l) {
        for (int i = 0; i < 6; i++)
            in.nextLine();
        l.BF = Double.valueOf(in.findInLine("(\\d){1,10}"));
        for (int i = 0; i < 3; i++)
            in.nextLine();
        l.Hit = Double.valueOf(in.findInLine("(\\d){1,10}"));
        in.nextLine();
        l.doubles = Double.valueOf(in.findInLine("(\\d){1,5}"));
        in.nextLine();
        l.triples = Double.valueOf(in.findInLine("(\\d){1,5}"));
        in.nextLine();
        l.HR = Double.valueOf(in.findInLine("(\\d){1,5}"));
        l.singles = l.Hit - (l.doubles+l.triples+l.HR);
        for (int i = 0; i < 4; i++)
            in.nextLine();
        l.BB = Double.valueOf(in.findInLine("(\\d){1,7}"));
        in.nextLine();
        l.SO = Double.valueOf(in.findInLine("(\\d){1,7}"));
        for (int i = 0; i < 8; i++)
            in.nextLine();
        l.HBP = Double.valueOf(in.findInLine("(\\d){1,7}"));

		/*
        System.out.println(l.BF);
        System.out.println(l.Hit);
        System.out.println(l.singles);
        System.out.println(l.doubles);
        System.out.println(l.triples);
        System.out.println(l.HR);
        System.out.println(l.BB);
        System.out.println(l.SO);
        System.out.println(l.HBP);
		*/
    }



}
