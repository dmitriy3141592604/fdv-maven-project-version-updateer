package com.github.fdv.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypedList<EventType> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final List<Consumer<Object>> listeners = new ArrayList<>();

	public void fire(String topicName, Object event) {
		check(topicName, "topicName");
		check(event, "event");
		final int[] idxs = new int[1];
		idxs[0] = -1;
		listeners.forEach(listener -> {
			logger.debug("Topic: [{}]. Accept event: [{}] for listener: [{}]", topicName, String.valueOf(event), String.valueOf(listener));
			try {
				idxs[0] = idxs[0] + 1;
				listener.accept(event);
			} catch (final Exception exception) {
				logger.error("Ошибка при отправке сообещния в топик: [" + topicName + "] листенеру: [" + String.valueOf(listener) + "] с индексом["
						+ idxs[0] + "]", exception);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void add(String topicName, Consumer<?> consumer) {
		check(topicName, "topicName");
		check(consumer, "consumer");
		logger.debug("Adding consumer with class: [{}] to topic: [{}]", consumer.getClass(), topicName);
		listeners.add((Consumer<Object>) consumer);
	}

	private void check(Object object, String message) {
		if (object == null) {
			throw new NullPointerException(message + " is null");
		}
		if ("".equals(String.valueOf(object))) {
			throw new IllegalArgumentException(message + " is empty");
		}

	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
