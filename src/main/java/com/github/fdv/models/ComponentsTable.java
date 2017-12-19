package com.github.fdv.models;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ComponentsTable {

	private final List<List<Component>> table = new ArrayList<>();

	private int rowsCount = -1;

	private int columnsCount = -1;

	public void add(Component... components) {
		table.add(Arrays.asList(components));
		if (rowsCount == -1) {
			rowsCount = 0;
		}
		rowsCount++;

		if (columnsCount == -1) {
			columnsCount = components.length;
		} else {
			if (columnsCount != components.length) {
				throw new IllegalArgumentException("В строке с номером: " + rowsCount + " ожидается: " + columnsCount + " столбцов");
			}
		}
	}

	public int getRowsCount() {
		if (rowsCount == -1) {
			throw new IllegalStateException("Значения еще не добавлялись");
		}
		return rowsCount;
	}

	public int getColumnsCount() {
		if (columnsCount == -1) {
			throw new IllegalStateException("Значения еще не добавлялись");
		}
		return columnsCount;
	}

	public void forEach(Consumer<Component> callback) {
		for (final List<Component> row : table) {
			for (final Component cmp : row) {
				callback.accept(cmp);
			}
		}
	}

	public Component get(int row, int column) {
		return table.get(row).get(column);
	}

}
