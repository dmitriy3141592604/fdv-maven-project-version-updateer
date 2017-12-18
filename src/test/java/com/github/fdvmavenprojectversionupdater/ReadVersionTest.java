package com.github.fdvmavenprojectversionupdater;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ReadVersionTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void test$readVersion() throws Exception {

		final InputStream inputStream = getResourceInputStream();

		final Document document = asDomDocument(inputStream);

		final String documentStringWithOriginalVersion = domDocumentAsString(document);

		logger.trace("Serialized original document:\n{}", documentStringWithOriginalVersion);

		assertEquals(false, documentStringWithOriginalVersion.contains("0.0.2"));
		assertEquals(true, documentStringWithOriginalVersion.contains("0.0.1-SNAPSHOT"));

		final NodeList nodeList = getNodeListByXPath(document, "/m:project/m:version/text()");

		assertEquals("0.0.1-SNAPSHOT", nodeList.item(0).getNodeValue());

		nodeList.item(0).setNodeValue("0.0.2");
		assertEquals(1, nodeList.getLength());

		final String documentStringWintChangedVersion = domDocumentAsString(document);

		logger.debug("Serialized result document:\n{}", documentStringWithOriginalVersion);

		assertEquals(true, documentStringWintChangedVersion.contains("0.0.2"));
		assertEquals(false, documentStringWintChangedVersion.contains("0.0.1-SNAPSHOT"));

	}

	private NodeList getNodeListByXPath(final Document document, String xPathString) throws XPathExpressionException {
		final XPathFactory xPathFactory = XPathFactory.newInstance();
		final XPath xPath = xPathFactory.newXPath();
		final Map<String, String> namespaceMap = new HashMap<String, String>();

		xPath.setNamespaceContext(newNamespaceContext(namespaceMap));
		return (NodeList) xPath.evaluate(xPathString, document, XPathConstants.NODESET);
	}

	private RegistrableNamespaceContext newNamespaceContext(Map<String, String> namespaceMap) {
		final RegistrableNamespaceContext namespaceContext = RegistrableNamespaceContext.newInstance();
		namespaceContext.registerNamespaceToPrefix("http://maven.apache.org/POM/4.0.0", "m");
		return namespaceContext;
	}

	private InputStream getResourceInputStream() {
		return getClass().getClassLoader().getResourceAsStream("com/github/fdvmavenprojectversionupdater/passed-self-pom.xml");
	}

	private Document asDomDocument(InputStream inputStream) throws Exception {
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		final Document document = documentBuilder.parse(new InputSource(inputStream));
		return document;
	}

	private String domDocumentAsString(Document document) throws Exception {
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// transformerFactory.setFeature(XMLConstants., value);
		final Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		final StringWriter out = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(out));

		return out.toString();
	}

}
