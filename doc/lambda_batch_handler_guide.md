# Lambdaバッチ呼び出し用ハンドラー実装ガイド

このドキュメントでは、AWS Lambdaでバッチ処理を呼び出すためのJavaハンドラー実装方法について説明します。
ここでは、SampleFunctionというクラスをバッチ処理の本体として利用します。

## 設定箇所

Lambda関数を実装・設定する際に、以下の2点が必要です：

- **lambdahandlerRequestの追加**: `RequestHandler<Map<String,String>, Object>`インターフェースを実装したハンドラークラスを作成します。
- **Lambda本体のハンドラー設定**: AWS Lambda コンソールで、ハンドラー設定に`jp.sample.batch.handler.SampleLambdaFunctionHandler::handleRequest`を指定します。

## 必要なもの

- Java 8 以上
- AWS Lambda Java ランタイム
- 追加ライブラリは不要（AWS Lambda SDKのみでOK）

## 実装例

Lambdaハンドラーは、`com.amazonaws.services.lambda.runtime.RequestHandler`インターフェースを実装したクラスとして作成します。
バッチ処理本体は`SampleFunction`クラスの`main_process`メソッドとして実装します。

```java
package jp.sample.batch.handler;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import jp.sample.batch.SampleFunction;

/**
 * Lambdaのエントリーポイントとなるハンドラークラス
 */
public class SampleLambdaFunctionHandler implements RequestHandler<Map<String,String>, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Lambdaのリクエストを処理
     */
    @Override
    public Object handleRequest(Map<String,String> events, Context context) {
        try {
            SampleFunction.main_process(events, context);
        } catch (Exception e) {
            LOGGER.error("バッチ処理でエラーが発生しました。", e);
            System.exit(1);
        }
        return "End";
    }
}
```

## ポイント

- Lambdaハンドラーは`RequestHandler`を実装する必要があります。
- バッチ本体は`SampleFunction`クラスにまとめてください。
- 追加ライブラリは不要です（標準のAWS Lambda Java SDKのみで動作します）。

## デプロイ・実行

1. **ビルドとデプロイ**：
   - プロジェクトをビルドし、生成されたJARファイルをAWS Lambdaにデプロイします。

2. **ハンドラー設定**：
   - AWSコンソール画面で、Lambda関数のハンドラー名に以下を指定します：
     ```
     jp.sample.batch.handler.SampleLambdaFunctionHandler::handleRequest
     ```

3. **実行プロセス**：
   - Lambda実行時、指定されたハンドラークラスの`handleRequest`メソッドが自動的に呼び出されます。