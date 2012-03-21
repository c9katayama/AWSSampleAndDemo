package swfdemoapp;

import javax.swing.JFrame;

import swfdemoapp.actor.Constants;
import swfdemoapp.actor.impl.MyActivities1Impl;
import swfdemoapp.actor.impl.MyActivities2Impl;
import swfdemoapp.actor.impl.MyActivities3Impl;
import swfdemoapp.actor.impl.MyActivities4Impl;
import swfdemoapp.utils.Helper;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;

/**
 * アクティビティワーカーを実行します。
 * 
 * このActivityのプロセスが、ワークフロー内でのアクティビティのタスクの実処理を担当します。 起動後、タスクの待ち受け状態になります。
 * 
 * @author c9katayama
 * 
 */
public class MyActivityWorker {

	public static void main(String[] args) throws Exception {

		Helper.setupLAF();
		int startX = 10;
		int startY = 10;
		final JFrame frame1 = new JFrame("休暇申請開始");
		frame1.setLocation(startX + 10, startY + 100);
		frame1.setSize(220, 200);

		final JFrame frame2legal = new JFrame("マネージャー");
		frame2legal.setLocation(startX + 250, startY + 10);
		frame2legal.setSize(200, 200);

		final JFrame frame2manager = new JFrame("総務");
		frame2manager.setLocation(startX + 250, startY + 210);
		frame2manager.setSize(200, 200);

		final JFrame frame3 = new JFrame("社長");
		frame3.setLocation(startX + 500, startY + 100);
		frame3.setSize(250, 200);

		final JFrame frame4 = new JFrame("結果");
		frame4.setLocation(startX + 780, startY + 100);
		frame4.setSize(250, 200);

		frame1.setVisible(true);
		frame2manager.setVisible(true);
		frame2legal.setVisible(true);
		frame3.setVisible(true);
		frame4.setVisible(true);

		// クライアント作成
		AmazonSimpleWorkflow swfClient = Helper.createSWFClient();
		// ドメイン取得
		String domain = Helper.getConfigHelper().getDomain();
		// アクティブワーカークラスをインスタンス化
		ActivityWorker worker = new ActivityWorker(swfClient, domain,
				Constants.ACTIVITIES_LIST_NAME);

		// 実行するアクティビティを登録
		MyActivities1Impl ac1 = new MyActivities1Impl();
		ac1.setFrame(frame1);
		MyActivities2Impl ac2 = new MyActivities2Impl();
		ac2.setFrames(frame2manager, frame2legal);
		MyActivities3Impl ac3 = new MyActivities3Impl();
		ac3.setFrame(frame3);
		MyActivities4Impl ac4 = new MyActivities4Impl();
		ac4.setFrame(frame4);

		worker.addActivitiesImplementation(ac1);
		worker.addActivitiesImplementation(ac2);
		worker.addActivitiesImplementation(ac3);
		worker.addActivitiesImplementation(ac4);

		// タスクの待ち受けを開始
		worker.start();

	}
}
