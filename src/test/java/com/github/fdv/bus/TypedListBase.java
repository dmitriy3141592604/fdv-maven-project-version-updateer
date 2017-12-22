package com.github.fdv.bus;

import static org.mockito.MockitoAnnotations.initMocks;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.github.fdvmavenprojectversionupdater.LoggableTestBase;
import com.github.fdvmavenprojectversionupdater.RandomTestBase;

public abstract class TypedListBase extends LoggableTestBase implements RandomTestBase {

	@Mock
	protected Logger logger;

	@InjectMocks
	protected TypedList<String> typedList;

	@Captor
	protected ArgumentCaptor<String> stringCaptor;

	@Captor
	protected ArgumentCaptor<Object> objectCaptor1;

	@Captor
	protected ArgumentCaptor<Object> objectCaptor2;

	@Captor
	protected ArgumentCaptor<Object> objectCaptor3;

	@Rule
	public ExpectedException expected = ExpectedException.none();

	protected Consumer<?> emptyConsumer = str -> {
	};

	@Before
	public final void setUpTypedListBase() {
		initMocks(this);
	}

	protected Object asObject(Object capture) {
		return capture;
	}

}
