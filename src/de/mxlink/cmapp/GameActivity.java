package de.mxlink.cmapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

public class GameActivity extends Activity {
	private Panel m_upperLeft;
	private Button m_upperLeftButton;
	private Panel m_upperRight;
	private Button m_upperRightButton;
	private Panel m_lowerLeft;
	private Button m_lowerLeftButton;
	private Panel m_lowerRight;
	private Button m_lowerRightButton;

	
	private GameMaster m_master;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		
		m_master = new GameMaster();
		
		m_upperLeftButton = (Button)findViewById(R.id.buttonUpperLeft);
		m_upperLeft = new Panel(m_master, upperLeftLEDs);
		m_upperLeftButton.setOnClickListener(m_upperLeft);
		
		m_upperRightButton = (Button)findViewById(R.id.buttonUpperRight);
		m_upperRight = new Panel(m_master, upperRightLEDs);
		m_upperRightButton.setOnClickListener(m_upperRight);
		
		m_lowerLeftButton = (Button)findViewById(R.id.buttonLowerLeft);
		m_lowerLeft = new Panel(m_master, lowerLeftLEDs);
		m_lowerLeftButton.setOnClickListener(m_lowerLeft);
		
		m_lowerRightButton = (Button)findViewById(R.id.buttonLowerRight);
		m_lowerRight = new Panel(m_master, lowerRightLEDs);
		m_lowerRightButton.setOnClickListener(m_lowerRight);
		
		List<Panel> panels = new ArrayList<Panel>();
		panels.add(m_upperLeft);
		panels.add(m_upperRight);
		panels.add(m_lowerLeft);
		panels.add(m_lowerRight);
		m_master.setPanels(panels);
		
		PlayGame game = new PlayGame();
		game.execute();
	}
	

	private class PlayGame extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(5000, 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_master.startGame();
			return null;
		}
		
	}
}
