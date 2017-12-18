package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;

public abstract class VersionHolderTestBase {

	private StringBuilder callLog;

	private boolean isFirstRecord;

	@Before
	public final void setUpVersionHolderTestBase() {
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
		callLog.append(message);
		isFirstRecord = false;
	}

	protected VersionHolder newVersionHolder(String version) {
		final VersionHolder versionHolder = new VersionHolder();
		versionHolder.setVersion(version);
		return versionHolder;
	}

}
