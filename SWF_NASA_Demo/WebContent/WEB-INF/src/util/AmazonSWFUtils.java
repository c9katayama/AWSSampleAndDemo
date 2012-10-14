package util;

import java.util.concurrent.TimeUnit;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkerLifecycle;

/**
 * SWF utilities
 * 
 * @author akiok
 */
public class AmazonSWFUtils {

	private static final ConfigHelper configHelper = ConfigHelper.getInstance();

	public static AmazonSimpleWorkflow createSWFClient() {

		ClientConfiguration config = new ClientConfiguration()
				.withSocketTimeout(configHelper.getSocketTimeout());

		AWSCredentials awsCredentials = new BasicAWSCredentials(
				configHelper.getAwsAccessId(), configHelper.getAwsSecretKey());
		AmazonSimpleWorkflow client = new AmazonSimpleWorkflowClient(
				awsCredentials, config);
		client.setEndpoint(configHelper.getSwfServiceUrl());
		return client;
	}

	public static String getTaskToken() {
		return new ActivityExecutionContextProviderImpl()
				.getActivityExecutionContext().getTaskToken();
	}

	public static final void addShutdownHook(final WorkerLifecycle worker) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					worker.shutdownAndAwaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
				}
			}
		});
	}
}
