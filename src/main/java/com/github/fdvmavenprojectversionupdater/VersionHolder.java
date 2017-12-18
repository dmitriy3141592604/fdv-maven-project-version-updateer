package com.github.fdvmavenprojectversionupdater;

import java.util.function.Consumer;

public class VersionHolder {

	private String version;

	public VersionHolder(String version) {
		this.version = version;
	}

	public VersionHolder incrementMajor() {
		final int position = 0;
		final int increment = 1;
		return increment(position, increment);
	}

	public VersionHolder increment(int position, int increment) {
		return updateVersion(sp -> sp[position] = String.valueOf(Integer.valueOf(sp[position]) + increment));
	}

	public VersionHolder incrementMinor() {
		return updateVersion(sp -> sp[1] = String.valueOf(Integer.valueOf(sp[1]) + 1));
	}

	public VersionHolder incrementBuild() {
		return updateVersion(sp -> sp[2] = String.valueOf(Integer.valueOf(sp[2]) + 1));
	}

	private VersionHolder updateVersion(final Consumer<String[]> updater) {
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
		version = sb.toString();
		return this;
	}

	@Override
	public String toString() {
		return version;
	}

	public static void main(String... args) {
		print("", "0.0.1", new VersionHolder("0.0.1").incrementMajor());
		print("", "0.0.1-SNAPSHOT", new VersionHolder("0.0.1-SNAPSHOT").incrementMajor());
		print("", "3.2.8", new VersionHolder("3.2.8").incrementMinor());
		print("", "3.2.8-SNAPSHOT", new VersionHolder("3.2.8-SNAPSHOT").incrementMinor());
		print("", "3.2.8", new VersionHolder("3.2.8").incrementBuild());
		print("", "3.2.8-SNAPSHOT", new VersionHolder("3.2.8-SNAPSHOT").incrementBuild());
		print("", "3.2.8-RC", new VersionHolder("3.2.8-RC").incrementBuild());
		print("sn", "0.0.2", new VersionHolder("0.0.2").toSnapshot());
		print("sn", "0.0.2-SNAPSHOT", new VersionHolder("0.0.2-SNAPSHOT").toSnapshot());
		print("rc", "0.0.2", new VersionHolder("0.0.2").toRC());
		print("rc", "0.0.2-SNAPSHOT", new VersionHolder("0.0.2-SNAPSHOT").toRC());
		print("on", "0.0.2", new VersionHolder("0.0.2").onlyVersion());
		print("on", "0.0.2-SNAPSHOT", new VersionHolder("0.0.2").onlyVersion());
	}

	public VersionHolder onlyVersion() {
		version = version.replaceAll("-?[a-zA-Z]*$", "");
		return this;
	}

	public VersionHolder toSnapshot() {
		version = version.replaceAll("-?[a-zA-Z]*$", "") + "-SNAPSHOT";
		return this;
	}

	public VersionHolder toRC() {
		version = version.replaceAll("-?[a-zA-Z]*$", "") + "-RC";
		return this;
	}

	private static void print(String marker, String label, VersionHolder vh) {
		System.out.println(String.format("%-4s %15s -> %s", marker, label, vh.toString()));
	}
}
