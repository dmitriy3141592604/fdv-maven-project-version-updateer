package com.github.fdvmavenprojectversionupdater;

import java.io.File;

public class FileClassificator {

	public boolean isFile(File file) {
		return file.isFile();
	}

	public boolean isDirectory(File file) {
		return file.isDirectory();
	}

	public boolean isExists(File file) {
		return file.exists();
	}

}
