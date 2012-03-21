package swfdemoapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import swfdemoapp.actor.Constants;
import swfdemoapp.actor.InputData;
import swfdemoapp.actor.MyWorkflowClientExternal;
import swfdemoapp.actor.MyWorkflowClientExternalFactory;
import swfdemoapp.actor.MyWorkflowClientExternalFactoryImpl;
import swfdemoapp.utils.Helper;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;

/**
 * 新しいワークフローを開始します。
 * 
 * これを実行するたびに、新しいワークスローエグゼキューションが作られます。
 * 
 * @author c9katayama
 * 
 */
public class MyWorkflowStarter {

	public static void main(String[] args) throws Exception {

		JFrame frame = new JFrame("休暇申請画面");
		frame.setSize(400, 100);
		JButton button = new JButton("申請");

		JLabel nameLabel = new JLabel("氏名：");
		final JTextField nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(100, 20));
		JPanel namePanel = new JPanel(new FlowLayout());
		namePanel.add(nameLabel);
		namePanel.add(nameField);

		JLabel dateLabel = new JLabel("日付：");
		final JTextField dateField = new JTextField();
		dateField.setPreferredSize(new Dimension(100, 20));
		JPanel datePanel = new JPanel(new FlowLayout());
		datePanel.add(dateLabel);
		datePanel.add(dateField);

		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(namePanel);
		mainPanel.add(datePanel);

		button.setFont(new Font("メイリオ", Font.PLAIN, 24));

		frame.getContentPane().add(mainPanel, BorderLayout.NORTH);
		frame.getContentPane().add(button, BorderLayout.CENTER);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AmazonSimpleWorkflow swfClient = Helper.createSWFClient();
				String domain = Helper.getConfigHelper().getDomain();

				MyWorkflowClientExternalFactory factory = new MyWorkflowClientExternalFactoryImpl(
						swfClient, domain);
				String workflowId = "DEMO"
						+ new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss")
								.format(new Date());
				MyWorkflowClientExternal client = factory.getClient(workflowId);

				StartWorkflowOptions options = new StartWorkflowOptions();
				options.setTaskList(Constants.DECIDER_LIST_NAME);
				InputData inputData = new InputData();

				String name = nameField.getText();
				if (name == null || name.length() == 0) {
					name = "名無し";
				}
				inputData.setName(name);
				String date = dateField.getText();
				if (date == null || date.length() == 0) {
					name = "2012/3/30";
				}
				inputData.setDate(date);
				client.processStart(inputData, options);
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
