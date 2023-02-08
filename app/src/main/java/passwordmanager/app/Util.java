package passwordmanager.app;

import java.io.File;
import java.security.SecureRandom;

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

	protected static char[] randomPassword(int length) {
		char[] pool = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','!','@','#','$','%','^','&','*'};
        SecureRandom rand = new SecureRandom();
        char[] randPassword = new char[length];
		for(int i = 0; i < randPassword.length; i++) {
            int num = rand.nextInt(pool.length - 1);
            randPassword[i] = pool[num];
        }
		return randPassword;
	}
}
