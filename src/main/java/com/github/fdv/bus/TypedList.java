package com.github.fdv.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TypedList<EventType> {

	private Class<?> acceptableEventsBaseTypeClass;

	private final List<Consumer<Object>> listeners = new ArrayList<>();

	public void fire(Object event) {
		// Проверка, что тип допустим
		acceptableEventsBaseTypeClass.cast(event);
		listeners.forEach(listener -> listener.accept(event));
	}

	public Class<?> getAcceptedEventsBaseTypeClass() {
		return acceptableEventsBaseTypeClass;
	}

	@SuppressWarnings("unchecked")
	public void add(Consumer<?> consumer) {
		listeners.add((Consumer<Object>) consumer);
	}

	public void setAcceptedEventBaseTypeClass(Class<?> eventType) {
		this.acceptableEventsBaseTypeClass = eventType;
	}

}
