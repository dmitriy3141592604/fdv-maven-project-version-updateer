package com.github.fdv.busBasedWindow;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fdv.borders.MarginBorderDecorator;
import com.github.fdv.bus.Bus;
import com.github.fdv.models.ComponentsTable;
import com.github.fdv.panels.ConfigurableLeftFlowLayoutPanel;
import com.github.fdvmavenprojectversionupdater.GroupPanelFromComponentsTableBuilder;

public class BusMessageSender extends JFrame {

	private static final long serialVersionUID = -6510495350918613603L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String... args) {
		new BusMessageSender(new Bus()).configure().setVisible(true);
	}

	private final Bus bus;

	public BusMessageSender(Bus bus) {
		this.bus = bus;
		setTitle(getClass().getSimpleName());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(772, 33, 371, 708);
	}

	public BusMessageSender configure() {
		{
			addComponentListener(new ComponentAdapter() {

				@Override
				public void componentMoved(ComponentEvent e) {
					super.componentMoved(e);
					logger.debug("Moved: {}", e.getComponent().getBounds());
				}

			}

			);
		}
		{
			final ComponentsTable componentsTable = new ComponentsTable();
			final List<String> topicNames = new ArrayList<String>();
			{
				topicNames.add("newShortPomFileName");
				topicNames.add("newFullPomFileName");
			}

			for (final String topicName : topicNames) {
				final JLabel jLabel = new JLabel(topicName);
				final JTextField tf = new JTextField("");
				tf.setColumns(10);
				final JButton button = new JButton(">>");
				button.addActionListener(e -> bus.post(topicName, new StringEvent(tf.getText())));
				componentsTable.add(jLabel, tf, button);
			}
			add(align(componentsTable));
		}
		return this;
	}

	private Component align(ComponentsTable componentsTable) {
		final JPanel panel = new GroupPanelFromComponentsTableBuilder().componentsTable(componentsTable).build();
		return new ConfigurableLeftFlowLayoutPanel(new MarginBorderDecorator().decorate(panel));
	}

	public BusMessageSender packThis() {
		super.pack();
		return this;
	}

}
