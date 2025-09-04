package com.mentalapp.cbt_cr.service;

import com.mentalapp.cbt_cr.dao.CbtCrDistortionRelationMapper;
import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.dao.CbtCrNegativeFeelMapper;
import com.mentalapp.cbt_cr.dao.CbtCrPositiveFeelMapper;
import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.entity.CbtCrDistortionRelation;
import com.mentalapp.cbt_cr.entity.CbtCrNegativeFeel;
import com.mentalapp.cbt_cr.entity.CbtCrPositiveFeel;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.user_memo_list.data.MemoListConst;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/** 認知再構成法の登録・更新・削除処理を行うサービスクラス */
@Service
@RequiredArgsConstructor
public class CbtCrRegistService {

  private final CbtCrMapper cbtCrMapper;
  private final CbtCrNegativeFeelMapper cbtCrNegativeFeelMapper;
  private final CbtCrPositiveFeelMapper cbtCrPositiveFeelMapper;
  private final CbtCrDistortionRelationMapper cbtCrDistortionRelationMapper;
  private final CbtCrCommonUtils cbtCrCommonUtils;
  private final MentalCommonUtils mentalCommonUtils;
  private final HttpSession session;

  /**
   * 新規登録処理
   *
   * @param form 入力フォーム
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @return 遷移先のパス
   */
  public String processRegist(CbtCrInputForm form, BindingResult bindingResult, Model model) {
    // バリデーションエラーチェック
    if (hasValidationError(form, bindingResult, model)) {
      return CbtCrConst.STEP2_PATH;
    }

    // セッションからデータを取得
    List<Long> negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
    List<Long> positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");

    // フォームからデータを取得
    List<Long> distortionIds = form.getDistortionIds();

    // モニタリング情報を作成
    CbtCr cbtCr = createCbtCr(form);

    try {
      // 登録
      insert(cbtCr, negativeFeelIds, positiveFeelIds, distortionIds);

      // 登録成功後にセッションデータを削除
      clearSessionData();

      return MemoListConst.REDIRECT_MEMOS;
    } catch (Exception e) {
      // エラー処理
      // ログ出力
      System.err.println("認知再構成法の登録中にエラーが発生しました: " + e.getMessage());
      e.printStackTrace();

      // ユーザーへのフィードバック
      model.addAttribute("errorMessage", "登録処理中にエラーが発生しました。もう一度お試しください。");

      // ビューデータを再作成して追加
      CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsAndDistortionsViewData();
      model.addAttribute("viewData", viewData);

      // セッションデータは削除せず、エラー画面を表示
      return CbtCrConst.STEP2_PATH;
    }
  }

  /**
   * 更新処理
   *
   * @param form 入力フォーム
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @param id 認知再構成法のID
   * @return 遷移先のパス
   */
  public String processUpdate(
      CbtCrInputForm form, BindingResult bindingResult, Model model, Long id) {
    // バリデーションエラーチェック
    if (hasValidationError(form, bindingResult, model)) {
      return CbtCrConst.EDIT_STEP2_PATH;
    }

    // セッションからデータを取得
    List<Long> negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
    List<Long> positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");

    // フォームからデータを取得
    List<Long> distortionIds = form.getDistortionIds();

    // モニタリング情報を作成
    CbtCr cbtCr = createCbtCr(form);

    try {
      // 更新
      update(cbtCr, negativeFeelIds, positiveFeelIds, distortionIds);

      // 更新成功後にセッションデータを削除
      clearSessionData();

      return MemoListConst.REDIRECT_MEMOS;
    } catch (Exception e) {
      // エラー処理
      // ログ出力
      System.err.println("認知再構成法の更新中にエラーが発生しました: " + e.getMessage());
      e.printStackTrace();

      // ユーザーへのフィードバック
      model.addAttribute("errorMessage", "更新処理中にエラーが発生しました。もう一度お試しください。");

      // ビューデータを再作成して追加
      CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsAndDistortionsViewData();
      model.addAttribute("viewData", viewData);

      // セッションデータは削除せず、エラー画面を表示
      return CbtCrConst.EDIT_STEP2_PATH;
    }
  }

