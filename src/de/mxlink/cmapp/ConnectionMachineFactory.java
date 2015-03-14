package de.mxlink.cmapp;

public class ConnectionMachineFactory {

	private static LEDMatrixBTConn m_machine;
	
	
	
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
		return m_machine;		
	}
}
