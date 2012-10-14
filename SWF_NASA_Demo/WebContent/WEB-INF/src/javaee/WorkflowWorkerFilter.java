package javaee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import swfapp.workflow_worker.impl.DeciderImpl;
import util.AmazonSWFUtils;
import util.ConfigHelper;
import util.Constants;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

public class WorkflowWorkerFilter implements Filter {

	protected WorkflowWorker workflowWorker;

	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			AmazonSimpleWorkflow swfClient = AmazonSWFUtils.createSWFClient();
			String domain = ConfigHelper.getInstance().getDomain();

			workflowWorker = new WorkflowWorker(swfClient, domain,
					Constants.DECIDER_LIST);
			workflowWorker.addWorkflowImplementationType(DeciderImpl.class);
			workflowWorker.start();

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		if (workflowWorker.isRunning() == false) {
			throw new ServletException("WorkflowWorker is not running.");
		}
		filterChain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		workflowWorker.shutdown();
	}
}
