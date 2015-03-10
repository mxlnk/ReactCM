package de.mxlink.cmapp;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

public class Panel implements OnClickListener {

	private byte[] m_leds;

	private GameMaster m_master;
	
	private boolean m_active;
	private boolean m_success;

	
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
		m_success = false;
	}

	public boolean successfull() {
		return m_success;
	}
	
	public void deactivate() {
		m_active = false;
		m_success = false;
	}

	@Override
	public void onClick(View v) {
		if (m_active)
			m_success = true;
		else {
			List<Panel> list = new ArrayList<Panel>();
			list.add(this);
			m_master.gameOver(list);
		}
			
	}
}
