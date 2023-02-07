package passwordmanager.app;

import java.io.File;

public class Util {
	protected static final byte PROMPT = 1;
	protected static final byte PROBLEM = 2;
	protected static final byte LIST = 3;
	protected static final byte DETAILS = 4;

	protected static String getConfigLocation() {
		String osProp = System.getProperty("os.name");
		StringBuilder configPathBuilder = new StringBuilder();
		if (osProp.matches("Windows(?:...)")) {
			configPathBuilder.append(System.getenv("APPDATA"));
			configPathBuilder.append(File.separator);
			configPathBuilder.append("jpass");
		} else if (osProp.matches("Linux|SunOS|FreeBSD(?:...)")) {
			String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
			if (!(xdgConfigHome == null)) {
				configPathBuilder.append(xdgConfigHome);
			} else {
				configPathBuilder.append(System.getenv("HOME"));
				configPathBuilder.append(File.separator);
				configPathBuilder.append(".config");
			}
			configPathBuilder.append(File.separator);
			configPathBuilder.append("jpass");
		} else if (osProp.matches("Mac(?:...)")) {
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

	protected static String color(String str, byte color) {
    if(System.getProperty("os.name").matches("Windows(?:...)")) {
      return str;
    }
		StringBuilder coloredStringBuilder = new StringBuilder();
		switch (color) {
			case PROMPT:
				coloredStringBuilder.append("\033[1;36m");
				break;
			case PROBLEM:
				coloredStringBuilder.append("\033[31m");
				break;
			case LIST:
				coloredStringBuilder.append("\033[35m");
				break;
			case DETAILS:
				coloredStringBuilder.append("\033[1;32m");
		}
		coloredStringBuilder.append(str);
		coloredStringBuilder.append("\033[0m");
		return coloredStringBuilder.toString();
	}

	protected static String color(int intToString, byte color) {
		return color(String.valueOf(intToString), color);
	}
}
