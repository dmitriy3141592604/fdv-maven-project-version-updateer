package com.github.fdvmavenprojectversionupdater;

import java.io.Reader;
import java.io.Writer;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NodeListProcessor {

	private Document document;

	private final RegistrableNamespaceContext namespaceContext;

	private final XPathFactory xPathFactory;

	public NodeListProcessor() {
		namespaceContext = RegistrableNamespaceContext.newInstance();
		xPathFactory = XPathFactory.newInstance();
	}

	public NodeList getNodeList(String xPathString) {
		try {
			final XPath xPath = xPathFactory.newXPath();
			xPath.setNamespaceContext(namespaceContext);

			return (NodeList) xPath.evaluate(xPathString, document, XPathConstants.NODESET);
		} catch (final Exception exception) {
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			}
			throw new RuntimeException(exception);
		}
	}

	public Node getNode(String xPathString) {
		final NodeList nodeList = getNodeList(xPathString);
		if (nodeList == null) {
			// TODO Проверить, что возвращается, если ничего не найдено
		}
		if (nodeList.getLength() != 1) {
			throw new IllegalStateException("Вернулось: " + nodeList.getLength() + " узлов. Ожидается один узел");
		}
		return nodeList.item(0);
	}

	public NodeListProcessor forNode(String xPathString, Consumer<Node> nodeProcessingCallback) {
		nodeProcessingCallback.accept(getNode(xPathString));
		return this;
	}

	public void registerPrefixToNamespace(String prefix, String namespace) {
		namespaceContext.registerNamespaceToPrefix(namespace, prefix);
	}

	public NodeListProcessor parse(Reader reader) {
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(new InputSource(reader));
			return this;
		} catch (final Exception exception) {
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			}
			throw new RuntimeException(exception);
		}
	}

	public Writer write(Writer writer) {
		try {
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			return writer;
		} catch (final Exception exception) {
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			}
			throw new RuntimeException(exception);
		}

	}
}