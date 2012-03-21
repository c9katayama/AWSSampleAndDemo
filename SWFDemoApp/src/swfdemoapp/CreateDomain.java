package swfdemoapp;

import swfdemoapp.utils.Helper;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.model.RegisterDomainRequest;

/**
 * ドメインを作成します。
 * 
 * 初回に一度だけ実行してください。
 * 
 * @author c9katayama
 * 
 */
public class CreateDomain {

	public static void main(String[] args) throws Exception {

		System.out.println("Start create domain");
		AmazonSimpleWorkflow swfClient = Helper.createSWFClient();
		String domain = Helper.getConfigHelper().getDomain();
		String days = Helper.getConfigHelper().getDomainRetentionPeriodInDays();
		RegisterDomainRequest request = new RegisterDomainRequest();
		request.setName(domain);
		request.setWorkflowExecutionRetentionPeriodInDays(days);
		swfClient.registerDomain(request);
		System.out.println("Create domain ["+domain+"] success.");

		System.exit(0);
	}
}
