package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ExitAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}