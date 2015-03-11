package de.mxlink.cmapp;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class GameMaster {

	
	private static final int LEVEL_ONE = 5;
	private static final int LEVEL_TWO = 20;

	private List<Panel> m_panels;
	private List<Panel> m_activePanels;
	
	private boolean m_gameOver;
	
	public GameMaster(List<Panel> panels) {
		m_panels = panels;
	}
	
	public GameMaster() {
		m_panels = new ArrayList<Panel>(); 
	}
	
	public void startGame() {
		m_gameOver = false;
		int round = 0;
		Pattern currentPattern = new Pattern(MainActivity.X_SIZE, MainActivity.Y_SIZE, ConnectionMachineFactory.getMachine());
		while(!m_gameOver) {
			currentPattern.clear();
			round++;
			int timeForRound = getTime(round);
			m_activePanels = getPanels(round);
			Log.i("GAME", "round " + round + " with time " + timeForRound);
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
			if (!m_activePanels.isEmpty())
				gameOver(m_activePanels);
			for (Panel p : m_panels)
				p.reset();
		}
	}
	
	public void gameOver(List<Panel> panels) {
		m_gameOver = true;
		Pattern over = new Pattern(MainActivity.X_SIZE, MainActivity.Y_SIZE, ConnectionMachineFactory.getMachine());
		for (Panel p : panels)
			over.addLeds(p.getLeds());
		
		over.blink();
	}
	
	public void setPanels(List<Panel> panels) {
		this.m_panels = panels;
	}
	
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
}
