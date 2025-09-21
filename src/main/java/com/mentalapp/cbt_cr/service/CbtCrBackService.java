package com.mentalapp.cbt_cr.service;

import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/** 認知再構成法のステップ2から戻る処理を行うサービス */
@Service
@RequiredArgsConstructor
@Slf4j
public class CbtCrBackService {

  private final HttpSession session;
  private final CbtCrCommonUtils cbtCrCommonUtils;

  /**
   * ステップ2からステップ1に戻る処理
   *
   * @param model モデル
   * @return ビュー名
   */
  public String processBackToStep1(Model model) {
    //    // セッションからステップ1の入力値を取得してフォームにセット
    //    CbtCrInputForm form = createFormFromSession();
    //
    //    // ビューデータを作成
    //    CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsViewData();
    //
    //    // モデルに追加
    //    model.addAttribute("viewData", viewData);
    //    model.addAttribute("cbtCrForm", form);

    return CbtCrConst.REDIRECT_NEW_PATH;
  }

  /**
   * 編集時にステップ2からステップ1に戻る処理
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  public String processBackToEditStep1(Long id, Model model) {
    return CbtCrConst.REDIRECT_PREFIX + id + CbtCrConst.EDIT_SUFFIX;
  }

  /**
   * セッションからフォームデータを作成
   *
   * @return 作成されたフォーム
   */
  private CbtCrInputForm createFormFromSession() {
    CbtCrInputForm form = new CbtCrInputForm();

    // ステップ1の入力値をセット
    form.setFact((String) session.getAttribute("fact"));
    form.setMind((String) session.getAttribute("mind"));
    form.setNegativeFeelIds((List<Long>) session.getAttribute("negativeFeelIds"));
    form.setPositiveFeelIds((List<Long>) session.getAttribute("positiveFeelIds"));

    return form;
  }
}
