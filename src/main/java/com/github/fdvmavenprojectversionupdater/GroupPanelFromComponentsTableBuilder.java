package com.github.fdvmavenprojectversionupdater;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;

import com.github.fdv.borders.MarginBorderDecorator;
import com.github.fdv.models.ComponentsTable;

public class GroupPanelFromComponentsTableBuilder {

	private ComponentsTable componentsTable;

	private final MarginBorderDecorator marginBorderDecarator = new MarginBorderDecorator();

	public GroupPanelFromComponentsTableBuilder componentsTable(ComponentsTable componentsTable) {
		this.componentsTable = componentsTable;
		return this;
	}

	public JPanel build() {

		final int rowsCount = componentsTable.getRowsCount();
		final int columnsCount = componentsTable.getColumnsCount();

		componentsTable.forEach(marginBorderDecarator::decorate);

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
				pg.addComponent(componentsTable.get(rowIndex, columnIndex));
			}
			hGroup.addGroup(pg);
		}

		for (int rowIndex = 0; rowIndex < rowsCount; ++rowIndex) {
			final ParallelGroup pg = gl.createParallelGroup(Alignment.BASELINE);
			for (int columnIndex = 0; columnIndex < columnsCount; ++columnIndex) {
				pg.addComponent(componentsTable.get(rowIndex, columnIndex));
			}
			vGroup.addGroup(pg);
		}

		gl.setHorizontalGroup(hGroup);
		gl.setVerticalGroup(vGroup);
		return propertyBox;
	}

}
