package com.github.fdvmavenprojectversionupdater;

import javax.swing.JFrame;

public class UpdateVersionWindow extends UpdateVersionWindowBase implements Runnable {

	public static void main(String... args) {
		new UpdateVersionWindow().run();
	}

	@Override
	public void run() {
		configureFrame();
		initializaComponents();
		alignComponents();
		frame.setVisible(true);
	}

	private void configureFrame() {
		frame.setTitle("Обновление версии pom.xml");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(200, 200, 600, 400);
	}

}
