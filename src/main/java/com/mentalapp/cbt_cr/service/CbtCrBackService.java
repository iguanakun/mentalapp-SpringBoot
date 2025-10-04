package com.mentalapp.cbt_cr.service;

import com.mentalapp.cbt_cr.data.CbtCrConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/** 認知再構成法のステップ2から戻る処理を行うサービス */
@Service
@RequiredArgsConstructor
@Slf4j
public class CbtCrBackService {

  /**
   * ステップ2からステップ1に戻る処理
   *
   * @param model モデル
   * @return ビュー名
   */
  public String processBackToStep1(Model model) {
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
}
