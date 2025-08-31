package com.mentalapp.user_memo_list.controller;

import com.mentalapp.common.entity.User;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.user_memo_list.data.MemoListConst;
import com.mentalapp.user_memo_list.service.MemoListIndexService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** メモ一覧のコントローラークラス */
@Controller
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoListController {

  private final MentalCommonUtils mentalCommonUtils;
  private final MemoListIndexService memoListIndexService;

  /**
   * メモ一覧表示
   *
   * @param model モデル
   * @return メモ一覧ページのビュー名
   */
  @GetMapping
  public String index(Model model) {
    // ログインユーザーの取得
    User currentUser = mentalCommonUtils.getUser();

    // ユーザーのメモデータを取得
    Map<String, Object> userData = memoListIndexService.getUserMemoList(currentUser.getId());

    // モデルに追加
    model.addAllAttributes(userData);

    return MemoListConst.INDEX_PATH;
  }
}
