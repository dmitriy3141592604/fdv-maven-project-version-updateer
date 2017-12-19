package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.rules.TestRule;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public abstract class UpdateVersionControllerTestBase {

	@Mock
	public FileClassificator fileClassificator;

	@InjectMocks
	public UpdateVersionController model;

	@Rule
	public final TestRule restoreSystemProperties = new RestoreSystemProperties();

	private StringBuilder callLog;

	@Before
	public final void setUpUpdateVersionControllerModelTestBase() {
		model = new UpdateVersionController();
		model.setFileClassificator(fileClassificator);
		callLog = new StringBuilder();
	}

	protected String callLog() {
		return callLog.toString();
	}

	protected void addMessage(String prefix, String message) {
		callLog.append(prefix).append(":").append(message);
	}

	protected void addMessage(String message) {
		callLog.append(message);
	}
}
