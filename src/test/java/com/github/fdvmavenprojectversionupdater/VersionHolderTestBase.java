package com.github.fdvmavenprojectversionupdater;

import org.junit.Before;

public abstract class VersionHolderTestBase extends LoggableTestBase {

	@Before
	public final void setUpVersionHolderTestBase() {
	}

	protected VersionHolder newVersionHolder(String version) {
		final VersionHolder versionHolder = new VersionHolder();
		versionHolder.setVersion(version);
		return versionHolder;
	}

}
