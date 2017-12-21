package com.github.fdv.bus;

import org.junit.Before;

import com.github.fdvmavenprojectversionupdater.LoggableTestBase;
import com.github.fdvmavenprojectversionupdater.RandomTestBase;

public abstract class BusTestBase extends LoggableTestBase implements RandomTestBase {

	protected Bus bus;

	@Before
	public final void setUpBusTestBase() {
		bus = new Bus();
	}
}
