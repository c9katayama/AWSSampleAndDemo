

import sdloader.SDLoader;
import sdloader.javaee.WebAppContext;

public class ServerStart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		SDLoader loader = new SDLoader(8080);
		WebAppContext ctx = new WebAppContext("/","WebContent");
		loader.addWebAppContext(ctx);
		loader.setUseOutSidePort(true);
		loader.setAutoPortDetect(false);
		
		loader.start();
		
		//Browser.open("http://localhost:8080/index.html");

	}

}
