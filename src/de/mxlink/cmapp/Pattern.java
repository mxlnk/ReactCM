package de.mxlink.cmapp;

import android.util.Log;

public class Pattern {

	private byte[] m_pattern;
	private int m_x;
	private int m_y;
	
	private LEDMatrixBTConn m_matrix;
	
	public Pattern(int x, int y, LEDMatrixBTConn matrix) {
		m_matrix = matrix;
		
		m_pattern = new byte[x * y];
		m_x = x;
		m_y = y;
	}
	
	public void addLeds(byte[] pattern) {
		for (int i = 0; i < m_x * m_y; i++)
			if (pattern[i] != 0)
				m_pattern[i] = (byte)255;
	}
	
	public void clear() {
		for (int i = 0; i < m_x * m_y; i++)
			m_pattern[i] = 0;
	}
	
	public void display() {
		// Start BT sending thread.
        Thread sender = new Thread() {
    		int sendDelay;
            
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

				// Connected. Calculate and set send delay from maximum FPS.
                // Negative maxFPS should not happen.
                int maxFPS = m_matrix.getMaxFPS();
                if (maxFPS > 0)
                    sendDelay = (int) (1000.0 / maxFPS);
                 else
 					Log.e("ERROR", "error2");

				// Fill message buffer.
				byte[] msgBuffer = m_pattern;
				    	

				// If write fails, the connection was probably closed by the server.
				if (!m_matrix.write(msgBuffer))
					Log.e("ERROR", "error3");
				
				try {
					// Delay for a moment.
                    // Note: Delaying the same amount of time every frame will not give you constant FPS.
					Thread.sleep(sendDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		// Start sending thread.
		sender.start();
	}
}
