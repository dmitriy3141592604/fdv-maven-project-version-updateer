package com.github.fdv.busBasedWindow;

public class SimpleBusEvent<T> {

	private T value;

	public SimpleBusEvent() {
	}

	public SimpleBusEvent(T value) {
		this.value = value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("[").append(getClass().getName()).append("{ value=\"").append(String.valueOf(value) + "}]").toString();
	}

}
