/* 	League class for BaseballSim
	by Ian Zapolsky 6/6/13 */

import java.util.ArrayList;
import java.util.Scanner;

public class League {

	int year;
	double BF, Hit, singles, doubles, triples, HR, BB, SO, HBP;
	double leagueOVASingle, leagueOVADouble, leagueOVATriple, leagueOVAHR, leagueOVABB, leagueOVASO, leagueOVAHBP;

	public League() { }

	public void setLeagueOVA() {
		leagueOVASingle = singles/BF;
		leagueOVADouble = doubles/BF;
		leagueOVATriple = triples/BF;
		leagueOVAHR = HR/BF;
		leagueOVABB = BB/BF;
		leagueOVASO = SO/BF;
		leagueOVAHBP = HBP/BF;
	}

	public void setYear(int init_year) {
		year = init_year;
	}

	public String toString() {
		return String.valueOf(year);
	}

}
