package com.mentalapp.common.exception;

/** データベース操作に関連するエラーを表す例外クラス */
public class MentalSystemException extends Exception {

  /**
   * @param message エラーメッセージ
   * @param cause エラーの原因
   */
  public MentalSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message エラーメッセージ
   */
  public MentalSystemException(String message) {
    super(message);
  }

  /**
   * @param cause エラーの原因
   */
  public MentalSystemException(Throwable cause) {
    super(cause);
  }
}
