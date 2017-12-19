package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PackAction implements ActionListener {

	private final RunnableList packActionListeners = new RunnableList();

	@Override
	public void actionPerformed(ActionEvent e) {
		packActionListeners.fire();
	}

	public RunnableList getPackAcctionListeners() {
		return packActionListeners;
	}
}