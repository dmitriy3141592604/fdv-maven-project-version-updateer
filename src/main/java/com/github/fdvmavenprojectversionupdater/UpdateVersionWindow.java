package com.github.fdvmavenprojectversionupdater;

public class UpdateVersionWindow extends UpdateVersionWindowBase {

	public static void main(String... args) {
		new UpdateVersionWindow(args).get().setVisible(true);
	}

	public UpdateVersionWindow(String[] args) {
		super(args);
	}

}
