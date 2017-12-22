package com.github.fdv.menus;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableMenu extends JMenu {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = -3920144986654842925L;

	public ConfigurableMenu(String label, Consumer<ConfigurableMenu> configurer) {
		super(label);
		configurer.accept(this);
	}

	public void add(String label, ActionListener action, String keyStrokeString) {
		this.add(new MenuItemWithKeysStroke(label, action, keyStrokeString));
	}

}