package de.mxlink.cmapp;

import de.mxlink.cmapp.game.GameMaster;
import de.mxlink.cmapp.machine.ConnectionMachineFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity {
	private Button m_startButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game_over_activity);
		
		TextView rounds = (TextView) findViewById(R.id.roundText);
		rounds.setText(getIntent().getExtras().getString(GameMaster.ROUNDS));	
		
		
		m_startButton = (Button) findViewById(R.id.startButton2);
	}
	
	public void start(View v) {
		m_startButton.setEnabled(false);

		if (ConnectionMachineFactory.getMachine() == null) {
			Toast.makeText(this, "is your bluetooth on and are you paired?", 10 * 1000).show();;
			m_startButton.setEnabled(true);
			return;
		}
			
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);      
	}
}
