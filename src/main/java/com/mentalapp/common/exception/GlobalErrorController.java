package com.mentalapp.common.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * アプリケーション全体のエラーを処理するコントローラー
 * すべてのHTTPステータスコードに対して統一されたエラーページを表示します
 */
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
        if (statusCode == null) {
            statusCode = 500;
        }
        
        // エラーメッセージを設定
        String errorMessage;
        switch (statusCode) {
            case 404:
                errorMessage = "ページが見つかりませんでした";
                break;
            case 403:
                errorMessage = "アクセス権限がありません";
                break;
            case 400:
                errorMessage = "不正なリクエストです";
                break;
            default:
                errorMessage = "サーバーエラーが発生しました";
                break;
        }
        
        // モデルにエラー情報を追加
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        
        // 統一されたエラーページを返す
        return "error/error";
    }
}