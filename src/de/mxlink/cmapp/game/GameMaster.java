package de.mxlink.cmapp.game;

import java.util.ArrayList;
import java.util.List;

import de.mxlink.cmapp.GameOverActivity;
import de.mxlink.cmapp.MainActivity;
import de.mxlink.cmapp.machine.ConnectionMachineFactory;
import de.mxlink.cmapp.machine.Panel;
import de.mxlink.cmapp.machine.Pattern;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * controls the game, decides which panels get active and how much time the player has to react
 * @author maxi
 *
 */
public class GameMaster {
	public static final String ROUNDS = "de.mxlink.rounds";
	public static final String LOGGING = "game";
	
	private Activity m_gameActivity;
	
	private static final int LEVEL_ONE = 5;
	private static final int LEVEL_TWO = 20;

	private List<Panel> m_panels;
	private List<Panel> m_activePanels;
	
	private boolean m_gameOver;
	private int m_round;
	
	public GameMaster(Activity activity) {
		m_panels = new ArrayList<Panel>();
		m_gameActivity = activity;
	}
	
	public int getRound() {
		return m_round;
	}
	
	/**
	 * start game starting from round
	 * @param round the round to start from
	 */
	public void startGame(int round) {
		if (m_panels.isEmpty())
			gameOver(m_panels);
		m_gameOver = false;
		m_round = round;
		Pattern currentPattern = new Pattern(MainActivity.X_SIZE, MainActivity.Y_SIZE, ConnectionMachineFactory.getMachine());
		while(!m_gameOver) {
			currentPattern.clear();
			m_round++;
			int timeForRound = getTime(m_round);
			m_activePanels = getPanels(m_round);
			Log.i(LOGGING, "round " + m_round + " with time " + timeForRound);
			for (Panel p : m_activePanels) {
				p.activate();
				currentPattern.addLeds(p.getLeds());
			}
			currentPattern.display();
			try {
				Thread.sleep(timeForRound, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!m_activePanels.isEmpty() && !m_gameOver)
				gameOver(m_activePanels);
			for (Panel p : m_panels)
				p.reset();
		}
	}
	
	/**
	 * end game
	 * @param panels the panels that caused gameOver
	 */
	public void gameOver(List<Panel> panels) {
		m_gameOver = true;
		if (!panels.isEmpty()) {
			Pattern over = new Pattern(MainActivity.X_SIZE, MainActivity.Y_SIZE, ConnectionMachineFactory.getMachine());
			for (Panel p : panels)
				over.addLeds(p.getLeds());
			
			int delay = over.blink();
			
			try {
				Thread.sleep(delay, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Intent intent = new Intent(m_gameActivity, GameOverActivity.class);
		intent.putExtra(ROUNDS, "" + (m_round - 1));
		m_gameActivity.startActivity(intent);
	}
	
	public void setPanels(List<Panel> panels) {
		this.m_panels = panels;
	}
	
	/**
	 * called if a panel gets clicked
	 * @param panel the panel that got clicked
	 */
	public void panelClicked(Panel panel) {
		m_activePanels.remove(panel);
		Pattern pattern = new Pattern(MainActivity.X_SIZE, MainActivity.Y_SIZE, ConnectionMachineFactory.getMachine());
		for (Panel p : m_activePanels)
			pattern.addLeds(p.getLeds());
		pattern.display();
	}
	
	private List<Panel> getPanels(int round) {
		List<Panel> ret = new ArrayList<Panel>();
		Panel draw = drawPanel();
		ret.add(draw);
		if (round > LEVEL_ONE) {
			draw = drawPanel();
			if (!ret.contains(draw))
				ret.add(draw);
		}
		if (round > LEVEL_TWO) {
			draw = drawPanel();
			if (!ret.contains(draw))
				ret.add(draw);
		}	
		return ret;
	}
	
	private Panel drawPanel() {
		int rand = (int)Math.floor(Math.random() * 4);
		return m_panels.get(rand);
	}
	
	private int getTime(int round) {
		int rand = (int)Math.floor(Math.random() * 5) + 2;	// base is between 2 and 6 seconds
		int time = 2000;
		
		time *= rand;
		
		if (round <= LEVEL_ONE)
			time /= 2;
		else if (round <= LEVEL_TWO)
			time /= 3;
		else
			time /= 4;
		
		return time;
	}
	
	/**
	 * 
	 * @return true if game is over, false otherwise
	 */
	public boolean gameOver() {
		return m_gameOver;
	}
}
