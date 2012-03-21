SWFDemoAppの使い方

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




次の順番でアプリケーションを起動してください。
