package com.github.fdvmavenprojectversionupdater;

import java.util.function.Consumer;

import javax.swing.JMenuBar;

public class ConfigurableMenuBar extends JMenuBar {

	private static final long serialVersionUID = 6295681529886167611L;

	public ConfigurableMenuBar(Consumer<ConfigurableMenuBar> callback) {
		callback.accept(this);
	}

	public void add(String label, Consumer<ConfigurableMenu> callback) {
		this.add(new ConfigurableMenu(label, callback));
	}

}
