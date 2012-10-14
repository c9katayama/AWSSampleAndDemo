package swfapp.activity_worker.impl;

import swfapp.activity_worker.AWS;
import swfapp.activity_worker.data.ProcessingResultA;
import swfapp.activity_worker.data.ProcessingResultB;
import swfapp.activity_worker.data.ProcessingResultC;
import swfapp.activity_worker.data.ProcessingResultD;
import util.Log;
import util.Processing;

public class AWSImpl implements AWS {

	@Override
	public ProcessingResultA dataProcessingA(String imagePath) {
		Log.log("[AWS]dataProcessingA start");
		Processing.execute(3000);
		Log.log("[AWS]dataProcessingA end");
		
		ProcessingResultA resultA = new ProcessingResultA();
		resultA.result = "RESULT A";
		return resultA;
	}

	@Override
	public ProcessingResultB dataProcessingB(String imagePath) {
		Log.log("[AWS]dataProcessingB start");
		Processing.execute(2000);
		Log.log("[AWS]dataProcessingB end");
		
		ProcessingResultB resultB = new ProcessingResultB();
		resultB.result = "RESULT B";
		return resultB;
	}
	
	@Override
	public ProcessingResultC dataProcessingC(String imagePath) {
		Log.log("[AWS]dataProcessingC start");
		Processing.execute(2000);
		Log.log("[AWS]dataProcessingC end");

		ProcessingResultC resultC = new ProcessingResultC();
		resultC.result = "RESULT C";
		return resultC;
	}

	@Override
	public ProcessingResultD dataProcessingD(ProcessingResultC resultC) {
		Log.log("[AWS]dataProcessingD start");
		Processing.execute(2000);
		Log.log("[AWS]dataProcessingD end");
		
		ProcessingResultD resultD = new ProcessingResultD();
		resultC.result = "RESULT D";
		return resultD;
	}
}
