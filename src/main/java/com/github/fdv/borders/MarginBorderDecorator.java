package com.github.fdv.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class MarginBorderDecorator {

	private int topMargin = 2;

	private int leftMargin = 2;

	private int bottomMargin = 2;

	private int rightMargin = 2;

	public JComponent decorate(Component component) {
		if (!(component instanceof JComponent)) {
			final String format = "Маргин можно установить только для производных класса: [%s]. Переданый объект имеет класс: [%s]";
			final String message = String.format(format, JComponent.class.toString(), component.getClass().toString());
			throw new IllegalArgumentException(message);
		}
		final JComponent jComponent = (JComponent) component;
		final Border border = jComponent.getBorder();
		final EmptyBorder margin = new EmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin);
		jComponent.setBorder(new CompoundBorder(border, margin));
		return jComponent;
	}

	public MarginBorderDecorator margin(int top, int left, int bottom, int right) {
		topMargin = top;
		leftMargin = left;
		bottomMargin = bottom;
		rightMargin = right;
		return this;
	}

	public static void main(String... args) {
		final boolean useColors = false;
		final JFrame jFrame = new JFrame(MarginBorderDecorator.class.toString());
		final JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		if (useColors) {
			jPanel.setBackground(Color.BLUE);
		}
		final MarginBorderDecorator marginBorderDecorator = new MarginBorderDecorator().margin(10, 20, 30, 40);
		final JLabel jComponent = new JLabel("my label");
		if (useColors) {
			jComponent.setBackground(Color.GREEN);
		}
		jComponent.setBorder(BorderFactory.createRaisedBevelBorder());
		jPanel.add(marginBorderDecorator.decorate(jComponent));
		jFrame.add(jPanel);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setBounds(300, 200, 300, 200);
		jFrame.setVisible(true);
	}

}
