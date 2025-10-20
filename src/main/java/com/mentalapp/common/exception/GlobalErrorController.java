package com.mentalapp.common.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** アプリケーション全体のエラーを処理するコントローラー */
@Controller
public class GlobalErrorController implements ErrorController {
  /**
   * すべてのエラーをハンドリングし、統一されたエラーページを表示します
   *
   * @param request HTTPリクエスト
   * @param model モデル
   * @return エラーページのビュー名
   */
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    // エラーステータスコードを取得（デフォルトは500）
    Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (Objects.isNull(statusCode)) {
      statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    // エラーメッセージを設定
    String errorMessage = statusCode + " " + "サーバーエラーが発生しました";

    // モデルにエラー情報を追加
    model.addAttribute("errorMessage", errorMessage);

    return "error";
  }
}
