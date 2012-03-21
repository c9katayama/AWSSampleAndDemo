package swfdemoapp.utils;

import java.net.URLEncoder;

import javax.swing.UIManager;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;

public class Helper {

	public static String encode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setupLAF() {

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static ConfigHelper configHelper;
	static {
		try {
			configHelper = ConfigHelper.createConfig();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static AmazonSimpleWorkflow createSWFClient() {
		return configHelper.createSWFClient();
	}

	public static String getValueFromConfig(String key) {
		return configHelper.getValueFromConfig(key);
	}

	public static AmazonSimpleEmailService createSESClient() {
		String swfAccessId = configHelper
				.getValueFromConfig(Constants.SWF_ACCESS_ID_KEY);
		String swfSecretKey = configHelper
				.getValueFromConfig(Constants.SWF_SECRET_KEY_KEY);

		BasicAWSCredentials credentials = new BasicAWSCredentials(swfAccessId,
				swfSecretKey);
		AmazonSimpleEmailService ses = new AmazonSimpleEmailServiceClient(
				credentials);
		return ses;
	}

	public static ConfigHelper getConfigHelper() {
		return configHelper;
	}

	public static String getTaskToken() {
		return new ActivityExecutionContextProviderImpl()
				.getActivityExecutionContext().getTaskToken();
	}
}
