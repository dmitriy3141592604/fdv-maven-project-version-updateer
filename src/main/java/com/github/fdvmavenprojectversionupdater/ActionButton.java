package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ActionButton extends JButton {

	private static final long serialVersionUID = 1L;

	public ActionButton(String label, ActionListener al) {
		setText(label);
		addActionListener(al);
		setHorizontalAlignment(SwingConstants.LEFT);
	};
}
