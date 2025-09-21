package com.mentalapp.common.exception;

/** データベース操作に関連するエラーを表す例外クラス */
public class DatabaseException extends RuntimeException {

  /**
   * 指定されたメッセージとエラー原因でDatabaseExceptionを構築
   *
   * @param message エラーメッセージ
   * @param cause エラーの原因
   */
  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 指定されたメッセージでDatabaseExceptionを構築
   *
   * @param message エラーメッセージ
   */
  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(Throwable cause) {
    super(cause);
  }
}
