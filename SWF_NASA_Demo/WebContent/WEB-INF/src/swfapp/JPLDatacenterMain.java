package swfapp;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import swfapp.activity_worker.data.CombineResult;
import swfapp.activity_worker.impl.JPLDatacenterImpl;
import swfapp.workflow_worker.impl.DeciderImpl;
import util.AmazonSWFUtils;
import util.ConfigHelper;
import util.Constants;
import util.Log;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

public class JPLDatacenterMain {

	private JFrame frame;
	private ActivityWorker activityWorker;
	private WorkflowWorker workflowWorker;
	private BufferedImage baseImage;

	public static void main(String[] args) throws Exception {
		new JPLDatacenterMain().start();
	}

	public void start() throws Exception {
		frame = new JFrame("NASA JPL Datacenter");
		frame.setLocation(new Point(500, 0));
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
		Log.log("[JPL]Initializing Workflow Worker(Decider)...");
		startWorkflowWorker();
		Log.log("[JPL]Initialized.");

		baseImage = ImageIO.read(CuriosityRoverMain.class
				.getResource("/mer_panorama.png"));
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
		activityWorker.addActivitiesImplementation(new JPLDatacenterImpl() {
			int indexX = 0;
			int indexY = 0;

			@Override
			public synchronized void showResult(CombineResult result) {
				ActivityExecutionContextProvider provider = new ActivityExecutionContextProviderImpl();
				ActivityExecutionContext aec = provider
						.getActivityExecutionContext();
				String workflowId = aec.getWorkflowExecution().getWorkflowId();
				int w = 500;
				int h = 300;
				if (indexX == 5) {
					indexX = 0;
					indexY = indexY == 0 ? 1 : 0;
				} else {
					indexX++;
				}
				int x = indexX * w;
				int y = indexY * h;
				BufferedImage partImage = baseImage.getSubimage(x, y, w, h);
				JLabel resultLabel = new JLabel(new ImageIcon(partImage));
				//JDialog resultDialog = new JDialog(frame,"RESULT:"+workflowId);
				JFrame resultDialog = new JFrame("RESULT:"+workflowId);
				resultDialog.setSize(w, h);
				Point parentLocation = frame.getLocation();
				int resultX = parentLocation.x + frame.getWidth() / 2 - w / 2;
				int resultY = parentLocation.y + frame.getHeight() / 2 - y / 2;
				System.out.println(resultX + " " + resultY);
				resultDialog.setLocation(new Point(Math.max(0, resultX), Math
						.max(0, resultY)));
				resultDialog.add(resultLabel);
				resultDialog.setVisible(true);
				resultDialog.toFront();
			}
		});

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
