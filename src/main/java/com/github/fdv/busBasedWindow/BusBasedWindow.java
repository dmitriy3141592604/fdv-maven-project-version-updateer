package com.github.fdv.busBasedWindow;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fdv.borders.MarginBorderDecorator;
import com.github.fdv.bus.Bus;
import com.github.fdv.labels.ConfigurableLabel;
import com.github.fdv.menus.ConfigurableMenuBar;
import com.github.fdv.models.ComponentsTable;
import com.github.fdv.panels.ConfigurableLeftFlowLayoutPanel;
import com.github.fdvmavenprojectversionupdater.GroupPanelFromComponentsTableBuilder;

public class BusBasedWindow extends JFrame {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = -3028713956990097930L;

	public static void main(String... args) {
		final Bus bus = new Bus();
		new BusBasedWindow(bus).setVisible(true);
		new BusMessageSender(bus).configure().setVisible(true);

	}

	public static class TitleBuilder {

		private String fileName;

		public TitleBuilder setFileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public String build() {

			return String.format("Выбранный файл: %s", fileName);
		}
	}

	private final Bus bus;

	public BusBasedWindow(Bus bus) {
		this.bus = bus;
		buildContent();
		setBounds(200, 300, 300, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Maket");
	}

	private void buildContent() {
		{

			final TitleBuilder titleBuilder = new TitleBuilder();

			bus.subscribe("newFullPomFileName", StringEvent.class, se -> {
				setTitle(titleBuilder.setFileName(se.getText()).build());
			});
		}
		{
			setJMenuBar(new ConfigurableMenuBar(menuBar -> {
				menuBar.add("File", menu -> {
					menu.add("Open", busAction("openAction"), "control O");
					menu.add("Save", busAction("saveAction"), "control S");
					menu.addSeparator();
					menu.add("Exit", busAction("exitAction"), "control Q");
				});
				menuBar.add("View", menu -> {
					menu.add("Pack", busAction("packAction"), "control P");
				});
				menuBar.add("Help", menu -> {
					menu.add("About", busAction("aboutAction"), "control H");
				});
			}));
		}
		{
			final ComponentsTable ct = new ComponentsTable();
			ct.add(new JLabel("Имя файла"), new ConfigurableLabel("Файл не выбран", label -> {
				bus.subscribe("newShortPomFileName", StringEvent.class, se -> label.setText(se.getText()));
				bus.subscribe("newFullPomFileName", StringEvent.class, se -> label.setToolTipText(se.getText()));
			}));
			ct.add(new JLabel("Версия"), new ConfigurableLabel("Файл не выбран", label -> {
				bus.subscribe("newPomFileVersion", StringEvent.class, se -> label.setText(se.getText()));
			}));

			add(align(ct));
		}
	}

	private Component align(ComponentsTable componentsTable) {
		final JPanel panel = new GroupPanelFromComponentsTableBuilder().componentsTable(componentsTable).build();
		return new ConfigurableLeftFlowLayoutPanel(new MarginBorderDecorator().decorate(panel));
	}

	private ActionListener busAction(String string) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bus.post(string, e);
			}

		};
	}

}
