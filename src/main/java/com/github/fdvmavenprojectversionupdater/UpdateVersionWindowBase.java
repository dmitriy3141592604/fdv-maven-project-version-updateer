package com.github.fdvmavenprojectversionupdater;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateVersionWindowBase {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected JFrame frame = new JFrame();

	protected File selectedFile;

	protected Consumer<String> fileNameUpdater;

	protected Consumer<String> messagesProcessor;

	private Runnable versionUpdater;

	private final VersionHolder versionHolder = new VersionHolder() {

		{
			addValueChangeListener(newVersion -> versionUpdater.run());
		}

	};

	protected Runnable processNewFileAction = () -> {
		try {
			final NodeListProcessor processor = new NodeListProcessor();
			processor.registerPrefixToNamespace("mv", "http://maven.apache.org/POM/4.0.0");
			processor.parse(new FileReader(selectedFile));
			processor.forNode("/mv:project/mv:version/text()", node -> {
				versionHolder.setVersion(node.getNodeValue());
			});
		} catch (final Exception e) {
			messagesProcessor.accept(e.getMessage());
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}
	};

	protected File searchDir;

	private final ActionListener openFileAction = e -> {
		logger.debug("Start opening dialog");
		final JFileChooser c = new JFileChooser();
		if (searchDir == null) {
			searchDir = new File(System.getProperty("user.dir"));
		}
		if (searchDir.exists() && searchDir.isDirectory()) {
			c.setCurrentDirectory(searchDir);
		}
		final int selectFileResult = c.showDialog(frame, "Open");
		if (selectFileResult == JFileChooser.APPROVE_OPTION) {
			selectedFile = c.getSelectedFile();
			processSelectedFile();
		}
	};

	protected void processSelectedFile() {
		searchDir = new File(selectedFile.getParent());
		logger.debug("File: {} selected", selectedFile.toString());
		fileNameUpdater.accept(selectedFile.toString());
		processNewFileAction.run();
	}

	private final ActionListener saveFileAction = e -> {
		try {
			final NodeListProcessor processor = new NodeListProcessor();
			processor.registerPrefixToNamespace("mv", "http://maven.apache.org/POM/4.0.0");
			try (FileReader reader = new FileReader(selectedFile)) {
				processor.parse(reader);
			}
			processor.forNode("/mv:project/mv:version/text()", node -> {
				node.setNodeValue(versionHolder.toString());
			});
			versionUpdater.run();
			try (FileWriter writer = new FileWriter(selectedFile)) {
				processor.write(writer);
			}

			messagesProcessor.accept("Файл " + selectedFile.toString() + "записан");

		} catch (final Exception exception) {
			messagesProcessor.accept(exception.getMessage());
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			}
			throw new RuntimeException(exception);
		}
	};

	private final ActionListener exitAction = e -> {
		System.exit(0);
	};

	protected void initializaComponents() {
		initializeMenu();
	}

	private void initializeMenu() {
		final JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		{
			final JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			{
				final JMenuItem openMenuItem = new JMenuItem("Open");
				fileMenu.add(openMenuItem);
				openMenuItem.addActionListener(openFileAction);
				openMenuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));

			}
			{
				final JMenuItem saveMenuItem = new JMenuItem("Save");
				fileMenu.add(saveMenuItem);
				saveMenuItem.addActionListener(saveFileAction);
				saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
			}
			fileMenu.addSeparator();
			{
				final JMenuItem exitMenu = new JMenuItem("Exit");
				fileMenu.add(exitMenu);
				exitMenu.addActionListener(exitAction);
				exitMenu.setAccelerator(KeyStroke.getKeyStroke("control Q"));
			}
		}
	}

	protected void alignComponents() {
		final Box basePane = Box.createVerticalBox();
		frame.add(basePane);
		{
			final Box fileNamePanel = Box.createHorizontalBox();
			basePane.add(fileNamePanel);
			fileNamePanel.add(new JLabel("Имя файла:"));
			fileNamePanel.add(Box.createHorizontalStrut(7));
			final JLabel fileNameLabel = new JLabel("Файл не выбран");
			fileNamePanel.add(fileNameLabel);
			fileNameUpdater = newText -> fileNameLabel.setText(newText);
			fileNamePanel.add(Box.createHorizontalStrut(10));
		}
		{
			final Box versionPanel = Box.createHorizontalBox();
			basePane.add(versionPanel);

			versionPanel.add(new JLabel("Текущая версия: "));
			versionPanel.add(Box.createHorizontalStrut(7));
			final JLabel versionLabel = new JLabel();
			versionPanel.add(versionLabel);
			versionUpdater = () -> versionLabel.setText(versionHolder.toString());

		}
		{
			final Box actionsBox = Box.createVerticalBox();
			frame.add(actionsBox, BorderLayout.WEST);

			class ActionButton extends JButton {

				private static final long serialVersionUID = 1L;

				public ActionButton(String label, ActionListener al) {
					setText(label);
					addActionListener(al);
				};
			}
			actionsBox.add(new ActionButton("Увеличить мажорную версию", e -> versionHolder.incrementMajor()));
			actionsBox.add(new ActionButton("Уменьшить мажорную версию", e -> versionHolder.increment(0, -1)));
			actionsBox.add(new ActionButton("Увеличить минорную версию", e -> versionHolder.incrementMinor()));
			actionsBox.add(new ActionButton("Уменьшить минорную версию", e -> versionHolder.increment(1, -1)));
			actionsBox.add(new ActionButton("Увеличить версию билда", e -> versionHolder.incrementBuild()));
			actionsBox.add(new ActionButton("Уменьшить версию билда", e -> versionHolder.increment(2, -1)));
			actionsBox.add(new ActionButton("Пометить SNAPSHOT", e -> versionHolder.toSnapshot()));
			actionsBox.add(new ActionButton("Пометить RC", e -> versionHolder.toRC()));
			actionsBox.add(new ActionButton("Оставить только версию", e -> versionHolder.onlyVersion()));
		}
		{
			final JTextArea jTextArea = new JTextArea(5, 30);
			frame.add(jTextArea, BorderLayout.SOUTH);
			jTextArea.setEditable(false);
			messagesProcessor = newMessage -> {
				jTextArea.append(newMessage);
				jTextArea.append("\n");
			};
		}
	}

}
