package com.github.fdvmavenprojectversionupdater;

import java.io.File;

import javax.swing.JFrame;

import org.slf4j.LoggerFactory;

public class UpdateVersionWindow extends UpdateVersionWindowBase implements Runnable {

	public static void main(String... args) {
		final UpdateVersionWindow window = new UpdateVersionWindow();
		window.run();
		if (args.length > 0) {
			final File file = new File(args[0]);
			if (file.exists()) {
				if (file.isDirectory()) {
					window.searchDir = file;
				} else if (file.isFile()) {
					window.selectedFile = file;
					window.processSelectedFile();
				}

			} else {
				LoggerFactory.getLogger(UpdateVersionWindow.class).error("Can't use commandline argument: [{}] as default directory ");
			}
		}

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
		frame.setBounds(200, 200, 700, 500);
	}

}
