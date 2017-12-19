package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SaveFileAction implements ActionListener {

	private File selectedFile;

	private String version;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			final NodeListProcessor processor = new NodeListProcessor();
			processor.registerPrefixToNamespace("mv", "http://maven.apache.org/POM/4.0.0");
			try (FileReader reader = new FileReader(selectedFile)) {
				processor.parse(reader);
			}
			processor.forNode("/mv:project/mv:version/text()", node -> {
				node.setNodeValue(version);
			});

			try (FileWriter writer = new FileWriter(selectedFile)) {
				processor.write(writer);
			}

			// messagesProcessor.accept("File: [" + controller.getSelectedFile() + "] сохранен с версией: [" + versionHolder.toString() + "]");
		} catch (

		final Exception exception) {
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			}
			throw new RuntimeException(exception);
		}
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}