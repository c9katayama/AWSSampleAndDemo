package util;

public class Processing {

	public static final void execute(long time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			//ignore
		}
	}
}
