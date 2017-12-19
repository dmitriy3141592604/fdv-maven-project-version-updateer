package com.github.fdv.borders;

import static org.junit.Assert.assertEquals;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.junit.Test;

public class MarginBorderDecoratorTest {

	@Test
	public void test$defaultDecoration() {
		final JPanel wrappedPanel = new JPanel();
		wrappedPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		final JComponent decorated = new MarginBorderDecorator().decorate(wrappedPanel);

		final CompoundBorder cb = (CompoundBorder) decorated.getBorder();
		final EmptyBorder eb = (EmptyBorder) cb.getInsideBorder();
		final BevelBorder bb = (BevelBorder) cb.getOutsideBorder();
		final Insets bi = eb.getBorderInsets();
		assertEquals("" + BevelBorder.RAISED + " 2 2 2 2", String.format("%d %d %d %d %d", bb.getBevelType(), bi.top, bi.left, bi.bottom, bi.right));
	}

	@Test
	public void test$margins() {
		final JPanel wrappedPanel = new JPanel();
		wrappedPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		final MarginBorderDecorator marginBorderDecorator = new MarginBorderDecorator().margin(1, 2, 3, 4);
		marginBorderDecorator.decorate(wrappedPanel);
		final Insets ib = ((EmptyBorder) ((CompoundBorder) wrappedPanel.getBorder()).getInsideBorder()).getBorderInsets();

		assertEquals("1,2,3,4", String.format("%d,%d,%d,%d", ib.top, ib.left, ib.bottom, ib.right));
	}

}
