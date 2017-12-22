package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typemarkup.Responsibility;

@Responsibility("Предоставляет возможность отслеживания вызова методов во время выполнения теста")
public abstract class LoggableTestBase {

	protected final Logger logableTestLogger = LoggerFactory.getLogger(getClass());

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

	protected <T> T logValue(String marker, T t) {
		return logValue(marker, t, false);
	}

	protected <T> T logValue(String marker, T t, boolean show) {
		if (show) {
			logableTestLogger.warn("[{}]: [{}]", marker, String.valueOf(t));
		}
		return t;
	}
}
