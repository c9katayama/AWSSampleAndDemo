package swfdemoapp.actor.impl;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import swfdemoapp.actor.InputData;
import swfdemoapp.actor.MyActivities3;
import swfdemoapp.utils.Constants;
import swfdemoapp.utils.Helper;
import swfdemoapp.utils.AmazonSES;

import com.amazonaws.services.simpleworkflow.flow.annotations.ManualActivityCompletion;

public class MyActivities3Impl implements MyActivities3 {

	private JFrame frame;

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	@ManualActivityCompletion
	@Override
	public String sendConfirmMail(InputData data) {
		System.out.println("SEND EMail");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					frame.getContentPane().removeAll();
					JLabel label = new JLabel("<html>社長にメール送信中・・・</html>");
					label.setFont(new Font("メイリオ", Font.PLAIN, 24));
					frame.getContentPane().add(label);
					frame.validate();
				}
			});

			String taskToken = Helper.getTaskToken();
			taskToken = Helper.encode(taskToken);
			String title = "休暇申請";
			String url = Helper.getConfigHelper().getValueFromConfig(Constants.MAIL_RESULT_RECEIVE_URL);
			String okurl = url+"?result=ok&taskToken="
					+ taskToken;
			String ngurl = url+"?result=ng&taskToken="
					+ taskToken;
			String body = data.getName()
					+ "さんから" + data.getDate()+"の休暇申請が届いています。\r\n\r\n承認する場合はこのリンクをクリックして下さい。\r\n"
					+ okurl;
			body += "\r\n\r\n承認しない場合はこのリンクをクリックして下さい\r\n" + ngurl;
			String mailFrom = Helper.getConfigHelper().getValueFromConfig(Constants.MAIL_FROM);
			String mailTo = Helper.getConfigHelper().getValueFromConfig(Constants.MAIL_TO);
			
			new AmazonSES().send(mailFrom,mailTo,title, body);

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					frame.getContentPane().removeAll();
					JLabel label = new JLabel(
							"<html>メールを送りました。<br>承認待ちです・・・</html>");
					label.setFont(new Font("メイリオ", Font.PLAIN, 24));
					frame.getContentPane().add(label);
					frame.validate();
				}
			});
			return null;//dummy 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
