package com.github.fdvmavenprojectversionupdater;

import javax.swing.JFileChooser;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

public abstract class OpenFileActionTestBase extends LoggableTestBase {

	@Mock
	public JFileChooser fileChooser;

	@Mock
	public Logger logger;

	@InjectMocks
	public OpenFileAction action;

	@Before
	public void setUpOpenFileActionTestBase() {

	}

}
