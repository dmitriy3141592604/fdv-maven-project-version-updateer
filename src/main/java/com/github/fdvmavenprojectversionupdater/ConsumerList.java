package com.github.fdvmavenprojectversionupdater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerList<T> {

	private final List<Consumer<T>> listeners = new ArrayList<>();

	public ConsumerList<T> fire(T t) {
		listeners.forEach(c -> c.accept(t));
		return this;
	}

	public ConsumerList<T> register(Consumer<T> consumer) {
		listeners.add(consumer);
		return this;
	}

	public ConsumerList<T> unRegister(Consumer<T> consumer) {
		final Iterator<Consumer<T>> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == consumer) {
				iterator.remove();
			}
		}
		return this;
	}

}
