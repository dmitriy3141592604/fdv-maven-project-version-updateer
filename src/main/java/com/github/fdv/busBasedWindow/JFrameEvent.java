package com.github.fdv.busBasedWindow;

import javax.swing.JFrame;

public class JFrameEvent {

	private final JFrame frame;

	public JFrameEvent(JFrame frame) {
		this.frame = frame;
	}

	public JFrame getJFrame() {
		return frame;
	}

}
