package com.github.fdv.bus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typemarkup.Responsibility;
import com.google.gson.Gson;

@Responsibility("Обеспечивает передачу сообщений между объектами, скрывая типы отправителя и получателя.")
public class Bus {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<String, TypedList<?>> topics = new HashMap<>();

	public <EventType> Bus post(String topicName, EventType event) {
		final TypedList<?> topicListeners = topics.get(topicName);
		if (topicListeners == null) {
			logger.warn("Для топика: [{}] нет получателей. Полученное сообщение: [{}]", topicName, new Gson().toJson(event));
			return this;
		}

		final Class<?> acceptedEventsBaseTypeClass = topicListeners.getAcceptedEventsBaseTypeClass();
		final Class<? extends Object> class1 = event.getClass();
		if (acceptedEventsBaseTypeClass.isAssignableFrom(class1)) {
			topicListeners.fire(event);
		}
		return this;
	}

	public <T> void subscribe(String topicName, Class<T> eventType, Consumer<T> consumer) {
		TypedList<?> topicListeners = topics.get(topicName);
		if (topicListeners == null) {
			topicListeners = new TypedList<T>();
			topicListeners.setAcceptedEventBaseTypeClass(eventType);
			topics.put(topicName, topicListeners);
		}
		topicListeners.add(consumer);
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
