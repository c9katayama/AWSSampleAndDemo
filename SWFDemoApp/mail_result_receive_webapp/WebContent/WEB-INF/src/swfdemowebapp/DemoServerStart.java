package swfdemowebapp;

import sdloader.SDLoader;
import sdloader.javaee.WebAppContext;

/**
 * デモ用のWebアプリ実行のサーブレットを実行
 * @author c9katayama
 *
 */
public class DemoServerStart {

	public static void main(String[] args) {

		SDLoader loader = new SDLoader(8080);
		WebAppContext ctx = new WebAppContext("/", "mail_result_receive_webapp/WebContent");
		loader.addWebAppContext(ctx);
		loader.setAutoPortDetect(false);

		loader.start();
	}

}
