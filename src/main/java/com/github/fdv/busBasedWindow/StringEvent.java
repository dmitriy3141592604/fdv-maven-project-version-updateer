package com.github.fdv.busBasedWindow;

public class StringEvent extends SimpleBusEvent<String> {

	public StringEvent(String value) {
		super(value);
	}

	public String getText() {
		return getValue();
	}

}
