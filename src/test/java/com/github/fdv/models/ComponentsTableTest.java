package com.github.fdv.models;

import static org.junit.Assert.assertEquals;

import javax.swing.JLabel;

import org.junit.Before;
import org.junit.Test;

public class ComponentsTableTest {

	private ComponentsTable componentsTable;

	@Before
	public final void setUpComponentsTableBase() {
		componentsTable = new ComponentsTable();
	}

	@Test
	public void test$autoUpdateRowCount() {

		componentsTable.add(new JLabel(), new JLabel());

		assertEquals(1, componentsTable.getRowsCount());

		componentsTable.add(new JLabel(), new JLabel());

		assertEquals(2, componentsTable.getRowsCount());
	}

	@Test(expected = IllegalStateException.class)
	public void test$getRowsCountBeforeInitialization() {
		componentsTable.getRowsCount();
	}

	@Test
	public void test$columnsCount() {

		componentsTable.add(new JLabel(), new JLabel(), new JLabel());

		assertEquals(3, componentsTable.getColumnsCount());

		componentsTable.add(new JLabel(), new JLabel(), new JLabel());

		assertEquals(3, componentsTable.getColumnsCount());

	}

	@Test(expected = IllegalStateException.class)
	public void test$requestColumnsCountBeforeInitialization() {
		componentsTable.getColumnsCount();
	}

	@Test(expected = IllegalArgumentException.class)
	public void test$columnsCountInRowsNotEqual() {
		componentsTable.add(new JLabel());
		componentsTable.add(new JLabel(), new JLabel());
	}

}
