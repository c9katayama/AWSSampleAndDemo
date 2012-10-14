package javaee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import swfapp.activity_worker.impl.AWSImpl;
import util.AmazonSWFUtils;
import util.ConfigHelper;
import util.Constants;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;

public class AWS_ActivityWorkerFilter implements Filter {

	protected ActivityWorker activityWorker;

	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			AmazonSimpleWorkflow swfClient = AmazonSWFUtils.createSWFClient();
			String domain = ConfigHelper.getInstance().getDomain();

			// create client instance
			activityWorker = new ActivityWorker(swfClient, domain,
					Constants.ACTIVTY_LIST_AWS);

			// register Activities
			activityWorker
					.addActivitiesImplementation(new AWSImpl());

			// task pooling start
			activityWorker.start();

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		if (activityWorker.isRunning() == false) {
			throw new ServletException("ActivityWorker is not running.");
		}
		filterChain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		activityWorker.shutdown();
	}
}
