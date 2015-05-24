package daogen;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private Properties properties;

	public Config(String filename) {
		InputStream in = null;
		try {
			properties = new Properties();
			in = new FileInputStream(filename);
			properties.load(in);
		} catch (Exception e) {
			close(in);
			try {
				in = Config.class.getResourceAsStream("/" + filename);
				properties.load(in);
			} catch (Exception ee) {
				throw new RuntimeException("config file not exists, configuation properties init error: " + e.getMessage() + ". " + ee.getMessage());
			}
		} finally {
			close(in);
		}
	}

	private void close(InputStream in) {
		if (in != null)
			try {
				in.close();
			} catch (IOException e) {
			}
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String defaultValue) {
		String property = properties.getProperty(key);
		if (property == null)
			return defaultValue;
		return property;
	}

	public Integer getInt(String key) {
		return getInt(key, null);
	}

	public Integer getInt(String key, Integer def) {
		String property = properties.getProperty(key);
		if (property == null)
			return def;
		return Integer.parseInt(property);
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Boolean getBoolean(String key, Boolean def) {
		String property = properties.getProperty(key);
		if (property == null)
			return def;
		return Boolean.valueOf(property);
	}
}