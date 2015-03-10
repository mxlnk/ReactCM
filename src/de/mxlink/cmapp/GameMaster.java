package de.mxlink.cmapp;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class GameMaster {

	
	private static final int LEVEL_ONE = 5;
	private static final int LEVEL_TWO = 20;

	private List<Panel> m_panels;
	
	private boolean m_gameOver;
	
	public GameMaster(List<Panel> panels) {
		m_panels = panels;
	}
	
	public void startGame() {
		m_gameOver = false;
		int round = 0;
		Pattern currentPattern = new Pattern(MainActivity.X_SIZE, MainActivity.Y_SIZE, ConnectionMachineFactory.getMachine());
		while(!m_gameOver) {
			currentPattern.clear();
			round++;
			int timeForRound = getTime(round);
			List<Panel> panels = getPanels(round);
			Log.i("GAME", "round " + round + " with time " + timeForRound);
			for (Panel p : panels) {
				p.activate();
				currentPattern.addLeds(p.getLeds());
			}
			currentPattern.display();
			try {
				Thread.sleep(timeForRound, 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Panel p : panels)
				if (!p.successfull())
					gameOver(panels);
		}
	}
	
	public void gameOver(List<Panel> panels) {
		m_gameOver = true;
		ConnectionMachineFactory.getMachine().closeConnection();
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
		int time = 3000;
		
		time *= rand;
		
		if (round <= LEVEL_ONE)
			time /= 2;
		else if (round <= LEVEL_TWO)
			time /= 3;
		else
			time /= 4;
		
		return time;
	}
}
