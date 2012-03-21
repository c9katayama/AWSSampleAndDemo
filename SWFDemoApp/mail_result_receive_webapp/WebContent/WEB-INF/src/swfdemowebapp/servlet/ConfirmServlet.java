package swfdemowebapp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClient;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClientFactoryImpl;

public class ConfirmServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String token = req.getParameter("taskToken");
		String result = req.getParameter("result");
		AWSCredentials credentials = readCredentials();
		
		AmazonSimpleWorkflow swfClient = new AmazonSimpleWorkflowClient(
				credentials);
		swfClient.setEndpoint("http://swf.us-east-1.amazonaws.com");
		ManualActivityCompletionClientFactoryImpl factory = new ManualActivityCompletionClientFactoryImpl(
				swfClient);

		ManualActivityCompletionClient manualActivityCompletionClient = factory
				.getClient(token);
		if ("ok".equals(result)) {
			manualActivityCompletionClient.complete("OK");
		} else {
			manualActivityCompletionClient.complete("NG");
		}

		resp.setContentType("text/html");
		Writer writer = resp.getWriter();
		writer.write("<html><body>");
		writer.write("<h1>" + URLEncoder.encode(result, "UTF-8") + "</h1>");
		writer.write("</body></html>");
		writer.flush();
	}

	protected AWSCredentials readCredentials() throws IOException {
		try {
			AWSCredentials credentials = new PropertiesCredentials(
					ConfirmServlet.class
							.getResourceAsStream("/swf-demo.properties"));
			if (credentials != null) {
				return credentials;
			}
		} catch (Exception e) {
			// ignore
		}
		File file = new File("swf-demo.properties");
		if (file.exists() == true) {
			Properties p = new Properties();
			p.load(new FileInputStream(file));
			BasicAWSCredentials credentials = new BasicAWSCredentials(
					p.getProperty("AWS.Access.ID"),
					p.getProperty("AWS.Secret.Key"));
			return credentials;
		} else {
			throw new RuntimeException("swf-demo.properties not found.");
		}
	}
}
