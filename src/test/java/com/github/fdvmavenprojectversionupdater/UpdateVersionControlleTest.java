package com.github.fdvmavenprojectversionupdater;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateVersionControlleTest extends UpdateVersionControllerTestBase {

	@Test
	public void test$argsWithDirectory() {

		model.getSearchDirectoryListeners().register(file -> addMessage("D", file.getAbsolutePath()));

		Mockito.when(fileClassificator.isDirectory(Mockito.any())).thenReturn(true);
		Mockito.when(fileClassificator.isFile(Mockito.any())).thenReturn(false);
		Mockito.when(fileClassificator.isExists(Mockito.any())).thenReturn(true);

		assertEquals("", callLog());

		model.setArgs(new String[] { "c:\\directoryName" });

		assertEquals("D:c:\\directoryName", callLog());
	}

	@Test
	public void test$argsWithFileName() {
		model.getSourceFileNameListeners().register(file -> addMessage("F", file.getAbsolutePath()));
		model.getSearchDirectoryListeners().register(file -> addMessage("D", file.getAbsolutePath()));

		Mockito.when(fileClassificator.isDirectory(Mockito.any())).thenReturn(false);
		Mockito.when(fileClassificator.isFile(Mockito.any())).thenReturn(true);
		Mockito.when(fileClassificator.isExists(Mockito.any())).thenReturn(true);

		assertEquals("", callLog());

		model.setArgs(new String[] { "c:\\dirName\\pom.xml" });

		assertEquals("F:c:\\dirName\\pom.xmlD:c:\\dirName", callLog());
	}

	@Test
	public void test$simpleFileNameWithCurrentDir() {
		System.setProperty("user.dir", "g:\\user\\dir");
		model.getSourceFileNameListeners().register(file -> addMessage("F", file.getAbsolutePath()));

		Mockito.when(fileClassificator.isExists(Mockito.any())).thenReturn(true);
		Mockito.when(fileClassificator.isFile(Matchers.argThat(new ArgumentMatcher<File>() {

			@Override
			public boolean matches(Object argument) {
				return "pom.xml".equals(((File) argument).getName());
			}
		}))).thenReturn(true);

		Mockito.when(fileClassificator.isDirectory(Matchers.argThat(new ArgumentMatcher<File>() {

			@Override
			public boolean matches(Object argument) {
				return "g:\\user\\dir".equals(((File) argument).getName());
			}
		}))).thenReturn(true);

		model.setArgs(new String[] { "pom.xml" });

		assertEquals("F:g:\\user\\dir\\pom.xml", callLog());

	}

	@Test
	public void test$noArgument() {
		System.setProperty("user.dir", "g:\\other\\dir");

		model.getSearchDirectoryListeners().register(file -> addMessage("D", file.getAbsolutePath()));

		Mockito.when(fileClassificator.isExists(Mockito.any())).thenReturn(true);
		Mockito.when(fileClassificator.isDirectory(Mockito.any())).thenReturn(true);
		Mockito.when(fileClassificator.isFile(Mockito.any())).thenReturn(false);

		assertEquals("", callLog());

		model.setArgs(new String[] {});

		assertEquals("D:g:\\other\\dir", callLog());

	}

}
