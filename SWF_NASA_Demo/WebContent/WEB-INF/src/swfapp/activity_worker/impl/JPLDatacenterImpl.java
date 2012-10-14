package swfapp.activity_worker.impl;

import swfapp.activity_worker.JPLDatacenter;
import swfapp.activity_worker.data.CombineResult;
import swfapp.activity_worker.data.ProcessingResultA;
import swfapp.activity_worker.data.ProcessingResultB;
import swfapp.activity_worker.data.ProcessingResultD;
import util.Log;
import util.Processing;

public class JPLDatacenterImpl implements JPLDatacenter {

	@Override
	public String fileTransfer(String srcImagePath) {
		Log.log("[JPL]fileTransfer start");
		Processing.execute(3000);
		Log.log("[JPL]fileTransfer end");

		return "http://adsj-demo.s3.amazonaws.com/swf-curiosity/alien.jpg";
	}

	@Override
	public CombineResult combineResult(ProcessingResultA resultA,
			ProcessingResultB resultB, ProcessingResultD resultD) {
		Log.log("[JPL]combineResult start");
		Processing.execute(2000);
		Log.log("[JPL]combineResult end");

		return null;
	}

	@Override
	public void showResult(CombineResult result) {
		Log.log("[JPL]showResult");
	}

}
