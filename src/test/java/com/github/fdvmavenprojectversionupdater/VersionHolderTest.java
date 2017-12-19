package com.github.fdvmavenprojectversionupdater;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import org.junit.Test;

import com.github.typemarkup.Behavior;

public class VersionHolderTest extends VersionHolderTestBase {

	@Test
	@Behavior("Увеличение мажорной версии увеличивает первую цифру")
	public void test$majorVersionIncrement() {
		assertEquals("1.0.1", newVersionHolder("0.0.1").incrementMajor().toString());
	}

	@Test
	@Behavior("Увеличение мажорной версии увеличивает первую цифру даже для SNAPSHOT")
	public void test$majorVersionIncrement$withSnapshot() {
		assertEquals("1.0.1-SNAPSHOT", newVersionHolder("0.0.1-SNAPSHOT").incrementMajor().toString());
	}

	@Test
	@Behavior("Увеличение минорной версии увеличивает вторую цифру")
	public void test$minorVersionIncrement() {
		assertEquals("3.3.8", newVersionHolder("3.2.8").incrementMinor().toString());
	}

	@Test
	@Behavior("Увеличение минорной версии увеличивает вторую цифру даже для SNAPSHOT")
	public void test$minorVersionIncrement$withSnapshot() {
		assertEquals("3.3.8-SNAPSHOT", newVersionHolder("3.2.8-SNAPSHOT").incrementMinor().toString());
	}

	@Test
	@Behavior("Увеличение версии сборки увеличивает третью цифру")
	public void test$buildVersionIncrement() {
		assertEquals("3.2.9", newVersionHolder("3.2.8").incrementBuild().toString());
	}

	@Test
	@Behavior("Увеличение версии сборки увеличивает третью цифру даже для SNAPSHOT")
	public void test$buildVersionIncrement$withSnapshot() {
		assertEquals("3.2.9-SNAPSHOT", newVersionHolder("3.2.8-SNAPSHOT").incrementBuild().toString());
	}

	@Test
	@Behavior("Увеличение версии сборки увеличивает третью цифру даже для RC")
	public void test$buildVersionIncrement$withRC() {
		assertEquals("3.2.9-RC", newVersionHolder("3.2.8-RC").incrementBuild().toString());
	}

	@Test
	@Behavior("Версию можно пометить как SNAPSHOT")
	public void test$removeSnapshotMarker() {
		assertEquals("0.0.2-SNAPSHOT", newVersionHolder("0.0.2").toSnapshot().toString());
	}

	@Test
	@Behavior("SNAPSHOT остается SNAPSHOT если его помечают как SNAPSHOT")
	public void test$removeSnapshotMarker$withSnapshot() {
		assertEquals("0.0.2-SNAPSHOT", newVersionHolder("0.0.2-SNAPSHOT").toSnapshot().toString());
	}

	@Test
	@Behavior("Версию можно пометить как RC")
	public void test$asRelease() {
		assertEquals("0.0.2-RC", newVersionHolder("0.0.2").toRC().toString());
	}

	@Test
	@Behavior("Версию можно пометить как RC, если она SNAPSHOT")
	public void test$asRelease$fromSnapshot() {
		assertEquals("0.0.2-RC", newVersionHolder("0.0.2-SNAPSHOT").toRC().toString());
	}

	@Test
	@Behavior("Можно убрать пометку. Если пометки нет, то ничего не происходит")
	public void test$onlyVersion() {
		assertEquals("0.0.2", newVersionHolder("0.0.2").onlyVersion().toString());
	}

	@Test
	@Behavior("Можно убрать пометку SNAPSHOT")
	public void test$onlyVersion$withSnapshot() {
		assertEquals("0.0.2", newVersionHolder("0.0.2-SNAPSHOT").onlyVersion().toString());
	}

	@Test
	@Behavior("Можно убрать пометку RC")
	public void test$onlyVersion$withRC() {
		assertEquals("0.0.2", newVersionHolder("0.0.2-RC").onlyVersion().toString());
	}

	@Test
	@Behavior("Версию можно задать через сеттер")
	public void test$setter() {
		final VersionHolder vh = newVersionHolder("0.0.2");
		vh.setVersion("2.3.4");

		assertEquals("2.3.4", vh.toString());
	}

	@Test
	@Behavior("При обновлении версии вызывается наблюдатель")
	public void test$listenerCalled() {
		final VersionHolder vh = newVersionHolder("0.1.2");
		final Consumer<String> consumer = newValue -> logMessage("called(" + newValue + ")");
		vh.addValueChangeListener(consumer);

		assertEquals("", callLog());

		vh.setVersion("4.5.7");
		vh.setVersion("4.5.7");

		assertEquals("MSG:called(4.5.7)", callLog());

		vh.removeValueChangeListener(consumer);

		vh.setVersion("1.2.3");

		assertEquals("Новых сообщений нет", "MSG:called(4.5.7)", callLog());
	}

	@Test(expected = NullPointerException.class)
	@Behavior("null как версия отвергается")
	public void test$nulVersionNotAllowed() {
		newVersionHolder(null);
	}

	@Test(expected = IllegalArgumentException.class)
	@Behavior("Отвергаются версии с неподдерживаемым форматом")
	public void test$wrongFormat() {
		newVersionHolder("asdf");
	}

}
