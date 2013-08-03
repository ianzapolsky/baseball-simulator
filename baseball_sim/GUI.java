/*	GUI class to provide a user friendly face for BaseballSim
	by Ian Zapolsky 6/14/13 */

/* 	TODO: Investigate cool skins for Java swing and awt components */

import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frame;
	private JPanel northPanel, centerPanel, homePanel, awayPanel, eastPanel, westPanel, southPanel, logPanel, iterationsPanel;
	private JButton buildButton, simButton;
	private JTextField homeURLField, awayURLField, iterationsField;
	private JTextField[] homeNums, awayNums;
	private JTextArea homeRoster, awayRoster, gameLog;
	private JLabel homeLabel, awayLabel, logLabel, instructionTopLabel, instructionBottomLabel, iterationsLabel;
	private JScrollPane homeScrollPane, awayScrollPane, gameLogScrollPane;

	public int[] homeInts, awayInts;
	public Team home, away;
	public League league;
	public Game game;
	public ScoreKeeper sk;

	public GUI() throws IOException {
		frame = new JFrame("BaseballSim");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(170,170,170));
		frame.add(createNorthPanel(), BorderLayout.NORTH);
		frame.add(createCenterPanel(), BorderLayout.CENTER);
		frame.add(createSouthPanel(), BorderLayout.SOUTH);
		frame.add(createGameLogPanel(), BorderLayout.EAST);
		frame.pack();
		frame.setSize(1300, 800);
		frame.setVisible(true);
	}

	private JPanel createSouthPanel() {
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.setBorder(BorderFactory.createEtchedBorder());
		simButton = new JButton("SIM");

		simButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				try {
					simGame();
				}
				catch (IOException e1) {
					JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of your URLs.\nCheck to make sure that they are both of the format http://www.baseball-reference.com/teams/SEA/1995.shtml");
				}
			}
		});	

		southPanel.add(simButton);
		return southPanel;
	}

	private JPanel createGameLogPanel() {
		logPanel = new JPanel();
		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.PAGE_AXIS));
		logPanel.setBorder(BorderFactory.createEtchedBorder());
		logLabel = new JLabel("GAME LOG:");
		gameLog = new JTextArea(35,50);
		JScrollPane gameLogScrollPane = new JScrollPane(gameLog);
		
		logPanel.add(logLabel);
		logPanel.add(gameLogScrollPane);
		return logPanel;
	}

	private JPanel createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout());
		centerPanel.setBorder(BorderFactory.createEtchedBorder());
		centerPanel.add(createWestPanel());
		centerPanel.add(createHomePanel());
		centerPanel.add(createAwayPanel());
		centerPanel.add(createEastPanel());
		centerPanel.add(createIterationsPanel());
		return centerPanel;
	}
	
	private JPanel createIterationsPanel() {
		iterationsPanel = new JPanel();
		iterationsPanel.setLayout(new BoxLayout(iterationsPanel, BoxLayout.PAGE_AXIS));
		iterationsPanel.setBorder(BorderFactory.createEtchedBorder());
		iterationsField = new JTextField(10);
		iterationsLabel = new JLabel("# iterations");
		iterationsPanel.add(iterationsLabel);
		iterationsPanel.add(iterationsField);
		return iterationsPanel;
	}

	private JPanel createHomePanel() {
		homePanel = new JPanel();
		homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.PAGE_AXIS));
		homePanel.setBorder(BorderFactory.createEtchedBorder());
		homeLabel = new JLabel("HOME: ROSTER");
		homeRoster = new JTextArea(30, 15);
		JScrollPane homeScrollPane = new JScrollPane(homeRoster);
		
		homePanel.add(homeLabel);
		homePanel.add(homeScrollPane);
		return homePanel;
	}

	private JPanel createAwayPanel() {
		awayPanel = new JPanel();
		awayPanel.setLayout(new BoxLayout(awayPanel, BoxLayout.PAGE_AXIS));
		awayPanel.setBorder(BorderFactory.createEtchedBorder());
		awayLabel = new JLabel("AWAY: ROSTER");
		awayRoster = new JTextArea(30, 15);
		JScrollPane awayScrollPane = new JScrollPane(awayRoster);
		
		awayPanel.add(awayLabel);
		awayPanel.add(awayScrollPane);
		return awayPanel;
	}	

	private JPanel createEastPanel() {
		eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
		eastPanel.setBorder(BorderFactory.createEtchedBorder());

		awayNums = new JTextField[10];
		for (int i = 0; i < 10; i++) {
			awayNums[i] = new JTextField(2);
			eastPanel.add(awayNums[i]);
		}
		return eastPanel;
	}

	private JPanel createWestPanel() {
		westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
		westPanel.setBorder(BorderFactory.createEtchedBorder());

		homeNums = new JTextField[10];
		for (int i = 0; i < 10; i++) {
			homeNums[i] = new JTextField(2);
			westPanel.add(homeNums[i]);
		}
		return westPanel;
	}
		
	private JPanel createNorthPanel() {
		northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		northPanel.setBorder(BorderFactory.createEtchedBorder());

		homeURLField = new JTextField(40);
		homeURLField.setText("Home team URL (e.g. http://www.baseball-reference.com/teams/CIN/1970.shtml)");
		awayURLField = new JTextField(40);
		awayURLField.setText("Away team URL (e.g. http://www.baseball-reference.com/teams/BAL/1970.shtml)... :)");
		buildButton = new JButton("Build Teams");
		instructionTopLabel = new JLabel("<html>Welcome to BaseballSim.<br>Please enter the baseball reference URLs of the teams you would like to use below. Simply copy the examples given and replace with your team's abbreviation and year.</html>");
		instructionBottomLabel = new JLabel("<html>Enter your desired player numbers into boxes 1-9 for the home and away team to build your batting order. The 10th box should be the number of the desired starting pitcher<br>Please ensure that you have one player for each position in the starting lineup. You may choose to include either a DH or a pitcher in your lineup.  When all the boxes are filled out, click SIM. If you click SIM with some number in the iterations box, you'll get a statistical summary of the number of games you choose.<br>Otherwise you'll get a detailed game summary of a single game.</html>");

		buildButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				try {
					buildTeams();
				}
				catch (IOException e1) {
					JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of your URLs.\nCheck to make sure that they are both of the format http://www.baseball-reference.com/teams/SEA/1995.shtml");
				}
			}
		});	

		northPanel.add(instructionTopLabel);
		northPanel.add(homeURLField);
		northPanel.add(awayURLField);
		northPanel.add(buildButton);
		northPanel.add(instructionBottomLabel);
		return northPanel;
	}


	private void simGame() throws IOException {
		try {
			game = new Game();
			constructIntArrays();

			if (iterationsField.getText().equals("")) {
				game.playGame(home, away, league, homeInts, awayInts);
				sk.updateMultisimStats(game);
				gameLog.setText("");
				gameLog.setText(returnGameLog()+sk.generateLineScore());
				home.zeroOut();
				away.zeroOut();
			}
			else {
				int iterations = Integer.valueOf(iterationsField.getText());
				for (int i = 0; i < iterations; i++) {
					game.playGame(home, away, league, homeInts, awayInts);
					gameLog.setText("");
					sk.updateMultisimStats(game);
					home.zeroOut();
					away.zeroOut();
				}
				gameLog.setText(sk.generateMultisimStats());
			}
		}
		catch (IOException error) {
					JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of your URLs.\nCheck to make sure that they are both of the format http://www.baseball-reference.com/teams/SEA/1995.shtml");
		}
		catch (NumberFormatException error) {
			JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of the input boxes.\nPlease make sure you use only digits in the batting order, starting pitcher, and iterations boxes."); 
		}
	}

	private void constructIntArrays() {
		homeInts = new int[10];
		awayInts = new int[10];		
		int i = 0;
		for (JTextField j : homeNums)
			homeInts[i++] = ((Integer.valueOf(j.getText()))-1);
		i = 0;
		for (JTextField j : awayNums)
			awayInts[i++] = ((Integer.valueOf(j.getText()))-1);
	}

	private String returnGameLog() throws IOException {
        String result = "";
        File file = new File("gamelog.txt");
        Scanner in = new Scanner(file);
        while (in.hasNextLine())
            result += in.nextLine()+"\n";
        return result;
    }

	private void uploadTeamData() {
		int i = 1;
		homeRoster.setText("");
		for (Player p : home.roster) {
			homeRoster.append(i+". "+p.toString()+"\n");
			i++;
		}
		i = 1;
		awayRoster.setText("");
		for (Player p : away.roster) {
			awayRoster.append(i+". "+p.toString()+"\n");
			i++;
		}
	}

	private void buildTeams() throws IOException, NumberFormatException {
		try {
			TeamBuilder teamBuilder = new TeamBuilder();
			home = teamBuilder.buildTeam(homeURLField.getText());
			away = teamBuilder.buildTeam(awayURLField.getText());
			for (Player p: home.roster)
				System.out.println(p.name+": "+p.speedScore);


        	LeagueBuilder leagueBuilder = new LeagueBuilder();
        	league = leagueBuilder.buildLeague(home.year);

			uploadTeamData();
			sk = new ScoreKeeper();
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of your URLs.\nCheck to make sure that they are both of the format http://www.baseball-reference.com/teams/SEA/1995.shtml");
		}
		catch (NumberFormatException e2) {
			JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of your URLs.\nCheck to make sure that they are both of the format http://www.baseball-reference.com/teams/SEA/1995.shtml");
		}
		catch (Exception e3) {
			JOptionPane.showMessageDialog(frame, "I'm sorry, something went wrong with one of your URLs.\nCheck to make sure that they are both of the format http://www.baseball-reference.com/teams/SEA/1995.shtml");
		}		
	}
}	
