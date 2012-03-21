package swfdemoapp.actor.impl;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import swfdemoapp.actor.MyActivities2;
import swfdemoapp.utils.Helper;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClient;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClientFactoryImpl;
import com.amazonaws.services.simpleworkflow.flow.annotations.ManualActivityCompletion;

public class MyActivities2Impl implements MyActivities2 {

	private JFrame managerFrame;
	private JFrame legalFrame;

	public void setFrames(JFrame managerFrame, JFrame legalFrame) {
		this.managerFrame = managerFrame;
		this.legalFrame = legalFrame;
	}

	@ManualActivityCompletion
	@Override
	public Boolean confirmManager() {
		System.out.println("confirmManager");

		String taskToken = Helper.getTaskToken();
		Confirm confirm = new Confirm(managerFrame, taskToken);
		confirm.execute();
		return true;
	}

	@ManualActivityCompletion
	@Override
	public Boolean confirmLegal() {
		System.out.println("confirmLegal");
		String taskToken = Helper.getTaskToken();
		Confirm confirm = new Confirm(legalFrame, taskToken);
		confirm.execute();
		return true;
	}

	public class Confirm {
		private JFrame frame;
		private ManualActivityCompletionClient manualActivityCompletionClient;

		public Confirm(JFrame frame, String token) {
			this.frame = frame;
			AmazonSimpleWorkflow swfClient = Helper.createSWFClient();
			ManualActivityCompletionClientFactoryImpl factory = new ManualActivityCompletionClientFactoryImpl(
					swfClient);
			manualActivityCompletionClient = factory.getClient(token);
		}

		public void execute() {
			JButton okButton = new JButton("OK");
			okButton.setFont(new Font("メイリオ", Font.PLAIN, 24));
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manualActivityCompletionClient.complete(Boolean.TRUE);
					frame.getContentPane().removeAll();
					JLabel label = new JLabel("OK");
					label.setFont(new Font("メイリオ", Font.PLAIN, 24));
					frame.getContentPane().add(label);
					frame.validate();
				}
			});
			JButton ngButton = new JButton("NG");
			ngButton.setFont(new Font("メイリオ", Font.PLAIN, 24));
			ngButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manualActivityCompletionClient.complete(Boolean.FALSE);
					frame.getContentPane().removeAll();
					JLabel label = new JLabel("NG");
					label.setFont(new Font("メイリオ", Font.PLAIN, 24));
					frame.getContentPane().add(label);
					frame.validate();
				}
			});
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.add(okButton);
			panel.add(ngButton);
			frame.getContentPane().add(panel);
			frame.validate();
		}
	}

}
