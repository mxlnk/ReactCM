package de.mxlink.cmapp.machine;

import java.util.ArrayList;
import java.util.List;

import de.mxlink.cmapp.game.GameMaster;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * represents a led panel on the connection machine, basically an observer on the buttons and a subject for the GameMaster
 * @author maxi
 *
 */
public class Panel implements OnClickListener {
	
	private byte[] m_leds;

	private GameMaster m_master;
	
	private boolean m_active;

	
	public Panel(GameMaster master, byte[] pattern) {
		super();
		this.m_master = master;
		this.m_leds = pattern;
	}

	public byte[] getLeds() {
		return m_leds;
	}
	
	public void activate() {
		m_active = true;
	}
	
	public void reset() {
		m_active = false;
	}

	@Override
	public void onClick(View v) {
		if (m_active) {
			m_master.panelClicked(this);
			m_active = false;
		}
		else {
			if (!m_master.gameOver()) {
				List<Panel> list = new ArrayList<Panel>();
				list.add(this);
				m_master.gameOver(list);
			}
		}
			
	}
}
