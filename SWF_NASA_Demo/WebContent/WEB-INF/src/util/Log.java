package util;

public class Log {

	public static interface Logger{
		void log(String message);
	}
	public static Logger logger = new Logger(){
		public void log(String message) {
			System.out.println(message);
		};
	};
	
	public static void log(String message){
		logger.log(message);
	}
}
