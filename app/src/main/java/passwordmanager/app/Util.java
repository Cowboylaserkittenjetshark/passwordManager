package passwordmanager.app;

import java.io.File;

public class Util {

	protected static String getConfigLocation() {
		String osProp = System.getProperty("os.name");
		StringBuilder configPathBuilder = new StringBuilder();
		if (osProp.matches("Windows]")) {
			configPathBuilder.append(System.getenv("APPDATA"));
			configPathBuilder.append(File.separator);
			configPathBuilder.append("jpass");
		} else if (osProp.matches("Linux|SunOS|FreeBSD")) {
			String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
			if (!xdgConfigHome.equals(null)) {
				configPathBuilder.append(xdgConfigHome);
			} else {
				configPathBuilder.append(System.getenv("HOME"));
				configPathBuilder.append(File.separator);
				configPathBuilder.append(".config");
			}
			configPathBuilder.append(File.separator);
			configPathBuilder.append("jpass");
		} else if (osProp.matches("Mac")) {
			configPathBuilder.append(System.getProperty("user.home"));
			configPathBuilder.append(File.separator);
			configPathBuilder.append("Library");
			configPathBuilder.append(File.separator);
			configPathBuilder.append("Application Support");
			configPathBuilder.append(File.separator);
			configPathBuilder.append("jpass");
		} else {
			configPathBuilder.append(System.getProperty("user.home"));
			configPathBuilder.append(File.separator);
			configPathBuilder.append(".jpass");
		}
		return configPathBuilder.toString();
	}

// TODO Finish color method?
/* 	protected static String color(String str, String color) {
		StringBuilder coloredStringBuilder = new StringBuilder();
		switch(color) {
			case "red":
				coloredStringBuilder.append("\u001B[31m");
				break;
			case("green"):
				coloredStringBuilder
		}
		coloredStringBuilder.append(str);
		coloredStringBuilder.append(coloredStringBuilder);
		return coloredStringBuilder.toString();
	} */
}
