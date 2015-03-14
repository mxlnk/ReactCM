package de.mxlink.cmapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;

public class GameActivity extends Activity {
	public static final String ROUND = "de.mxlink.round";
	
	private Panel m_upperLeft;
	private ImageButton m_upperLeftButton;
	private Panel m_upperRight;
	private ImageButton m_upperRightButton;
	private Panel m_lowerLeft;
	private ImageButton m_lowerLeftButton;
	private Panel m_lowerRight;
	private ImageButton m_lowerRightButton;

	private GameMaster m_master;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // take over orientation from previous activity, but don't allow changes
        /*int orientation = getIntent().getExtras().getInt(MainActivity.ORIENTATION);
        getResources().getConfiguration();
		if (orientation == Configuration.ORIENTATION_LANDSCAPE)  
        	requestWindowFeature(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
        	requestWindowFeature(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		*/
        setContentView(R.layout.activity_game);

		byte[] upperLeftLEDs = new byte[MainActivity.X_SIZE * MainActivity.Y_SIZE];
		byte[] upperRightLEDs = new byte[MainActivity.X_SIZE * MainActivity.Y_SIZE];
		byte[] lowerLeftLEDs = new byte[MainActivity.X_SIZE * MainActivity.Y_SIZE];
		byte[] lowerRightLEDs = new byte[MainActivity.X_SIZE * MainActivity.Y_SIZE];

		for (int i = 0; i < MainActivity.X_SIZE * MainActivity.Y_SIZE; i++)
			if (i < (MainActivity.X_SIZE * MainActivity.Y_SIZE) / 2 && (i / 12) % 2 == 0)
				upperLeftLEDs[i] = (byte)255;
			else if (i < (MainActivity.X_SIZE * MainActivity.Y_SIZE) / 2 && (i / 12) % 2 == 1)
				upperRightLEDs[i] = (byte)255;
			else if (i >= (MainActivity.X_SIZE * MainActivity.Y_SIZE) / 2 && (i / 12) % 2 == 0)
				lowerLeftLEDs[i] = (byte)255;
			else if (i >= (MainActivity.X_SIZE * MainActivity.Y_SIZE) / 2 && (i / 12) % 2 == 1)
				lowerRightLEDs[i] = (byte)255;
		
		m_master = new GameMaster(this);
		
		m_upperLeftButton = (ImageButton)findViewById(R.id.buttonUpperLeft);
		m_upperLeft = new Panel(m_master, upperLeftLEDs);
		m_upperLeftButton.setOnClickListener(m_upperLeft);
		
		m_upperRightButton = (ImageButton)findViewById(R.id.buttonUpperRight);
		m_upperRight = new Panel(m_master, upperRightLEDs);
		m_upperRightButton.setOnClickListener(m_upperRight);
		
		m_lowerLeftButton = (ImageButton)findViewById(R.id.buttonLowerLeft);
		m_lowerLeft = new Panel(m_master, lowerLeftLEDs);
		m_lowerLeftButton.setOnClickListener(m_lowerLeft);
		
		m_lowerRightButton = (ImageButton)findViewById(R.id.buttonLowerRight);
		m_lowerRight = new Panel(m_master, lowerRightLEDs);
		m_lowerRightButton.setOnClickListener(m_lowerRight);
		
		List<Panel> panels = new ArrayList<Panel>();
		panels.add(m_upperLeft);
		panels.add(m_upperRight);
		panels.add(m_lowerLeft);
		panels.add(m_lowerRight);
		m_master.setPanels(panels);
		
		int round = 0;
		if (savedInstanceState != null)
			round = savedInstanceState.getInt(ROUND);
		PlayGame game = new PlayGame(round);
		game.execute();
	}
	
	/*
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (m_master != null)
			outState.putInt(ROUND, m_master.getRound());
	}*/
	
	@Override
	protected void onPause() {
		super.onPause();
		ConnectionMachineFactory.getMachine().closeConnection();
	}
	
	private class PlayGame extends AsyncTask<Void,Void,Void> {
		private int m_round;
		public PlayGame(int round) {
			m_round = round;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(5000, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			m_master.startGame(m_round);
			return null;
		}
	}
}
