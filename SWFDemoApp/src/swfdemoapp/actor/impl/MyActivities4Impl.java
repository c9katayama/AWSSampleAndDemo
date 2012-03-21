package swfdemoapp.actor.impl;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import swfdemoapp.actor.InputData;
import swfdemoapp.actor.MyActivities4;



public class MyActivities4Impl implements MyActivities4 {

	private JFrame frame;
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void showResult(InputData inputData,String result) {
		System.out.println("showResult");
		
		JLabel label =new JLabel("Have Fun!!");
		label.setFont(new Font("メイリオ",Font.PLAIN,30));
		frame.getContentPane().setBackground(Color.GREEN);
		if("OK".equals(result)==false){
			label.setText("Work Hard!!");
			frame.getContentPane().setBackground(Color.RED);
		}
		frame.getContentPane().add(label);
		frame.validate();

	}

}
