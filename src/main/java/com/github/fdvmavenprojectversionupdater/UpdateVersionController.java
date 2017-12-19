package com.github.fdvmavenprojectversionupdater;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateVersionController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> args;

	private FileClassificator fileClassificator = new FileClassificator();

	private final List<Consumer<File>> searchDirectoryListeners = new ArrayList<>();

	private final List<Consumer<File>> sourceFileListeners = new ArrayList<>();

	private File selectedFile;

	public void setArgs(String[] args) {
		this.args = Arrays.asList(args);
		if (args.length > 0 || System.getProperty("user.dir") != null) {
			setFile(new File(args.length > 0 ? args[0] : System.getProperty("user.dir")));
		}
	}

	public List<String> getArgs() {
		return args;
	}

	public void setFile(File file) {
		final boolean directory = fileClassificator.isDirectory(file);
		final boolean isExists = fileClassificator.isExists(file);
		if (!isExists) {
			// nothing
		} else if (directory) {
			searchDirectoryListeners.forEach(c -> c.accept(file));
			logger.info("Process searchDirectory: [{}]", file);
		} else if (fileClassificator.isFile(file)) {
			selectedFile = file;
			sourceFileListeners.forEach(c -> c.accept(file));
			searchDirectoryListeners.forEach(c -> c.accept(file.getParentFile()));
			logger.info("Process file: [{}] and searchDirectory: [{}]", file, file.getParentFile());
		}
	}

	public void addSearchDirectoryListener(Consumer<File> searchDirectoryConsumer) {
		searchDirectoryListeners.add(searchDirectoryConsumer);
	}

	public void setFileClassificator(FileClassificator fileClassificator) {
		this.fileClassificator = fileClassificator;
	}

	public void addSourceFileListener(Consumer<File> sourceFileConsumer) {
		sourceFileListeners.add(sourceFileConsumer);
	}

	public File getSelectedFile() {
		return selectedFile;
	}

}
