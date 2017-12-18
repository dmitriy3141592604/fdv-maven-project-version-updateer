package com.github.fdvmavenprojectversionupdater;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.github.typemarkup.Behavior;

@RunWith(BlockJUnit4ClassRunner.class)
public class RegistrableNamespaceContextTest extends RegistrableNamespaceContextTestBase {

	@Test
	@Behavior("После регистрации префикс отображается в пространство имен")
	public void test$simpePrefixMapptig() {
		ctx.registerNamespaceToPrefix("myNamespace", "my");
		assertEquals("myNamespace", ctx.getNamespaceURI("my"));
	}

	@Test
	@Behavior("После регистрации пространство имен отображается в префикс")
	public void test$simpleNamespaceMapping() {
		ctx.registerNamespaceToPrefix("myNamespace", "prf");
		assertEquals("prf", ctx.getPrefix("myNamespace"));
	}

	@Test
	@Behavior("После регистрации можно получить список зарегистрированных префиксов для данного пространства имен")
	public void test$prefixIterator() {
		ctx.registerNamespaceToPrefix("namespace", "ns1");
		ctx.registerNamespaceToPrefix("namespace", "ns2");

		final Iterator<String> iterator = ctx.getPrefixes("namespace");
		assertEquals("ns1ns2false", iterator.next() + iterator.next() + iterator.hasNext());
	}

	@Test
	@Behavior("Можно получить все префиксы для зарегистрированного пространства имен")
	public void test$prefixIterator$filtering() {
		ctx.registerNamespaceToPrefix("ns1", "p1");
		ctx.registerNamespaceToPrefix("ns2", "p2");

		final Iterator<String> iterator = ctx.getPrefixes("ns1");
		assertEquals("p1false", iterator.next() + iterator.hasNext());
	}

	@Test
	@Behavior("Попытка получения префикса для пространства имен null приводит к ошике")
	public void test$prefixIterator$nullNamespace() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("null - Некорректное пространство имен");

		ctx.getPrefixes(null);
	}

	@Test
	@Behavior("Если пространство имен не зарегистрировано - геренируется ошибка")
	public void test$prefixIterator$notRegistredNamespace() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("Пространство имен [not:existsnamespace] незарегистрировано");

		ctx.getPrefixes("not:existsnamespace");
	}

	@Test
	@Behavior("Повторная регистрация префикса для другого пространства имен приводит к исключительеой ситуации")
	public void test$prefixesIsUniqueForAllNamespaces() {
		e.expect(IllegalStateException.class);
		e.expectMessage("Префикс [prf] регистрируется повторно. Предыдущее пространство имен: [ns1]. Новое пространство имен: [ns2]");

		ctx.registerNamespaceToPrefix("ns1", "prf");
		ctx.registerNamespaceToPrefix("ns2", "prf");

	}

	@Test
	@Behavior("Повторная регистрация префикса для того же самого пространства имен приводит к исключительеой ситуации")
	public void test$repeatelyPrefixNamespaceRegistrationNotAllowed() {
		e.expect(IllegalStateException.class);
		e.expectMessage("Префикс [prf] регистрируется повторно. Предыдущее пространство имен: [ns1]. Новое пространство имен: [ns2]");

		ctx.registerNamespaceToPrefix("ns1", "prf");
		ctx.registerNamespaceToPrefix("ns2", "prf");

	}

	@Test
	@Behavior("При получении префикса для незарегистрированного пространства имен генерируется ошибка")
	public void test$notRegisteredNamespace() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("Пространство имен [not:registrednamespace] не зарегистрировано");

		ctx.getPrefix("not:registrednamespace");
	}

	@Test
	@Behavior("При запросе префикса для null генерируется исключение")
	public void test$nullIsNotAllowedNamespace$request() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("null - Некорректное пространство имен");

		ctx.getPrefix(null);
	}

	@Test
	@Behavior("При запросе пространства имен для null генерируется исключение")
	public void test$nullIsNotAllowedPrefix() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("null - Некорректный префикс");

		ctx.getNamespaceURI(null);
	}

	@Test
	@Behavior("При регистрации null как пространства имен генерируется исключение")
	public void test$nullIsNotAllowedNamespace$registration() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("null - Некорректное пространство имен");

		ctx.registerNamespaceToPrefix(null, "prf");
	}

	@Test
	@Behavior("При регистрации null как префикса генерируется исключение")
	public void test$nullIsNotAllowedPrefix$registration() {
		e.expect(IllegalArgumentException.class);
		e.expectMessage("null - Некорректный префикс");

		ctx.registerNamespaceToPrefix("namespace", null);
	}

}
