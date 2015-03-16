package de.mxlink.cmapp;


import de.mxlink.cmapp.machine.ConnectionMachineFactory;
import de.mxlink.cmapp.machine.LEDMatrixBTConn;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class MainActivity extends Activity {
//	public static final String ORIENTATION = "de.mxlink.orientation";
	
	private LEDMatrixBTConn BT;
	public static final String REMOTE_BT_DEVICE_NAME = "ubuntu-gnome-0";

	// Remote display x and y size.
	public static final int X_SIZE = 24;
	public static final int Y_SIZE = 24;

	// Remote display color mode. 0 = red, 1 = green, 2 = blue, 3 = RGB.
	public static final int COLOR_MODE = 0;

	// The name this app uses to identify with the server.
	public static final String APP_NAME = "ReactCM";

	// The start button.
	private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.startButton);
    }


	public void start(View v) {
		mStartButton.setEnabled(false);

		if ((BT = ConnectionMachineFactory.getMachine()) == null) {
			Toast.makeText(this, "is your bluetooth on and are you paired?", 10 * 1000).show();;
			mStartButton.setEnabled(true);
			return;
		}
		
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);        
	}

	@Override
	public void onPause() {
		super.onPause();

        mStartButton.setEnabled(true);

        // Avoid crash if user exits the app before pressing start.
        if (BT != null) {
            BT.onPause();
        }
	}
}
