package com.github.fdvmavenprojectversionupdater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class VersionHolder {

	private String version;

	private final List<Consumer<String>> valueChangeListeners = new ArrayList<>();

	public VersionHolder() {
	}

	public VersionHolder incrementMajor() {
		return increment(0, 1);
	}

	public VersionHolder incrementMinor() {
		return increment(1, 1);
	}

	public VersionHolder incrementBuild() {
		return increment(2, 1);
	}

	public VersionHolder increment(int position, int increment) {
		return updateVersion(sp -> sp[position] = String.valueOf(Integer.valueOf(sp[position]) + increment));
	}

	private VersionHolder updateVersion(Consumer<String[]> updater) {
		final String[] split = version.split("[-.]");
		updater.accept(split);

		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < split.length; ++i) {
			if (i != 0) {
				if (split[i].matches("[a-zA-Z]+")) {
					sb.append("-");
				} else {
					sb.append(".");
				}
			}
			sb.append(split[i]);
		}
		return updateVersion(sb.toString());
	}

	@Override
	public String toString() {
		return version;
	}

	public VersionHolder onlyVersion() {
		return replaceTextSuffix("");
	}

	public VersionHolder toSnapshot() {
		return replaceTextSuffix("-SNAPSHOT");
	}

	public VersionHolder toRC() {
		return replaceTextSuffix("-RC");
	}

	private VersionHolder replaceTextSuffix(String replacement) {
		return updateVersion(version.replaceAll("-?[a-zA-Z]*$", "") + replacement);
	}

	public void setVersion(String newVersion) {
		updateVersion(newVersion);
	}

	private VersionHolder updateVersion(String newVersion) {
		final String pattern = "\\d+[.]\\d+[.]\\d+(-[a-zA-Z]+)?$";
		if (newVersion == null) {
			throw new NullPointerException();
		}
		if (!newVersion.matches(pattern)) {
			throw new IllegalArgumentException("Поддерживаются только версии формата: " + pattern);
		}
		if (!Objects.equals(this.version, newVersion)) {
			this.version = newVersion;
			fireNewVersionEvent();
		}
		return this;
	}

	private void fireNewVersionEvent() {
		valueChangeListeners.forEach(c -> c.accept(version));
	}

	public VersionHolder addValueChangeListener(Consumer<String> consumer) {
		valueChangeListeners.add(consumer);
		return this;
	}

	public void removeValueChangeListener(Consumer<String> consumer) {
		final Iterator<Consumer<String>> iterator = valueChangeListeners.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == consumer) {
				iterator.remove();
			}
		}
	}

}
