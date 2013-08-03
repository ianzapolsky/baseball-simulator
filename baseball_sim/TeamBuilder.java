/* 	TeamBuilder class that harvests data from baseball-reference.com team pages for BaseballSim
	by Ian Zapolsky 6/9/14 */

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TeamBuilder {

	Scanner in;

	public TeamBuilder() { }

	public Team buildTeam(String url) throws IOException {
		
		Team t = new Team();
		
		URL u = new URL(url);
		in = new Scanner(new InputStreamReader(u.openStream()));
		String nextLine, temp;	

		/* 	year & name */
		String info = in.findWithinHorizon("<h1>.*</h1>", 0);
		t.setInfo(info);

		/* 	navigate to beginning of Team Batting and create entire team roster
			with batting stats. */
		in.findWithinHorizon("<tbody>", 0);
		in.nextLine();
		for(;;) {
			nextLine = in.nextLine();
			if(!(nextLine.equals("<tr class=\" thead\">")) && !(nextLine.equals("</tbody>"))) {
				Player p = new Player();
				setBattingStats(p);
				t.addPlayer(p);
				in.findWithinHorizon("</tr>", 0);
				in.nextLine();
			}
			else {
				if(nextLine.equals("<tr class=\" thead\">")) {
					in.findWithinHorizon("</tr>", 0);
					in.nextLine();
				}
				else
					break;
			}
		}

		/* 	navigate to beginning of Team Pitching and set pitching stats for pitchers */
		in.findWithinHorizon("<tbody>", 0);
		in.nextLine();
		for(;;) {
			nextLine = in.nextLine();
			if(!(nextLine.equals("<tr class=\" thead\">")) && !(nextLine.equals("</tbody>"))) {
				in.nextLine();
				in.nextLine();
				temp = findName();
				setPitchingStats(t.getPlayer(temp));
				in.findWithinHorizon("</tr>", 0);
				in.nextLine();
			}
			else {
				if(nextLine.equals("<tr class=\" thead\">")) {
					in.findWithinHorizon("</tr>", 0);
					in.nextLine();
				}
				else
					break;
			}
		}
		
		/*	navigate to beginning of Team Fielding and set fielding stats for players */
		in.findWithinHorizon("<tbody>", 0);
		in.nextLine();
		for(;;) {
			nextLine = in.nextLine();
			if(!(nextLine.equals("<tr class=\" thead\">")) && !(nextLine.equals("</tbody>"))) {
				temp = findName();
				setFieldingStats(t.getPlayer(temp));
				in.findWithinHorizon("</tr>", 0);
				in.nextLine();
			}
			else {
				if(nextLine.equals("<tr class=\" thead\">")) {
					in.findWithinHorizon("</tr>", 0);
					in.nextLine();
				}
				else
					break;
			}
		}
		in.close();
		t.setAverages();
		return t;
	}

	public void setBattingStats(Player p) {
		in.nextLine();
        String pos = in.findInLine("[0-9]{0,1}[A-Z]{1,2}");
		if (pos != null)
			p.position = pos;
		else
			p.position = "UT";
        in.nextLine();
		p.name = findName();

        if (in.findInLine("(#)") != null)
            p.LR = "B";
        else if (in.findInLine("\\*") != null)
            p.LR = "L";
        else
            p.LR = "R";

        for (int i = 0; i < 2; i++)
            in.nextLine();
		p.G = Double.valueOf(in.findInLine("(\\d){1,3}"));
		in.nextLine();
        p.PA = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.AB = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
		p.Run = Double.valueOf(in.findInLine("(\\d){1,3}")); 
        in.nextLine();
        p.Hit = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.doubles = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.triples = Double.valueOf(in.findInLine("(\\d){1,2}"));
        in.nextLine();
        p.HR = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
		p.RBI = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.SB = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.CS = Double.valueOf(in.findInLine("(\\d){1,2}"));
        in.nextLine();
        p.BB = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.SO = Double.valueOf(in.findInLine("(\\d){1,3}"));
        for (int i = 0; i < 7; i++)
            in.nextLine();
		p.GDP = Double.valueOf(in.findInLine("(\\d){1,3}"));
		in.nextLine();
        p.HBP = Double.valueOf(in.findInLine("(\\d){1,2}"));
		p.singles = p.Hit - ((p.doubles + p.triples + p.HR));

		/*
        System.out.println(p.name+", "+p.position+" ("+p.LR+")");
        System.out.println(p.PA);
        System.out.println(p.AB);
        System.out.println(p.Hit);
        System.out.println(p.doubles);
        System.out.println(p.triples);
        System.out.println(p.HR);
        System.out.println(p.SB);
        System.out.println(p.CS);
        System.out.println(p.BB);
        System.out.println(p.SO);
        System.out.println(p.HBP);
		*/
	}

	public void setPitchingStats(Player p) {
		in.nextLine();
		in.nextLine();
       	p.W = Double.valueOf(in.findInLine("(\\d){1,2}"));
        in.nextLine();
        p.L = Double.valueOf(in.findInLine("(\\d){1,2}"));
        in.nextLine();
        in.nextLine();
        in.nextLine();
        p.pG = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.GS = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        in.nextLine();
        in.nextLine();
        in.nextLine();
        p.SAV = Double.valueOf(in.findInLine("(\\d){1,2}"));
        in.nextLine();
        p.IP = Double.valueOf(in.findInLine("(\\d){1,3}\\.(\\d){1}"));
        in.nextLine();
        p.pHit = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        in.nextLine();
        in.nextLine();
        p.pHR = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.pBB = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        in.nextLine();
        p.pSO = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        p.pHBP = Double.valueOf(in.findInLine("(\\d){1,3}"));
        in.nextLine();
        in.nextLine();
        in.nextLine();
        p.BF = Double.valueOf(in.findInLine("(\\d){1,4}"));
	
		/*
        System.out.println(p.name);
        System.out.println(p.W);
        System.out.println(p.L);
        System.out.println(p.G);
        System.out.println(p.GS);
        System.out.println(p.SAV);
        System.out.println(p.IP);
        System.out.println(p.pHit);
        System.out.println(p.pBB);
        System.out.println(p.pHR);
        System.out.println(p.pSO);
        System.out.println(p.BF);
        System.out.println(p.pHBP);
		*/
	}

	public void setFieldingStats(Player p) {
		String value;
		for (int i = 0; i < 7; i++)
			in.nextLine();

		if ((value = in.findInLine("(\\d){1,4}")) == null)
			p.PO = 0;
		else
			p.PO = Double.valueOf(value);
		in.nextLine();
		if ((value = in.findInLine("(\\d){1,4}")) == null)
			p.ASS = 0;
		else
			p.ASS = Double.valueOf(value);
        in.nextLine();
		if ((value = in.findInLine("(\\d){1,4}")) == null)
			p.ERR = 0;
		else
			p.ERR = Double.valueOf(value);

		/*
        System.out.println(p.name);
        System.out.println(p.PO);
        System.out.println(p.ASS);
        System.out.println(p.ERR);
		*/
    }
	
	private String findName() {
		String name;
		if ((name = in.findInLine("[A-Z](\\w)*(\\s)[A-Z](\\w)*")) != null)
			return name;
		else
			return in.findInLine("([A-Z][.]){2}(\\s)(\\w)*");
	}

}












