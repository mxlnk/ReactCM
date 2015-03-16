package de.mxlink.cmapp.machine;

import android.util.Log;

/**
 * A pattern displayable on the connection machine's panels
 * @author maxi
 *
 */
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
	
	/**
	 * add a subpattern, the pattern will turn on any led previously turned on or turned on by given pattern
	 * @param pattern subpattern to add
	 */
	public void addLeds(byte[] pattern) {
		for (int i = 0; i < m_x * m_y; i++)
			if (pattern[i] != 0)
				m_pattern[i] = (byte)255;
	}
	
	/**
	 * resets the pattern to not display anything
	 */
	public void clear() {
		for (int i = 0; i < m_x * m_y; i++)
			m_pattern[i] = 0;
	}
	
	/**
	 * displays the pattern on the connection machine
	 */
	public void display() {
		// Start BT sending thread.
        Thread sender = new Thread() {            
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

				// Fill message buffer.
				byte[] msgBuffer = m_pattern;
				    	

				// If write fails, the connection was probably closed by the server.
				if (!m_matrix.write(msgBuffer))
					Log.e("ERROR", "failed to write");
			}
		};
		// Start sending thread.
		sender.start();
	}
	
	/**
	 * lets the pattern appear and disappear several times on the connection machine
	 * @return
	 */
	public int blink() {
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
						Log.e("ERROR", "failed to write");
						break;
					}
					
					try {
						// Delay for a moment.
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			}
		};
		// Start sending thread.
		sender.start();
		return 12 * 500;
	}
}
