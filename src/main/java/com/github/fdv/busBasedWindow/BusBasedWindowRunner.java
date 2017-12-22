package com.github.fdv.busBasedWindow;

import java.awt.event.ActionEvent;

import com.github.fdv.bus.Bus;

public class BusBasedWindowRunner extends Bus {

	public static void main(String... args) {
		new BusBasedWindow(new BusBasedWindowRunner().configure()).setVisible(true);

	}

	private Bus configure() {
		subscribe("exitAction", ActionEvent.class, e -> {
			logger.warn("Exit");
			System.exit(0);
		});
		return this;
	}

}
