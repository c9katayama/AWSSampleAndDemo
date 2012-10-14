package swfapp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import swfapp.activity_worker.impl.JPLDatacenterImpl;
import swfapp.workflow_worker.impl.DeciderImpl;
import util.AmazonSWFUtils;
import util.ConfigHelper;
import util.Constants;
import util.Log;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

public class JPLDatacenterMain {

	private ActivityWorker activityWorker;
	private WorkflowWorker workflowWorker;

	public static void main(String[] args) throws Exception {
		new JPLDatacenterMain().start();
	}

	public void start() throws Exception {
		JFrame frame = new JFrame("NASA JPL Datacenter");
		frame.setLocation(new Point(500,0));
		frame.setSize(320, 500);
		JLabel label = new JLabel(new ImageIcon(
				CuriosityRoverMain.class.getResource("/NASA.png")));
		frame.getContentPane().add(label, BorderLayout.NORTH);

		@SuppressWarnings("serial")
		final JTextArea text = new JTextArea() {
			public void append(String str) {
				super.append(str);
				setCaretPosition(getDocument().getLength());
			}
		};
		text.setFont(new Font("Arial", Font.PLAIN, 18));

		JScrollPane scrollpane2 = new JScrollPane(text);
		frame.getContentPane().add(scrollpane2, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Log.logger = new Log.Logger() {
			@Override
			public void log(final String message) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						text.append(message + "\r\n");
					}
				});
			}
		};
		Log.log("[JPL]Initializing Activity Worker...");
		startActivityWorker();
		Log.log("[JPL]Initializing Workflow Worker...");
		startWorkflowWorker();
		Log.log("[JPL]Initialized.");

		AmazonSWFUtils.addShutdownHook(activityWorker);
		AmazonSWFUtils.addShutdownHook(workflowWorker);
	}

	public void startActivityWorker() throws Exception {
		// start activity
		AmazonSimpleWorkflow swfClient = AmazonSWFUtils.createSWFClient();
		String domain = ConfigHelper.getInstance().getDomain();

		// create client instance
		activityWorker = new ActivityWorker(swfClient, domain,
				Constants.ACTIVTY_LIST_JPL);

		// register Activities
		activityWorker.addActivitiesImplementation(new JPLDatacenterImpl());

		// start task pooling
		activityWorker.start();
	}

	public void startWorkflowWorker() throws Exception {
		AmazonSimpleWorkflow swfClient = AmazonSWFUtils.createSWFClient();
		String domain = ConfigHelper.getInstance().getDomain();
		// start decider
		workflowWorker = new WorkflowWorker(swfClient, domain,
				Constants.DECIDER_LIST);
		workflowWorker.addWorkflowImplementationType(DeciderImpl.class);
		workflowWorker.start();

	}
}
