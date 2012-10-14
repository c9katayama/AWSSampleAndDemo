package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Helper to used to create SWF and S3 clients
 */

public class ConfigHelper {

	private static ConfigHelper configHelper;

	private Properties sampleConfig;
	private String swfServiceUrl;
	private String awsAccessId;
	private String awsSecretKey;
	private String domain;
	private String domainRetentionPeriodInDays;// "NONE" means unlimited
	private int socketTimeout;

	private ConfigHelper(File propertiesFile) throws IOException {
		loadProperties(propertiesFile);
	}

	private ConfigHelper(Properties p) throws IOException {
		loadProperties(p);
	}

	private void loadProperties(File propertiesFile) throws IOException {
		FileInputStream inputStream = new FileInputStream(propertiesFile);
		Properties p = new Properties();
		p.load(inputStream);
		loadProperties(p);
	}

	private void loadProperties(Properties p) throws IOException {
		sampleConfig = p;

		this.swfServiceUrl = sampleConfig
				.getProperty(Constants.SWF_SERVICE_URL_KEY);
		this.awsAccessId = sampleConfig
				.getProperty(Constants.AWS_ACCESS_ID_KEY);
		this.awsSecretKey = sampleConfig
				.getProperty(Constants.AWS_SECRET_KEY_KEY);
		this.socketTimeout = Integer.valueOf(sampleConfig
				.getProperty(Constants.SOCKET_TIMEOUT));

		this.domain = sampleConfig.getProperty(Constants.DOMAIN_KEY);
		this.domainRetentionPeriodInDays = sampleConfig
				.getProperty(Constants.DOMAIN_RETENTION_PERIOD_KEY);
	}

	public static ConfigHelper getInstance() {

		if (configHelper != null) {
			return configHelper;
		}

		boolean envVariableExists = false;
		// first, check the existence of environment variable
		String sampleConfigPath = System
				.getenv(Constants.ACCESS_PROPERTIES_ENVIRONMENT_VARIABLE);
		if (sampleConfigPath != null && sampleConfigPath.length() > 0) {
			envVariableExists = true;
		}
		File accessProperties = new File(
				System.getProperty(Constants.HOME_DIRECTORY_PROPERTY),
				Constants.ACCESS_PROPERTIES_FILENAME);

		try {
			if (accessProperties.exists()) {
				configHelper = new ConfigHelper(accessProperties);
			} else if (envVariableExists) {
				accessProperties = new File(sampleConfigPath,
						Constants.ACCESS_PROPERTIES_FILENAME);
				configHelper = new ConfigHelper(accessProperties);
			} else {
				// try checking the existence of file on relative path.
				try {
					accessProperties = new File(
							Constants.ACCESS_PROPERTIES_FILENAME);
					configHelper = new ConfigHelper(accessProperties);
				} catch (Exception e) {
					Properties p = new Properties();
					p.load(ConfigHelper.class.getResourceAsStream("/"
							+ Constants.ACCESS_PROPERTIES_FILENAME));
					configHelper = new ConfigHelper(p);
				}
			}
			return configHelper;
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public String getDomain() {
		return domain;
	}

	public String getDomainRetentionPeriodInDays() {
		return domainRetentionPeriodInDays;
	}

	public String getValueFromConfig(String key) {
		return sampleConfig.getProperty(key);
	}

	public String getSwfServiceUrl() {
		return swfServiceUrl;
	}

	public String getAwsAccessId() {
		return awsAccessId;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}
}
