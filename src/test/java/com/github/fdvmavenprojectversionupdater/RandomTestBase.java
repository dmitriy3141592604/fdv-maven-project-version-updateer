package com.github.fdvmavenprojectversionupdater;

import java.util.Random;

import com.github.typemarkup.Responsibility;

@Responsibility("Предоставляет возможность сделать тест независимым от тестовых констант")
public interface RandomTestBase {

	@Responsibility("Формирует случайную строку из цифр, начинающуюся с буквенного префикса")
	default String randomString() {
		final Random random = new Random();
		return "" + (new char[] { 'a', 'b', 'c' })[randomInt(3)] + Math.abs(random.nextLong());
	}

	@Responsibility("Позволяет сгенерировать int от 0 до указанного значения")
	default int randomInt(int i) {
		return new Random().nextInt(i);
	}

}
