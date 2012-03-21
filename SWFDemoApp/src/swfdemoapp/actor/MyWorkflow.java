package swfdemoapp.actor;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;

@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 600, defaultTaskStartToCloseTimeoutSeconds = 300)
public interface MyWorkflow {

	@Execute(version = "1.0")
	public void processStart(InputData data);
}
