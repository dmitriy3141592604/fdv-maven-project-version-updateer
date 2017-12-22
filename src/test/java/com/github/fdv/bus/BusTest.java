package com.github.fdv.bus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.github.fdv.busBasedWindow.SimpleBusEvent;
import com.github.typemarkup.Behavior;

public class BusTest extends BusTestBase {

	public static class StringEventForTest extends SimpleBusEvent<String> {

		public StringEventForTest(String value) {
			super(value);
		}
	}

	public static class ExtendedStringEventForTest extends StringEventForTest {

		public ExtendedStringEventForTest(String value) {
			super(value);
		}
	}

	@Test
	@Behavior("Можно отправлять сообщения в существующие топики (Вариант с одним наблюдателем)")
	public void test$oneSubscriber() {
		final String topicName = randomString();

		bus.subscribe(topicName, StringEventForTest.class, (e) -> logMessage("accept: " + e.getValue()));

		assertEquals("", callLog());

		final String messageContent = randomString();

		bus.post(topicName, new ExtendedStringEventForTest(messageContent));

		assertEquals("MSG:accept: " + messageContent, callLog());
	}

	@Test
	@Behavior("Можно отправлять сообщения в зарегистрированные топики (Вариант с несколькими наблюдателями")
	public void test$multipleSubscription() {
		final String topicName = randomString();

		final int subscriberCount = 2;
		for (int i = 0; i < subscriberCount; ++i) {
			final int ii = i;
			bus.subscribe(topicName, StringEventForTest.class, (e) -> logMessage("accept(" + ii + ")" + e.getValue()));
		}

		final String messageContent = randomString();
		bus.post(topicName, new ExtendedStringEventForTest(messageContent));

		assertEquals("truetruefalse", "" + callLog().contains("accept(0)") + callLog().contains("accept(1)") + callLog().contains("accept(3)"));
	}

	@Test
	@Behavior("Для отправки сообщений подписчики не требуются")
	public void test$noSubscribers() {
		final String topicName = randomString();
		bus.post(topicName, new ExtendedStringEventForTest(randomString()));
	}

	@Test
	@Behavior("При отстутствии подписчиков, генерируется предупреждение")
	public void test$generateWarningIfNoSubscribersPresent() {
		final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> topicNameCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> eventClassCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> eventAsStringCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.doNothing().when(logger).warn(messageCaptor.capture(), topicNameCaptor.capture(), eventClassCaptor.capture(),
				eventAsStringCaptor.capture());

		// bus.setLogger(mockLogger);

		final String topicName = "tn" + randomString();
		final String message = "msg" + randomString();

		bus.post(topicName, new ExtendedStringEventForTest(message));

		assertEquals("Для топика: [{}] нет получателей. Полученное сообщение: [{}][{}]", messageCaptor.getValue());
		assertEquals(true, topicNameCaptor.getValue().contains(topicName));
		assertEquals(true, logValue("eventClass", eventClassCaptor, false).getValue().contains(ExtendedStringEventForTest.class.getSimpleName()));
		assertEquals(true, eventAsStringCaptor.getValue().contains(message));

	}

}
