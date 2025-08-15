# アプリケーション名
自分でできる心理療法

# アプリケーション概要
認知行動療法によるセルフケアができる。

# URL
[https://mental-app.onrender.com](https://mental-app.onrender.com/)

# テスト用アカウント
- Basic認証ID：admin
- Basic認証パスワード：mentalapp
- メールアドレス：test@test.com
- パスワード：test00

# 利用方法
## セルフケア
- トップページのヘッダーからユーザー新規登録を行う。
- 「エクササイズをする」ボタンを押し、各質問に沿って項目を入力する。

# アプリケーションを作成した背景
近頃「モヤモヤする力」が注目されている。  
ビジネスシーンでは、効率的に結論を出すことが重要視される。  
この延長線上で、日常生活でも答えを出すことが求められる面がある。  
しかし、人生における生き方や人間関係、恋愛などの悩みは答えが出ない問題である。  
答えが出ない問題に対して、「悩むのは当たり前」という前提で、問題を分析し、対処する方法を世の中に提供したい、という思いからアプリを開発した。

# 実装予定の機能
- レスポンシブデザイン対応
- SNSアカウント連携機能

# データベース設計
[![Image from Gyazo](https://i.gyazo.com/c1c49596c21ee129d376aecc1515f4f0.png)](https://gyazo.com/c1c49596c21ee129d376aecc1515f4f0)

# 画面遷移図
[![Image from Gyazo](https://i.gyazo.com/f459b2c37c729f503ab1e5bbc7348334.png)](https://gyazo.com/f459b2c37c729f503ab1e5bbc7348334)

# 開発環境
- フロントエンド：HTML/CSS、JavaScript
- バックエンド：Ruby on Rails
- テスト：Rspec

# ローカルでの動作方法
以下のコマンドを順に実行。

```ruby
% git clone https://github.com/iguanakun/mental-app.git
% cd mental-app
% bundle install
```

# 工夫したポイント
- 重要な個人情報を扱うので、Rails 7のActiveRecordEncryptionを利用し、データベースのデータを暗号化した。
- メモのバリデーションは「いずれかひとつの入力を必須」とし、全ての項目が思いつかなくても記入できるようにした。