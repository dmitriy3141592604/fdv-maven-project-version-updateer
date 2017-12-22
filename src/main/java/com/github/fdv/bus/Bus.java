package com.github.fdv.bus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typemarkup.Responsibility;

@Responsibility("Обеспечивает передачу сообщений между объектами, скрывая типы отправителя и получателя.")
public class Bus {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<String, TypedList<?>> topics = new HashMap<>();

	public <EventType> Bus post(String topicName, EventType event) {
		final TypedList<?> topicListeners = topics.get(topicName);
		if (topicListeners == null) {
			// TODO 2017-12-22: Если тут возникает ошибка, то все рушится
			// TODO 2017-12-22: Вообще сообщене должно уходить в deadqueue
			final String eventClass = event.getClass().toString();
			final String eventAsString = String.valueOf(event);
			logger.warn("Для топика: [{}] нет получателей. Полученное сообщение: [{}][{}]", topicName, eventClass, eventAsString);
			return this;
		}

		topicListeners.fire(topicName, event);
		return this;
	}

	public <T> void subscribe(String topicName, Class<T> eventType, Consumer<T> consumer) {
		TypedList<?> topicListeners = topics.get(topicName);
		if (topicListeners == null) {
			topicListeners = new TypedList<T>();
			topics.put(topicName, topicListeners);
		}
		topicListeners.add(topicName, consumer);
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
