package de.mxlink.cmapp.machine;

import de.mxlink.cmapp.MainActivity;

/**
 * utility class to access an instance of LEDMatrixBTConn in connected state
 * @author maxi
 *
 */
public class ConnectionMachineFactory {

	private static LEDMatrixBTConn m_machine;
	
	
	/**
	 * 
	 * @return a connected interface to the connection machine
	 */
	public static LEDMatrixBTConn getMachine() {
		if (m_machine == null) {
			m_machine = new LEDMatrixBTConn(MainActivity.REMOTE_BT_DEVICE_NAME, MainActivity.X_SIZE, MainActivity.Y_SIZE, MainActivity.COLOR_MODE, MainActivity.APP_NAME);
			if (!m_machine.prepare() ||	!m_machine.checkIfDeviceIsPaired()) {
				m_machine = null;
				return null;
			}
			else
				if	(!m_machine.connect()) {
					m_machine = null;
					return null;
				}
		}
		else if (!m_machine.isConnected())
			if	(!m_machine.connect()) {
				m_machine = null;
				return null;
			}
		
		
		return m_machine;		
	}
}
