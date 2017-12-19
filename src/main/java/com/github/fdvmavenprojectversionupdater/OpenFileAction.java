package com.github.fdvmavenprojectversionupdater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenFileAction implements ActionListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private JFileChooser fileChooser = new JFileChooser();

	private final ConsumerList<File> selectedFileListeners = new ConsumerList<File>();

	@Override
	public void actionPerformed(ActionEvent e) {
		if (fileChooser.showDialog(null, null) == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = fileChooser.getSelectedFile();
			logger.info("OpenFileDialog approve file: [{}]", selectedFile);
			selectedFileListeners.fire(selectedFile);
		}
	}

	public ConsumerList<File> getSelectedFileListeners() {
		return selectedFileListeners;
	}

	public void setCurrentDirectory(File file) {
		this.fileChooser.setCurrentDirectory(file);
	}

	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}