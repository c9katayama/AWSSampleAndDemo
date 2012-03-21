package swfdemoapp.actor.impl;

import swfdemoapp.actor.InputData;
import swfdemoapp.actor.MyActivities1Client;
import swfdemoapp.actor.MyActivities1ClientImpl;
import swfdemoapp.actor.MyActivities2Client;
import swfdemoapp.actor.MyActivities2ClientImpl;
import swfdemoapp.actor.MyActivities3Client;
import swfdemoapp.actor.MyActivities3ClientImpl;
import swfdemoapp.actor.MyActivities4Client;
import swfdemoapp.actor.MyActivities4ClientImpl;
import swfdemoapp.actor.MyWorkflow;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Task;

public class MyWorkflowImpl implements MyWorkflow {

	private MyActivities1Client activities1 = new MyActivities1ClientImpl();
	private MyActivities2Client activities2 = new MyActivities2ClientImpl();
	private MyActivities3Client activities3 = new MyActivities3ClientImpl();
	private MyActivities4Client activities4 = new MyActivities4ClientImpl();

	@Override
	public void processStart(final InputData inputData) {
		System.out.println("decision");

		Promise<Void> logResult = activities1.writeStartLog(inputData);

		final Promise<Boolean> confirmManagerResult = activities2
				.confirmManager(logResult);
		final Promise<Boolean> confirmRegalResult = activities2
				.confirmLegal(logResult);

		// Promiseの内容を使って処理をしたい場合は、その処理をTask内に入れるか
		// もしくは@Asynchronousアノテーションをつけたメソッド内で処理を実行する
		new Task(confirmManagerResult, confirmRegalResult) {
			@Override
			protected void doExecute() throws Throwable {
				if (confirmManagerResult.get() == true
						&& confirmRegalResult.get() == true) {
					Promise<String> result = activities3
							.sendConfirmMail(inputData);
					// APTで生成されるクラスの引数が2つの場合、1つの変数がPromiseでほかが実際の変数だと、うまく
					// データを渡せないため、この場合は実際の変数をPromise.asPromiseを使ってPromiseで包む
					Promise<InputData> input = Promise.asPromise(inputData);
					activities4.showResult(input, result);
				} else {

				}
			}
		};
	}

	// @Asynchronous
	// private void step2(Promise<Boolean> confirmManagerResult,
	// Promise<Boolean> confirmRegalResult) {
	// }
}
