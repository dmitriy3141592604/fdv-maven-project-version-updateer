package com.github.fdvmavenprojectversionupdater;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.function.Consumer;

import org.junit.Before;
import org.w3c.dom.Node;

public abstract class NodeListProcessorTestBase {

	protected NodeListProcessor processor;

	@Before
	public void setUpNodeListProcessorBase() {
		processor = new NodeListProcessor();
	}

	protected String process(final String s, final String xPathString, final Consumer<Node> nodeProcessingCallback) {
		return processor.parse(new StringReader(s)).forNode(xPathString, nodeProcessingCallback).write(new StringWriter()).toString();
	}

}
