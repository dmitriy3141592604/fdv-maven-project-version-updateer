package com.github.fdvmavenprojectversionupdater;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateVersionController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> args;

	private FileClassificator fileClassificator = new FileClassificator();

	private final ConsumerList<File> searchDirectoryListeners = new ConsumerList<>();

	private final ConsumerList<File> sourceFileListeners = new ConsumerList<>();

	private File selectedFile;

	public void setArgs(String[] args) {
		this.args = Arrays.asList(args);
		if (args.length > 0 || System.getProperty("user.dir") != null) {
			setFile(new File(args.length > 0 ? args[0] : System.getProperty("user.dir")));
		}
	}

	public void setFile(File file) {
		final boolean directory = fileClassificator.isDirectory(file);
		final boolean isExists = fileClassificator.isExists(file);
		if (!isExists) {
			// nothing
		} else if (directory) {
			searchDirectoryListeners.fire(file);
			logger.info("Process searchDirectory: [{}]", file);
		} else if (fileClassificator.isFile(file)) {
			selectedFile = file;
			sourceFileListeners.fire(file);
			searchDirectoryListeners.fire(file.getParentFile());
			logger.info("Process file: [{}] and searchDirectory: [{}]", file, file.getParentFile());
		}
	}

	public ConsumerList<File> getSearchDirectoryListeners() {
		return searchDirectoryListeners;
	}

	public void setFileClassificator(FileClassificator fileClassificator) {
		this.fileClassificator = fileClassificator;
	}

	public ConsumerList<File> getSourceFileNameListeners() {
		return sourceFileListeners;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public List<String> getArgs() {
		return args;
	}

}
