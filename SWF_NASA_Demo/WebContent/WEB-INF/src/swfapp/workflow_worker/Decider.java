package swfapp.workflow_worker;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;

@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 600, defaultTaskStartToCloseTimeoutSeconds = 300)
public interface Decider {

	@Execute(version = "1.0",name="CuriosityDecider")
	public void processStart(String srcImagePath);
}
