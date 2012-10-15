package swfapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import swfapp.workflow_worker.DeciderClientExternal;
import swfapp.workflow_worker.DeciderClientExternalFactory;
import swfapp.workflow_worker.DeciderClientExternalFactoryImpl;
import util.AmazonSWFUtils;
import util.ConfigHelper;
import util.Constants;
import util.Log;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;

/**
 * Start new workflow execution
 * 
 * @author akiok
 * 
 */
public class CuriosityRoverMain {

	public static void main(String[] args) throws Exception {

		JFrame frame = new JFrame("From Mars");
		frame.setSize(507, 440);
		JButton button = new JButton("Send data to the Earth");
		button.setPreferredSize(new Dimension(100,40));
		button.setFont(new Font("Arial", Font.PLAIN, 24));
		JLabel label = new JLabel(new ImageIcon(
				CuriosityRoverMain.class
						.getResource("/curiosity.jpg")));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		button.addActionListener(new ActionListener() {
			private AmazonSimpleWorkflow swfClient = AmazonSWFUtils
					.createSWFClient();
			private String domain = ConfigHelper.getInstance().getDomain();
			private DeciderClientExternalFactory factory = new DeciderClientExternalFactoryImpl(
					swfClient, domain);
			@Override
			public void actionPerformed(ActionEvent e) {
				String workflowId = "CURIOSITY-"
						+ new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS")
								.format(new Date());

				StartWorkflowOptions option = new StartWorkflowOptions();
				option.setTaskList(Constants.DECIDER_LIST);

				DeciderClientExternal client = factory.getClient(workflowId);

				client.processStart(workflowId, option);

				// WorkflowExecution is available after workflow creation
				WorkflowExecution workflowExecution = client
						.getWorkflowExecution();
				Log.log("[Curiosity]"+workflowExecution);
			}
		});

		frame.setVisible(true);
	}

}
