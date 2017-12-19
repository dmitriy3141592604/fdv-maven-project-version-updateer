package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;

public abstract class LoggableTestBase {

	private StringBuilder callLog;

	protected boolean isFirstRecord;

	@Before
	public final void setUpLoggableTestBase() {
		callLog = new StringBuilder();
		isFirstRecord = true;
	}

	protected String callLog() {
		return callLog.toString();
	}

	protected void logMessage(String message) {
		if (!isFirstRecord) {
			callLog.append(",");
		}
		callLog.append("MSG:").append(message);
		isFirstRecord = false;
	}
}