  /**
   * バリデーションエラーチェック
   *
   * @param form 入力フォーム
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @return エラーがある場合はtrue
   */
  private Boolean hasValidationError(
      CbtCrInputForm form, BindingResult bindingResult, Model model) {
    // バリデーションエラーがある場合
    if (cbtCrCommonUtils.checkValidationError(bindingResult) || !hasAnyContent(form)) {
      // ビューデータを作成して追加
      CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsAndDistortionsViewData();
      model.addAttribute("viewData", viewData);
      return true;
    }
    return false;
  }

  /**
   * バリデーション用のメソッド - 入力フォームに有効なデータが含まれているかチェックする
   *
   * @param form 入力フォーム
   * @return いずれかの項目が有効な値で入力されている場合はtrue、すべての項目が空または無効な場合はfalse
   */
  public boolean hasAnyContent(CbtCrInputForm form) {
    // セッションからデータを取得
    List<Long> negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
    List<Long> positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");
    String fact = (String) session.getAttribute("fact");
    String mind = (String) session.getAttribute("mind");

    // フォームからデータを取得
    List<Long> distortionIds = form.getDistortionIds();
    String whyCorrect = form.getWhyCorrect();
    String whyDoubt = form.getWhyDoubt();
    String newThought = form.getNewThought();

    if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(distortionIds) && !distortionIds.isEmpty()) {
      return true;
    }

