package com.github.fdvmavenprojectversionupdater;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileReader;
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

	private final JFrame frame = new JFrame();

	private final Consumer<String> messagesProcessor;

	private final VersionHolder versionHolder = new VersionHolder();

	private final JLabel showSelectedFileLabel = new JLabel("Файл не выбран");

	private final JLabel showExtractedVersionLabel = new JLabel("Файл не выбран");

	private final JTextArea loggingTextArea = new JTextArea(5, 30);

	private final OpenFileAction openFileAction = new OpenFileAction();

	private final SaveFileAction saveFileAction = new SaveFileAction();

	private final ExitAction exitAction = new ExitAction();

	private final PackAction packAction = new PackAction();

	protected UpdateVersionWindowBase(String[] args) {

		messagesProcessor = newMessage -> {
			loggingTextArea.append(newMessage);
			loggingTextArea.append("\n");
		};

		controller.getSearchDirectoryListeners().register(openFileAction::setCurrentDirectory);
		controller.getSearchDirectoryListeners().register(this::logNewSearchDirectory);

		controller.getSourceFileNameListeners().register(this::logNewSourceFile);
		controller.getSourceFileNameListeners().register(this::extractVersionFromSourceFile);
		controller.getSourceFileNameListeners().register(this::updateSelectedFileLabel);

		controller.getSourceFileNameListeners().register(file -> messagesProcessor.accept("Открыт файл: [{" + file.getAbsolutePath() + "}]"));

		versionHolder.addValueChangeListener(showExtractedVersionLabel::setText);
		versionHolder.addValueChangeListener(saveFileAction::setVersion);

		openFileAction.getSelectedFileListeners().register(controller::setFile);
		openFileAction.getSelectedFileListeners().register(saveFileAction::setSelectedFile);

		packAction.getPackAcctionListeners().register(frame::pack);

		controller.setArgs(args);
	}

	private void updateSelectedFileLabel(File file) {
		showSelectedFileLabel.setToolTipText(file.getAbsolutePath());
		showSelectedFileLabel.setText(file.getAbsolutePath().replaceFirst(".*[/\\\\]([^/\\\\]+)$", "$1"));
	}

	private void extractVersionFromSourceFile(File file) {
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
	}

	private void logNewSourceFile(File file) {
		logger.info("New source file: [{}]", file.getAbsolutePath());
	}

	private void logNewSearchDirectory(File file) {
		logger.info("New search directory: [{}]", file.getAbsolutePath());
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
