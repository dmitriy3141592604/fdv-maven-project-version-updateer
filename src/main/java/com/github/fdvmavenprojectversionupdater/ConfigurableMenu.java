package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JMenu;

public class ConfigurableMenu extends JMenu {

	private static final long serialVersionUID = -3920144986654842925L;

	public ConfigurableMenu(String label, Consumer<ConfigurableMenu> configurer) {
		super(label);
		configurer.accept(this);
	}

	public void add(String label, ActionListener action, String keyStrokeString) {
		this.add(new MenuItemWithKeysStroke(label, action, keyStrokeString));
	}
}