    return (Objects.nonNull(fact) && !fact.trim().isEmpty())
        || (Objects.nonNull(mind) && !mind.trim().isEmpty())
        || (Objects.nonNull(whyCorrect) && !whyCorrect.trim().isEmpty())
        || (Objects.nonNull(whyDoubt) && !whyDoubt.trim().isEmpty())
        || (Objects.nonNull(newThought) && !newThought.trim().isEmpty());
  }

  /**
   * セッションデータとフォームデータをマージした新しいフォームを作成
   *
   * @param form 元のフォーム
   * @return マージされた新しいフォーム
   */
  private CbtCr createCbtCr(CbtCrInputForm form) {
    // セッションからデータを取得
    String fact = (String) session.getAttribute("fact");
    String mind = (String) session.getAttribute("mind");

    // エンティティの作成
    CbtCr cbtCr = new CbtCr();
    cbtCr.setFact(fact);
    cbtCr.setMind(mind);
    cbtCr.setWhyCorrect(form.getWhyCorrect());
    cbtCr.setWhyDoubt(form.getWhyDoubt());
    cbtCr.setNewThought(form.getNewThought());
    cbtCr.setUserId(mentalCommonUtils.getUser().getId());

    return cbtCr;
  }

  /** セッションデータを削除 */
  private void clearSessionData() {
    session.removeAttribute("negativeFeelIds");
    session.removeAttribute("positiveFeelIds");
    session.removeAttribute("fact");
    session.removeAttribute("mind");
    session.removeAttribute("cbtCrId");
  }

  /**
   * 新規登録
   *
   * @param cbtCr 認知再構成法エンティティ
   * @param negativeFeelIds ネガティブ感情のIDリスト
   * @param positiveFeelIds ポジティブ感情のIDリスト
   * @param distortionIds 思考の歪みのIDリスト
   */
  @Transactional
  public void insert(
      CbtCr cbtCr,
      List<Long> negativeFeelIds,
      List<Long> positiveFeelIds,
      List<Long> distortionIds) {
    // 登録
    cbtCrMapper.insert(cbtCr);

    // ネガティブ感情の関連付け
    insertNegativeFeelJoinTable(cbtCr, negativeFeelIds);

    // ポジティブ感情の関連付け
    insertPositiveFeelsJoinTable(cbtCr, positiveFeelIds);

    // 思考の歪みの関連付け
    insertDistortionRelationsJoinTable(cbtCr, distortionIds);
  }

  /**
   * ネガティブ感情の関連付け
   *
   * @param cbtCr 認知再構成法
   * @param negativeFeelIds ネガティブ感情のIDリスト
   */
  private void insertNegativeFeelJoinTable(CbtCr cbtCr, List<Long> negativeFeelIds) {
    if (Objects.isNull(negativeFeelIds) || negativeFeelIds.isEmpty()) {
      return;
    }

    for (Long negativeFeelId : negativeFeelIds) {
      CbtCrNegativeFeel cbtCrNegativeFeel = new CbtCrNegativeFeel();
      cbtCrNegativeFeel.setCbtCrId(cbtCr.getId());
      cbtCrNegativeFeel.setNegativeFeelId(negativeFeelId.intValue());
      cbtCrNegativeFeelMapper.insert(cbtCrNegativeFeel);
    }
  }

  /**
   * ポジティブ感情の関連付け
   *
   * @param cbtCr 認知再構成法
   * @param positiveFeelIds ポジティブ感情のIDリスト
   */
  private void insertPositiveFeelsJoinTable(CbtCr cbtCr, List<Long> positiveFeelIds) {
    if (Objects.isNull(positiveFeelIds) || positiveFeelIds.isEmpty()) {
      return;
    }

    for (Long positiveFeelId : positiveFeelIds) {
      CbtCrPositiveFeel cbtCrPositiveFeel = new CbtCrPositiveFeel();
      cbtCrPositiveFeel.setCbtCrId(cbtCr.getId());
      cbtCrPositiveFeel.setPositiveFeelId(positiveFeelId.intValue());
      cbtCrPositiveFeelMapper.insert(cbtCrPositiveFeel);
    }
  }

  /**
   * 思考の歪みの関連付け
   *
   * @param cbtCr 認知再構成法
   * @param distortionIds 思考の歪みのIDリスト
   */
  private void insertDistortionRelationsJoinTable(CbtCr cbtCr, List<Long> distortionIds) {
    if (Objects.isNull(distortionIds) || distortionIds.isEmpty()) {
      return;
    }

    for (Long distortionId : distortionIds) {
      CbtCrDistortionRelation cbtCrDistortionRelation = new CbtCrDistortionRelation();
      cbtCrDistortionRelation.setCbtCrId(cbtCr.getId());
      cbtCrDistortionRelation.setDistortionListId(distortionId);
      cbtCrDistortionRelationMapper.insert(cbtCrDistortionRelation);
    }
  }

  /**
   * 既存のCbtCrを更新
   *
   * @param cbtCr 更新するCbtCr
   * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
   * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
   * @param distortionIds 関連付ける思考の歪みのIDリスト
   */
  @Transactional
  public void update(
      CbtCr cbtCr,
      List<Long> negativeFeelIds,
      List<Long> positiveFeelIds,
      List<Long> distortionIds) {
    // 既存の感情テーブルと思考の歪みテーブルの関連を削除
    cbtCrNegativeFeelMapper.deleteByCbtCrId(cbtCr.getId());
    cbtCrPositiveFeelMapper.deleteByCbtCrId(cbtCr.getId());
    cbtCrDistortionRelationMapper.deleteByCbtCrId(cbtCr.getId());

    // 更新
    cbtCrMapper.updateByPrimaryKey(cbtCr);

    // ネガティブ感情の関連付け
    insertNegativeFeelJoinTable(cbtCr, negativeFeelIds);

    // ポジティブ感情の関連付け
    insertPositiveFeelsJoinTable(cbtCr, positiveFeelIds);

    // 思考の歪みの関連付け
    insertDistortionRelationsJoinTable(cbtCr, distortionIds);
  }

  /**
   * 削除処理と権限チェック
   *
   * @param id 認知再構成法のID
   * @return 遷移先のパス
   */
  public String processDelete(Long id) {
    // 認知再構成法を取得し、アクセス権限をチェック
    CbtCr cbtCr = cbtCrCommonUtils.validateAccessPermission(id);
    if (Objects.isNull(cbtCr)) {
      return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
    }

    // 削除
    delete(id);
    return MemoListConst.REDIRECT_MEMOS;
  }

  /**
   * 指定されたIDの認知再構成法を削除
   *
   * @param id 削除する認知再構成法のID
   */
  @Transactional
  public void delete(Long id) {
    // 関連するネガティブ感情、ポジティブ感情、思考の歪みの関連を削除
    cbtCrNegativeFeelMapper.deleteByCbtCrId(id);
    cbtCrPositiveFeelMapper.deleteByCbtCrId(id);
    cbtCrDistortionRelationMapper.deleteByCbtCrId(id);

    // 認知再構成法を削除
    cbtCrMapper.deleteByPrimaryKey(id);
  }
}
