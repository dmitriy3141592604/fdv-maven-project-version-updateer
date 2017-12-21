package com.github.fdv.bus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.github.typemarkup.Behavior;

public class BusTest extends BusTestBase {

	public static class StringEvent {

		private String value;

		public String getValue() {
			return value;
		}

		public final void setValue(String value) {
			this.value = value;
		}
	}

	public static class ExtendedStringEvent extends StringEvent {

		public ExtendedStringEvent(String value) {
			setValue(value);
		}
	}

	@Test
	@Behavior("Можно отправлять сообщения в существующие топики (Вариант с одним наблюдателем)")
	public void test$oneSubscriber() {
		final String topicName = randomString();

		bus.subscribe(topicName, StringEvent.class, (e) -> logMessage("accept: " + e.getValue()));

		assertEquals("", callLog());

		final String messageContent = randomString();

		bus.post(topicName, new ExtendedStringEvent(messageContent));

		assertEquals("MSG:accept: " + messageContent, callLog());
	}

	@Test
	@Behavior("Можно отправлять сообщения в зарегистрированные топики (Вариант с несколькими наблюдателями")
	public void test$multipleSubscription() {
		final String topicName = randomString();

		final int subscriberCount = 2;
		for (int i = 0; i < subscriberCount; ++i) {
			final int ii = i;
			bus.subscribe(topicName, StringEvent.class, (e) -> logMessage("accept(" + ii + ")" + e.getValue()));
		}

		final String messageContent = randomString();
		bus.post(topicName, new ExtendedStringEvent(messageContent));

		assertEquals("truetruefalse", "" + callLog().contains("accept(0)") + callLog().contains("accept(1)") + callLog().contains("accept(3)"));
	}

	@Test
	@Behavior("Для отправки сообщений подписчики не требуются")
	public void test$noSubscribers() {
		final String topicName = randomString();
		bus.post(topicName, new ExtendedStringEvent(randomString()));
	}

	@Test
	@Behavior("При отстутствии подписчиков, генерируется предупреждение")
	public void test$generateWarningIfNoSubscribersPresent() {
		final Logger mockLogger = Mockito.mock(Logger.class);
		final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.doNothing().when(mockLogger).warn(messageCaptor.capture(), topicCaptor.capture(), eventCaptor.capture());

		bus.setLogger(mockLogger);

		final String topicName = "tn" + randomString();
		final String message = "msg" + randomString();

		bus.post(topicName, new ExtendedStringEvent(message));

		assertEquals("Для топика: [{}] нет получателей. Полученное сообщение: [{}]", messageCaptor.getValue());
		assertEquals(true, topicCaptor.getValue().contains(topicName));
		assertEquals(true, eventCaptor.getValue().contains(message));

	}

}
