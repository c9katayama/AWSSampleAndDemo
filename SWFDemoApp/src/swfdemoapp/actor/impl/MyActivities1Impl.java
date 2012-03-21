package swfdemoapp.actor.impl;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import swfdemoapp.actor.InputData;
import swfdemoapp.actor.MyActivities1;

public class MyActivities1Impl implements MyActivities1 {

	private JFrame frame;

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void writeStartLog(InputData input) {
		System.out.println("ワークフロー開始");
		JLabel label = new JLabel("<html>休暇申請開始:<br>申請者:"+input.getName()+"<br>申請日:"+input.getDate()+"</html>");
		label.setFont(new Font("メイリオ", Font.PLAIN, 24));
		frame.getContentPane().add(label);
		frame.validate();
	}

}
