SWFDemoAppの使い方

このプロジェクトセットは、Eclipseで利用することを前提としています。
Eclipse3.7およびJDK1.6で稼働確認をしています。
AmazonSWF自体の説明およびデモの説明については
http://www.slideshare.net/c95029/introduction-of-amazon-simpleworkflow
を参照して下さい。

1、JDK/プロジェクトの設定
eclipseから Window > Preferences > Java > Installed JREsを選択。
利用しているJRE/JDKを選んで、Editボタンをクリック。
Default Java Arguments に
-javaagent:C:\ファイルパス\lib\aspectjweaver.jar
を設定（このプロジェクトのlibに入っているaspectjweaver.jarのパスを設定）

プロジェクト右クリックから、APTの設定を実施
Java Compiler > Annotation Processing 
 Enable project specific settings, Enable annotation processing, Enable processing in editorにチェック
Java Compiler > Annotation Processing >Factory Path
 Enable project specific settingsにチェック
  Add JARs..を選択し、SWFDemoApp/lib下にあるaws-java-sdk-1.3.3.jar, aws-java-sdk-flow-build-tools-1.3.3.jar, freemarker-2.3.10.jarを選択

1、初期設定
プロジェクト直下の「SETUP-swf-demo.properties」をコピーし、「swf-demo.properties」とリネームします。
リネーム後、以下の項目を変更します。

AWS.Access.ID・・・AWSのアクセスID
AWS.Secret.Key・・・AWSのシークレットキー
mailFrom=途中でメールを送るフローで使う、Fromアドレス
mailTo=途中でメールを送るフローで使う、Toアドレス

2、Amazon SESの設定
AWS Management ConsoleからSESを選択していただき、mailFromとmailToのアドレスを登録します。

3、Webアプリの設定
このデモには、ローカルで動作するアプリケーションサーバが入っていますのでそちらを利用することも
できますが、外部のサーバでアプリケーションを動作させたい場合は、
mail_result_receive_webapp内のbuid.xmlをビルドして、できたwarファイルをデプロイしてください。
また、デプロイしたサーバのアクセスURLを、swf-demo.propertiesに記載して下さい。

4、ドメインの作成
swfdemoapp.CreateDomainクラスを実行すると、ドメインが作られます。
初回一度だけ実行してください。

4、実行
実行するファイルは、以下の４つになります。
swfdemoapp.MyActivityWorker
swfdemoapp.MyDeciderWorker
swfdemoapp.MyWorkflowStarter
swfdemowebapp.DemoServerStart(ローカルでメール受信Webアプリを動作させる場合）
はじめにMyActivityWorker、MyDeciderWorker、DemoServerStartを起動し、
最後にMyWorkflowStarterを実行します。

MyWorkflowStarter起動ごとに、1つのワークフローエグゼキューションが生成されます。

