package com.github.fdv.buttons;

import java.util.function.Consumer;

import javax.swing.JButton;

public class ConfigurableButton extends JButton {

	private static final long serialVersionUID = 2213724754523257978L;

	public ConfigurableButton(String label, Consumer<ConfigurableButton> configurer) {
		super(label);
		configurer.accept(this);
	}

}
