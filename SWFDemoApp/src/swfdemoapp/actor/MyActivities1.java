package swfdemoapp.actor;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;


@Activities(version = "1.0")
@ActivityRegistrationOptions(defaultTaskStartToCloseTimeoutSeconds = 120, defaultTaskScheduleToStartTimeoutSeconds = 60)
public interface MyActivities1 {

	// アノテーションがなくても、自動的にActivity登録される。アノテーションつけると指定の名前にできる
	@Activity(name = "writeStartLog")
	void writeStartLog(InputData data);

}
