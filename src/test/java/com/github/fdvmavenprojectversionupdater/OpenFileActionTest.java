package com.github.fdvmavenprojectversionupdater;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.swing.JFileChooser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.typemarkup.Behavior;

@RunWith(MockitoJUnitRunner.class)
public class OpenFileActionTest extends OpenFileActionTestBase {

	@Test
	@Behavior("Если пользователь выбрал файл, то генерируется событие")
	public void test$fireEventAfterApproveOption() {
		Mockito.when(fileChooser.showDialog(Mockito.any(), Mockito.any())).thenReturn(JFileChooser.APPROVE_OPTION);
		Mockito.when(fileChooser.getSelectedFile()).thenReturn(new File("g:\\file\\pom.xml"));

		action.getSelectedFileListeners().register(file -> logMessage(file.getAbsolutePath()));

		assertEquals("", callLog());

		action.actionPerformed(null);

		assertEquals("MSG:g:\\file\\pom.xml", callLog());
	}

	@Test
	@Behavior("Если пользователь отказался от выбора, то событие не возникает")
	public void test$noFireEventIfNoApproveOption() {
		Mockito.when(fileChooser.showDialog(Mockito.any(), Mockito.any())).thenReturn(JFileChooser.CANCEL_OPTION);

		action.getSelectedFileListeners().register(file -> logMessage(file.getAbsolutePath()));

		assertEquals("", callLog());

		action.actionPerformed(null);

		assertEquals("", callLog());
	}

	@Test
	@Behavior("Установка имени директории делегируется fileChooser")
	public void test$setCurrentDirIsDelegated() {
		final ArgumentCaptor<File> currentFileCaptor = ArgumentCaptor.forClass(File.class);
		Mockito.doNothing().when(fileChooser).setCurrentDirectory(currentFileCaptor.capture());

		assertEquals("", callLog());

		action.setCurrentDirectory(new File("g:\\some\\path"));

		assertEquals("g:\\some\\path", currentFileCaptor.getValue().toString());
	}

	@Test
	@Behavior("При открытии файла, формируется сообщение в лог, с уровнем инфо")
	public void test$selectedFileNameLogged() {
		Mockito.when(fileChooser.showDialog(Mockito.any(), Mockito.any())).thenReturn(JFileChooser.APPROVE_OPTION);
		Mockito.when(fileChooser.getSelectedFile()).thenReturn(new File("g:\\myDir\\myPom.xml"));

		action.actionPerformed(null);

		Mockito.verify(logger).info("OpenFileDialog approve file: [{}]", new File("g:\\myDir\\myPom.xml"));

	}

}
