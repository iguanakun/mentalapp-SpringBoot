package com.mentalapp.common.util;

/** タグ処理中に発生する例外 */
public class TagProcessingException extends RuntimeException {

  /**
   * メッセージを指定して例外を作成
   *
   * @param message エラーメッセージ
   */
  public TagProcessingException(String message) {
    super(message);
  }

  /**
   * メッセージと原因となる例外を指定して例外を作成
   *
   * @param message エラーメッセージ
   * @param cause 原因となる例外
   */
  public TagProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
