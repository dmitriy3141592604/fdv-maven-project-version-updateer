package com.github.fdvmavenprojectversionupdater;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateVersionWindowBase implements Supplier<JFrame> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private final UpdateVersionController controller = new UpdateVersionController();

	protected JFrame frame = new JFrame();

	protected Consumer<String> messagesProcessor;

	private final VersionHolder versionHolder = new VersionHolder();

	final JFileChooser fileChooser = new JFileChooser();

	final JLabel showSelectedFileLabel = new JLabel("Файл не выбран");

	final JLabel showExtractedVersionLabel = new JLabel("Файл не выбран");

	private final JTextArea loggingTextArea = new JTextArea(5, 30);

	private final ActionListener openFileAction = e -> {
		final int selectFileResult = fileChooser.showDialog(frame, "Open");
		if (selectFileResult == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = fileChooser.getSelectedFile();
			logger.debug("File: {} selected", selectedFile.toString());
			controller.setFile(selectedFile);
		}
	};

	private final ActionListener saveFileAction = e -> {
		try {
			final NodeListProcessor processor = new NodeListProcessor();
			processor.registerPrefixToNamespace("mv", "http://maven.apache.org/POM/4.0.0");
			try (FileReader reader = new FileReader(controller.getSelectedFile())) {
				processor.parse(reader);
			}
			processor.forNode("/mv:project/mv:version/text()", node -> {
				node.setNodeValue(versionHolder.toString());
			});

			try (FileWriter writer = new FileWriter(controller.getSelectedFile())) {
				processor.write(writer);
			}

			messagesProcessor.accept("File: [" + controller.getSelectedFile() + "] сохранен с версией: [" + versionHolder.toString() + "]");
		} catch (

		final Exception exception) {
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			}
			throw new RuntimeException(exception);
		}
	};

	private final ActionListener exitAction = e -> {
		System.exit(0);
	};

	private final ActionListener packAction = e -> frame.pack();

	protected UpdateVersionWindowBase(String[] args) {
		messagesProcessor = newMessage -> {
			loggingTextArea.append(newMessage);
			loggingTextArea.append("\n");
		};

		controller.getSearchDirectoryListeners().register(file -> fileChooser.setCurrentDirectory(file));
		controller.getSearchDirectoryListeners().register(file -> logger.info("New search directory: [{}]", file.getAbsolutePath()));

		controller.getSourceFileNameListeners().register(file -> logger.info("New source file: [{}]", file.getAbsolutePath()));
		controller.getSourceFileNameListeners().register(file -> {
			try {
				final NodeListProcessor processor = new NodeListProcessor();
				processor.registerPrefixToNamespace("mv", "http://maven.apache.org/POM/4.0.0");
				processor.parse(new FileReader(file));
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
		});
		controller.getSourceFileNameListeners().register(file -> {
			showSelectedFileLabel.setToolTipText(file.getAbsolutePath());
			showSelectedFileLabel.setText(file.getAbsolutePath().replaceFirst(".*[/\\\\]([^/\\\\]+)$", "$1"));
		});

		controller.getSourceFileNameListeners().register(file -> messagesProcessor.accept("Открыт файл: [{" + file.getAbsolutePath() + "}]"));

		versionHolder.addValueChangeListener(showExtractedVersionLabel::setText);

		controller.setArgs(args);
	}

	@Override
	public JFrame get() {
		configureFrame();
		initializeMenu();
		alignComponents();
		return frame;
	}

	protected void initializeMenu() {
		frame.setJMenuBar(new ConfigurableMenuBar(menuBar -> {

			menuBar.add("File", menu -> {
				menu.add("Open", openFileAction, "control O");
				menu.add("Save", saveFileAction, "control S");
				menu.addSeparator();
				menu.add("Exit", exitAction, "control Q");
			});

			menuBar.add("View", viewMenu -> {
				viewMenu.add("Pack", packAction, "control shift P");
			});

		}));

	}

	protected void alignComponents() {
		{
			final List<List<JComponent>> componentsTable = new ArrayList<>();

			componentsTable.add(Arrays.asList(new JLabel("Имя файла"), showSelectedFileLabel));
			componentsTable.add(Arrays.asList(new JLabel("Текущая версия"), showExtractedVersionLabel));

			frame.add(createLeftFlowLayoutPanel(alignWithGroupLayout(componentsTable)));
		}
		{

			final List<JComponent> actionButtons = new ArrayList<>();
			actionButtons.add(new ActionButton("Увеличить мажорную версию", e -> versionHolder.incrementMajor()));
			actionButtons.add(new ActionButton("Уменьшить мажорную версию", e -> versionHolder.increment(0, -1)));
			actionButtons.add(new ActionButton("Увеличить минорную версию", e -> versionHolder.incrementMinor()));
			actionButtons.add(new ActionButton("Уменьшить минорную версию", e -> versionHolder.increment(1, -1)));
			actionButtons.add(new ActionButton("Увеличить версию билда", e -> versionHolder.incrementBuild()));
			actionButtons.add(new ActionButton("Уменьшить версию билда", e -> versionHolder.increment(2, -1)));
			actionButtons.add(new ActionButton("Пометить SNAPSHOT", e -> versionHolder.toSnapshot()));
			actionButtons.add(new ActionButton("Пометить RC", e -> versionHolder.toRC()));
			actionButtons.add(new ActionButton("Оставить только версию", e -> versionHolder.onlyVersion()));

			final JPanel actionsBox = new JPanel(new GridLayout(actionButtons.size(), 1, 1, 1));
			actionButtons.forEach(b -> actionsBox.add(addMargin(b)));
			frame.add(new JScrollPane(createLeftFlowLayoutPanel(actionsBox)), BorderLayout.WEST);
		}
		{

			frame.add(new JScrollPane(loggingTextArea), BorderLayout.SOUTH);
			loggingTextArea.setEditable(false);
		}
	}

	private JPanel alignWithGroupLayout(final List<List<JComponent>> componentsTable) {
		final int rowsCount = componentsTable.size();
		int columnsCount = -1;
		for (final List<JComponent> cmps : componentsTable) {
			if (columnsCount == -1) {
				columnsCount = cmps.size();
			} else {
				if (columnsCount != cmps.size()) {
					throw new IllegalStateException("");
				}
			}
			for (final JComponent cmp : cmps) {
				addMargin(cmp);
			}
		}

		final JPanel propertyBox = new JPanel();
		final GroupLayout gl = new GroupLayout(propertyBox);
		propertyBox.setLayout(gl);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		final SequentialGroup hGroup = gl.createSequentialGroup();
		final SequentialGroup vGroup = gl.createSequentialGroup();

		for (int columnIndex = 0; columnIndex < columnsCount; ++columnIndex) {
			final ParallelGroup pg = gl.createParallelGroup();
			for (int rowIndex = 0; rowIndex < rowsCount; ++rowIndex) {
				pg.addComponent(componentsTable.get(rowIndex).get(columnIndex));
			}
			hGroup.addGroup(pg);
		}

		for (int rowIndex = 0; rowIndex < rowsCount; ++rowIndex) {
			final ParallelGroup pg = gl.createParallelGroup(Alignment.BASELINE);
			for (int columnIndex = 0; columnIndex < columnsCount; ++columnIndex) {
				pg.addComponent(componentsTable.get(rowIndex).get(columnIndex));
			}
			vGroup.addGroup(pg);
		}

		gl.setHorizontalGroup(hGroup);
		gl.setVerticalGroup(vGroup);
		return propertyBox;
	}

	private JPanel createLeftFlowLayoutPanel(JPanel propertyBox) {
		final JPanel bx = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addBorder(propertyBox, bx);
		return bx;
	}

	private void addBorder(JPanel propertyBox, final JPanel bx) {
		bx.add(propertyBox);
		bx.setBorder(BorderFactory.createCompoundBorder(bx.getBorder(), BorderFactory.createEtchedBorder()));
	}

	private Component addMargin(JComponent jLabel) {
		final Border border = jLabel.getBorder();
		final EmptyBorder margin = new EmptyBorder(2, 2, 2, 2);
		jLabel.setBorder(new CompoundBorder(border, margin));
		return jLabel;
	}

	protected void configureFrame() {
		frame.setTitle("Обновление версии pom.xml");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(200, 200, 700, 500);
	}

}
