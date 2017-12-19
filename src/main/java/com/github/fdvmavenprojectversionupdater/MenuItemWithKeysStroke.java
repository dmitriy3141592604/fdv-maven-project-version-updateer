package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuItemWithKeysStroke extends JMenuItem {

	private static final long serialVersionUID = -8543468324960530191L;

	public MenuItemWithKeysStroke(String label, ActionListener action, String keyStrokeString) {
		super(label);
		addActionListener(action);
		setAccelerator(KeyStroke.getKeyStroke(keyStrokeString));
	}

}