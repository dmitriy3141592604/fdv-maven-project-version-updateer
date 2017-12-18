package com.github.fdvmavenprojectversionupdater;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//FIXME Отображение множества префиксов в одно пространство имен не поддерживается
public class RegistrableNamespaceContext implements NamespaceContext {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<String, Set<String>> namespaceToPrefix;

	private final Map<String, String> prefixToNamespace;

	public static RegistrableNamespaceContext newInstance() {
		return new RegistrableNamespaceContext();
	}

	private RegistrableNamespaceContext() {
		this(new TreeMap<>(), new TreeMap<>());
	}

	private RegistrableNamespaceContext(Map<String, String> prefixToNamespace) {
		this(prefixToNamespace, new TreeMap<String, Set<String>>());
	}

	private RegistrableNamespaceContext(Map<String, String> prefixToNamespace, Map<String, Set<String>> namespaceToPrefix) {
		this.prefixToNamespace = prefixToNamespace;
		this.namespaceToPrefix = namespaceToPrefix;
	}

	public void registerNamespaceToPrefix(String namespace, String prefix) {
		checkNamespace(namespace);
		checkPrefix(prefix);
		if (!namespaceToPrefix.containsKey(namespace)) {
			namespaceToPrefix.put(namespace, new TreeSet<String>());
		}
		final boolean didNotContains = namespaceToPrefix.get(namespace).add(prefix);
		final String previousNamespaceForNewPrefix = prefixToNamespace.get(prefix);
		if (!didNotContains || null != previousNamespaceForNewPrefix) {
			final StringBuilder sb = new StringBuilder();
			sb.append("Префикс [" + prefix + "] регистрируется повторно.");
			sb.append(" Предыдущее пространство имен: [" + previousNamespaceForNewPrefix + "].");
			sb.append(" Новое пространство имен: [" + namespace + "]");
			throw new IllegalStateException(sb.toString());
		}
		prefixToNamespace.put(prefix, namespace);
	}

	@Override
	public Iterator<String> getPrefixes(String namespace) {
		checkNamespace(namespace);
		if (!namespaceToPrefix.containsKey(namespace)) {
			throw new IllegalArgumentException("Пространство имен [" + namespace + "] незарегистрировано");
		}
		return namespaceToPrefix.get(namespace).iterator();
	}

	@Override
	public String getPrefix(String namespace) {
		checkNamespace(namespace);
		final Set<String> set = namespaceToPrefix.get(namespace);
		if (set == null) {
			throw new IllegalArgumentException("Пространство имен [" + namespace + "] не зарегистрировано");
		}
		final Iterator<String> iterator = set.iterator();
		final String prefix = iterator.next();
		logger.debug("map namespace: [{}] to prefix: [{}]", prefixToNamespace, prefix);
		return prefix;
	}

	private void checkNamespace(String namespace) {
		if (namespace == null) {
			throw new IllegalArgumentException("null - Некорректное пространство имен");
		}
	}

	private void checkPrefix(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("null - Некорректный префикс");
		}
	}

	@Override
	public String getNamespaceURI(String prefix) {
		checkPrefix(prefix);
		final String namespace = prefixToNamespace.get(prefix);
		logger.debug("map prefix: [{}] to namespace: [{}]", prefix, namespace);
		return namespace;
	}
}