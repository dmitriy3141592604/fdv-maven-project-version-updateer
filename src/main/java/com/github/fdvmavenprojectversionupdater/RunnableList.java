package com.github.fdvmavenprojectversionupdater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunnableList {

	private final List<Runnable> listeners = new ArrayList<>();

	public RunnableList fire() {
		this.listeners.forEach(r -> r.run());
		return this;
	}

	public RunnableList register(Runnable r) {
		this.listeners.add(r);
		return this;
	}

	public RunnableList remove(Runnable r) {
		final Iterator<Runnable> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == r) {
				iterator.remove();
			}
		}
		return this;
	}

}
