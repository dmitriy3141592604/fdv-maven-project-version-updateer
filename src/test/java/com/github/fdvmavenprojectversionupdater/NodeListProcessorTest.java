package com.github.fdvmavenprojectversionupdater;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.typemarkup.Behavior;

public class NodeListProcessorTest extends NodeListProcessorTestBase {

	@Test
	@Behavior("Позволяет получить доступ к узлу дерева")
	public void test$nodeAccess() {
		assertEquals("<d><v>0.0.2</v></d>", process("<d><v>0.0.1</v></d>", "/d/v/text()", n -> n.setNodeValue("0.0.2")));
	}
}
