package swfdemoapp;

import swfdemoapp.actor.Constants;
import swfdemoapp.actor.impl.MyWorkflowImpl;
import swfdemoapp.utils.Helper;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

/**
 * デサイダーを実行します。
 * 
 * このDeciderのプロセスが、ワークフロー内でのデジション部分を担当します。 起動後、タスクの待ち受け状態になります。
 * 
 * @author c9katayama
 * 
 */
public class MyDeciderWorker {

	public static void main(String[] args) throws Exception {
		AmazonSimpleWorkflow swfClient = Helper.createSWFClient();
		String domain = Helper.getConfigHelper().getDomain();

		WorkflowWorker worker = new WorkflowWorker(swfClient, domain,
				Constants.DECIDER_LIST_NAME);
		worker.addWorkflowImplementationType(MyWorkflowImpl.class);
		worker.start();
	}
}
