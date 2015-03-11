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
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

				// Fill message buffer.
				byte[] msgBuffer = m_pattern;
				    	

				// If write fails, the connection was probably closed by the server.
				if (!m_matrix.write(msgBuffer))
					Log.e("ERROR", "error3");
			}
		};
		// Start sending thread.
		sender.start();
	}
	
	public void blink() {
        Thread sender = new Thread() {
            
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                
                byte[] msgBuffer;
                byte[] empty = new byte[m_x * m_y];
				for (int i = 0; i < 11; i++) {
					if (i % 2 == 0)
						msgBuffer = empty;
					else
						msgBuffer = m_pattern;
					
					// If write fails, the connection was probably closed by the server.
					if (!m_matrix.write(msgBuffer)) {
						Log.e("ERROR", "error3");
						break;
					}
					
					try {
						// Delay for a moment.
	                    // Note: Delaying the same amount of time every frame will not give you constant FPS.
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			}
		};
		// Start sending thread.
		sender.start();
	}
}
