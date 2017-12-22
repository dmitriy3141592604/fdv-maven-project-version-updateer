package com.github.fdv.bus;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.github.fdvmavenprojectversionupdater.LoggableTestBase;
import com.github.fdvmavenprojectversionupdater.RandomTestBase;

public abstract class BusTestBase extends LoggableTestBase implements RandomTestBase {

	@InjectMocks
	protected Bus bus;

	@Mock
	public Logger logger;

	@Before
	public final void setUpBusTestBase() {
		MockitoAnnotations.initMocks(this);
	}
}
