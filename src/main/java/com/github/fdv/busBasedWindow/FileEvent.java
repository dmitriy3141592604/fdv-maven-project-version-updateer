package com.github.fdv.busBasedWindow;

import java.io.File;

public class FileEvent extends SimpleBusEvent<File> {

	public FileEvent(File file) {
		super(file);
	};
}
