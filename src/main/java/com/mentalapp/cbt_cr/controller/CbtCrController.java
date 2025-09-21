package com.mentalapp.cbt_cr.controller;

import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.service.CbtCrBackService;
import com.mentalapp.cbt_cr.service.CbtCrIndexService;
import com.mentalapp.cbt_cr.service.CbtCrRegistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** 認知再構成法のコントローラークラス */
@Controller
@RequestMapping("/cbt_cr")
@RequiredArgsConstructor
public class CbtCrController {

  private final CbtCrIndexService cbtCrIndexService;
  private final CbtCrRegistService cbtCrRegistService;
  private final CbtCrBackService cbtCrBackService;

  /**
   * 新規作成フォーム表示（ステップ1）
   *
   * @param model モデル
   * @return 新規作成フォームのビュー名
   */
  @GetMapping("/new")
  public String newCbtCr(Model model) {
    return cbtCrIndexService.processNew(model);
  }

  /**
   * ステップ2フォーム表示（リダイレクト用）
   *
   * @param form 入力フォーム（ステップ1の入力内容）
   * @param model モデル
   * @return リダイレクト先のパス
   */
  @GetMapping("/step2")
  public String step2(@ModelAttribute("cbtCrForm") CbtCrInputForm form, Model model) {
    return cbtCrIndexService.nextStep2(form, model);
  }

  /**
   * ステップ2フォーム表示（セッションからデータを取得）
   *
   * @param model モデル
   * @return ステップ2フォームのビュー名
   */
  @GetMapping("/step2_view")
  public String step2View(Model model) {
    return cbtCrIndexService.processStep2(model);
  }

  /**
   * 新規作成処理
   *
   * @param form 入力フォーム
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @return 遷移先のビュー名
   */
  @PostMapping("/regist")
  public String create(
      @Valid @ModelAttribute("cbtCrForm") CbtCrInputForm form,
      BindingResult bindingResult,
      Model model) {
    return cbtCrRegistService.processRegist(form, bindingResult, model);
  }

  /**
   * 詳細表示
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return 詳細ページのビュー名
   */
  @GetMapping("/{id}")
  public String show(@PathVariable Long id, Model model) {
    return cbtCrIndexService.processShow(id, model);
  }

  /**
   * 編集フォーム表示（ステップ1）
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return 編集フォームのビュー名
   */
  @GetMapping("/{id}/edit")
  public String edit(@PathVariable Long id, Model model) {
    return cbtCrIndexService.processEdit(id, model);
  }

  /**
   * 編集フォーム表示（ステップ2）（リダイレクト用）
   *
   * @param id 認知再構成法のID
   * @param form 入力フォーム（ステップ1の入力内容）
   * @param model モデル
   * @return リダイレクト先のパス
   */
  @GetMapping("/{id}/edit_step2")
  public String editStep2(
      @PathVariable Long id, @ModelAttribute("cbtCrForm") CbtCrInputForm form, Model model) {
    return cbtCrIndexService.processEditStep2(id, form, model);
  }

  /**
   * 編集フォーム表示（ステップ2）（セッションからデータを取得）
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ステップ2編集フォームのビュー名
   */
  @GetMapping("/{id}/edit_step2_view")
  public String editStep2View(@PathVariable Long id, Model model) {
    return cbtCrIndexService.processEditStep2FromSession(id, model);
  }

  /**
   * 更新処理
   *
   * @param id 認知再構成法のID
   * @param form 入力フォーム
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @return 遷移先のビュー名
   */
  @PostMapping("/{id}")
  public String update(
      @PathVariable Long id,
      @Valid @ModelAttribute("cbtCrForm") CbtCrInputForm form,
      BindingResult bindingResult,
      Model model) {
    return cbtCrRegistService.processUpdate(form, bindingResult, model, id);
  }

  /**
   * 削除処理
   *
   * @param id 削除する認知再構成法のID
   * @return 遷移先のビュー名
   */
  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id) {
    return cbtCrRegistService.processDelete(id);
  }

  /**
   * ステップ2からステップ1に戻る処理
   *
   * @param model モデル
   * @return ビュー名
   */
  @GetMapping("/back")
  public String backToStep1(Model model) {
    return cbtCrBackService.processBackToStep1(model);
  }

  /**
   * 編集時にステップ2からステップ1に戻る処理
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  @GetMapping("/{id}/back")
  public String backToEditStep1(@PathVariable Long id, Model model) {
    return cbtCrBackService.processBackToEditStep1(id, model);
  }
}
