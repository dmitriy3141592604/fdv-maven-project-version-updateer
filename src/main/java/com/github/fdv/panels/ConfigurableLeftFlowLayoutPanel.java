package com.github.fdv.panels;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

public class ConfigurableLeftFlowLayoutPanel extends JPanel {

	private static final long serialVersionUID = -8771981624720930457L;

	public ConfigurableLeftFlowLayoutPanel(Component component) {
		this(component, null);
	}

	public ConfigurableLeftFlowLayoutPanel(Component component, Consumer<ConfigurableLeftFlowLayoutPanel> c) {
		super(new FlowLayout(FlowLayout.LEFT));
		add(component);
		if (c != null) {
			c.accept(this);
		}
	}

}
