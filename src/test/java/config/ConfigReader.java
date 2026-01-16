package config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

	private static final Properties properties = new Properties();

	// static block runs once when class is loaded
	static {
		loadProperties();
	}

	private ConfigReader() {
		// prevent object creation
	}

	private static void loadProperties() {
		String env = System.getProperty("env", "qa"); // default = qa
		String fileName = "config/config" + ".properties";

		try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {

			if (input == null) {
				throw new RuntimeException("❌ Property file not found: " + fileName);
			}
			properties.load(input);

		} catch (Exception e) {
			throw new RuntimeException("❌ Failed to load property file: " + fileName, e);
		}
	}

	/**
	 * Get property value by key
	 */
	public static String get(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			throw new RuntimeException("❌ Property key not found: " + key);
		}
		return value.trim();
	}
}
