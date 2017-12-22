package com.github.fdv.bus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.typemarkup.Behavior;

public class TypedListTest extends TypedListBase {

	@Test
	@Behavior("Формирует отладочное сообщение при добавлении листенера. Проверка шаблона")
	public void test$add$debugMessageWhenListenerAdded$messageTemplate() {

		doNothing().when(logger).debug(stringCaptor.capture(), Mockito.any(), Mockito.any());

		typedList.add(randomString(), emptyConsumer);

		assertEquals("Adding consumer with class: [{}] to topic: [{}]", stringCaptor.getValue());

	}

	@Test
	@Behavior("Формирует отладочное сообщение при добавлении листенера. Проверка класса Consumer")
	public void test$add$debugTemplateWhenListenerAdded$consumerClass() {
		doNothing().when(logger).debug(Mockito.any(), asObject(stringCaptor.capture()), Mockito.any());
		typedList.add(randomString(), emptyConsumer);
		assertEquals(emptyConsumer.getClass(), stringCaptor.getValue());
	}

	@Test
	@Behavior("Формирует отладочное сообщение при добавлении листенера. Проверка имени топика")
	public void test$add$debugTemplateWhenListenerAdded$topicName() {
		doNothing().when(logger).debug(Mockito.any(), asObject(Mockito.any()), asObject(stringCaptor.capture()));
		final String topicName = randomString();
		typedList.add(topicName, emptyConsumer);
		assertEquals(topicName, stringCaptor.getValue());
	}

	@Test
	@Behavior("add. Контракт не допускает пустых топиков. NullPointerException")
	public void test$add$nullPointer$topic$null() {
		expected.expect(NullPointerException.class);
		expected.expectMessage("topicName is null");

		typedList.add(null, emptyConsumer);
	}

	@Test
	@Behavior("add. Контракт не допускает пустых топиков. empty")
	public void test$add$nullPointer$topic$empty() {
		expected.expect(IllegalArgumentException.class);
		expected.expectMessage("topicName is empty");

		typedList.add("", emptyConsumer);
	}

	@Test
	@Behavior("add. Контракт не допускает null consumer")
	public void test$add$nullPointer$consumer$null() {
		expected.expect(NullPointerException.class);
		expected.expectMessage("consumer is null");
		typedList.add(randomString(), null);
	}

	@Test
	@Behavior("fire. Контракт не позволяет отправлять в пустые топики. null")
	public void test$fire$topic$null() {
		expected.expect(NullPointerException.class);
		expected.expectMessage("topicName is null");
		typedList.fire(null, emptyConsumer);
	}

	@Test
	@Behavior("fire. Контраке те позволяет отправлять в пустые топики: empty")
	public void test$fire$topic$empty() {
		expected.expect(IllegalArgumentException.class);
		expected.expectMessage("topicName is empty");
		typedList.fire("", emptyConsumer);
	}

	@Test
	@Behavior("fire. Контраке не позволяет отправлять null эвенты")
	public void test$fire$event$null() {
		expected.expect(NullPointerException.class);
		expected.expectMessage("event is null");

		typedList.fire(randomString(), null);
	}

	@Test
	@Behavior("При выполнении листенера формируется отладочное сообщение")
	public void test$fire$debugMessage() {
		final String randomEvent = randomString();
		final String topicName = randomEvent;

		final StringBuilder formatBuffer = new StringBuilder();
		final StringBuilder topicNameBuffer = new StringBuilder();
		final StringBuilder eventStringBuffer = new StringBuilder();
		final StringBuilder listenerStringBuffer = new StringBuilder();
		typedList.add(topicName, emptyConsumer);
		{
			// Забываем сообщения с регистрации
			typedList.setLogger(new AbstractLogger() {

				@Override
				public void debug(String format, Object... arguments) {
					formatBuffer.append(format);
					topicNameBuffer.append(arguments[0]);
					eventStringBuffer.append(arguments[1]);
					listenerStringBuffer.append(arguments[2]);
				}
			});
		}

		typedList.fire(topicName, randomEvent);

		assertEquals("Topic: [{}]. Accept event: [{}] for listener: [{}]", formatBuffer.toString());
		assertEquals(topicName, topicNameBuffer.toString());
		assertEquals(randomEvent, eventStringBuffer.toString());
		assertEquals(true, listenerStringBuffer.toString().contains(TypedListBase.class.getSimpleName()));
	}

	@Test
	@Behavior("fire. При ошибке выполнени листенера, она логируется и игнорируется")
	public void test$fire$ignoreListenerExecutionExceptions() {
		final String topicName = randomString();

		final Consumer<?> badConsumer = v -> {
			throw new RuntimeException("ERROR!");
		};
		typedList.add(topicName, markerListener("l1"));
		typedList.add(topicName, badConsumer);
		typedList.add(topicName, markerListener("l3"));

		{
			// Забываем старые сообщения
			typedList.setLogger(logger = new AbstractLogger() {

				@Override
				public void error(String msg, Throwable t) {
					logMessage(msg);
					logMessage(t.getMessage());
				}

				@Override
				public void debug(String format, Object... arguments) {

				}

			});
		}

		typedList.fire(topicName, randomString());

		assertEquals("MSG:l1,MSG:Ошибка при отправке сообещния в топик: [" + topicName + "] листенеру: [" + String.valueOf(badConsumer)
				+ "] с индексом[1],MSG:ERROR!,MSG:l3", callLog());

	}

	private Consumer<?> markerListener(String marker) {
		return v -> logMessage(marker);
	}
}
