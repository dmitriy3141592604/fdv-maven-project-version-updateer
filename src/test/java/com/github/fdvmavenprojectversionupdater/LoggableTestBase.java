package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;

import com.github.typemarkup.Responsibility;

@Responsibility("Предоставляет возможность отслеживания вызова методов во время выполнения теста")
public abstract class LoggableTestBase {

	private StringBuilder callLog;

	@Responsibility("")
	protected boolean isFirstRecord;

	@Before
	public final void setUpLoggableTestBase() {
		callLog = new StringBuilder();
		isFirstRecord = true;
	}

	@Responsibility("Предоставляет возможность отследить сделанные во время тестирования вызовы")
	protected String callLog() {
		return callLog.toString();
	}

	@Responsibility("Позволяет регистрировать вызовы методов тестируемых классов")
	protected void logMessage(String message) {
		if (!isFirstRecord) {
			callLog.append(",");
		}
		callLog.append("MSG:").append(message);
		isFirstRecord = false;
	}
}
