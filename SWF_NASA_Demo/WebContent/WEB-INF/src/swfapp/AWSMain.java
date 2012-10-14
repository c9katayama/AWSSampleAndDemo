package swfapp;

import swfapp.activity_worker.impl.AWSImpl;
import util.AmazonSWFUtils;
import util.ConfigHelper;
import util.Constants;
import util.Log;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;

public class AWSMain {

	private ActivityWorker activityWorker;

	public static void main(String[] args) throws Exception {
		new AWSMain().start();
	}

	public void start() throws Exception {
		startActivityWorker();
		AmazonSWFUtils.addShutdownHook(activityWorker);
	}

	public void startActivityWorker() throws Exception {
		Log.log("[AWS]Initializing Activity Worker...");
		AmazonSimpleWorkflow swfClient = AmazonSWFUtils.createSWFClient();
		String domain = ConfigHelper.getInstance().getDomain();

		// create client instance
		ActivityWorker activityWorker = new ActivityWorker(swfClient, domain,
				Constants.ACTIVTY_LIST_AWS);

		// register Activities
		activityWorker.addActivitiesImplementation(new AWSImpl());

		// task pooling start
		activityWorker.start();
		Log.log("[AWS]Initialized.");
	}

}
