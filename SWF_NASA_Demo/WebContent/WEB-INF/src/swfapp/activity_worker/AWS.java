package swfapp.activity_worker;

import swfapp.activity_worker.data.ProcessingResultA;
import swfapp.activity_worker.data.ProcessingResultB;
import swfapp.activity_worker.data.ProcessingResultC;
import swfapp.activity_worker.data.ProcessingResultD;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;

@Activities(version = "1.0")
@ActivityRegistrationOptions(defaultTaskStartToCloseTimeoutSeconds = 120, defaultTaskScheduleToStartTimeoutSeconds = 60)
public interface AWS {

	ProcessingResultA dataProcessingA(String imagePath);

	ProcessingResultB dataProcessingB(String imagePath);

	ProcessingResultC dataProcessingC(String imagePath);
	
	ProcessingResultD dataProcessingD(ProcessingResultC resultC);
}
