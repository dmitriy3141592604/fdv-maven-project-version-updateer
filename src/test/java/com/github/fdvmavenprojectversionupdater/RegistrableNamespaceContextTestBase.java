package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class RegistrableNamespaceContextTestBase {

	@Rule
	public ExpectedException e = ExpectedException.none();

	protected RegistrableNamespaceContext ctx;

	@Before
	public final void setUpRegistrableNamespaceContextTestBase() {
		ctx = RegistrableNamespaceContext.newInstance();
	}

}
