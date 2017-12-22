package com.github.fdv.labels;

import java.util.function.Consumer;

import javax.swing.JLabel;

public class ConfigurableLabel extends JLabel {

	private static final long serialVersionUID = -3954953575443982113L;

	public ConfigurableLabel(String label, Consumer<ConfigurableLabel> configurer) {
		super(label);
		configurer.accept(this);
	}

}
