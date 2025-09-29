package com.mentalapp.cbt_cr.service;

import com.mentalapp.cbt_cr.dao.CbtCrDistortionRelationMapper;
import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.dao.CbtCrNegativeFeelMapper;
import com.mentalapp.cbt_cr.dao.CbtCrPositiveFeelMapper;
import com.mentalapp.cbt_cr.dao.CbtCrTagRelationMapper;
import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.entity.CbtCrDistortionRelation;
import com.mentalapp.cbt_cr.entity.CbtCrNegativeFeel;
import com.mentalapp.cbt_cr.entity.CbtCrPositiveFeel;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.exception.DatabaseException;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import com.mentalapp.user_memo_list.data.MemoListConst;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/** 認知再構成法の登録・更新・削除処理を行うサービスクラス */
@Service
@RequiredArgsConstructor
@Slf4j
public class CbtCrRegistService {

  private final CbtCrMapper cbtCrMapper;
  private final CbtCrNegativeFeelMapper cbtCrNegativeFeelMapper;
  private final CbtCrPositiveFeelMapper cbtCrPositiveFeelMapper;
  private final CbtCrDistortionRelationMapper cbtCrDistortionRelationMapper;
  private final CbtCrTagRelationMapper cbtCrTagRelationMapper;
  private final TagMapper tagMapper;
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
      return CbtCrConst.REDIRECT_NEW_PATH;
    }

    // セッションからデータを取得
    List<Long> negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
    List<Long> positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");

    // フォームからデータを取得
    List<Long> distortionIds = form.getDistortionIds();
    String tagNames = form.getTagNames();

    // モニタリング情報を作成
    CbtCr cbtCr = createCbtCr(form);

    // 登録
    save(cbtCr, negativeFeelIds, positiveFeelIds, distortionIds, tagNames);

    // 登録成功後にセッションデータを削除
    clearSessionData();

    return MemoListConst.REDIRECT_MEMOS;
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
      return CbtCrConst.PREFIX + id + CbtCrConst.EDIT_SUFFIX;
    }

    // セッションからデータを取得
    List<Long> negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
    List<Long> positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");

    // フォームからデータを取得
    List<Long> distortionIds = form.getDistortionIds();
    String tagNames = form.getTagNames();

    // モニタリング情報を作成
    CbtCr cbtCr = createCbtCr(form);
    // IDを明示的に設定
    cbtCr.setId(id);

    // 更新
    update(cbtCr, negativeFeelIds, positiveFeelIds, distortionIds, tagNames);

    // 更新成功後にセッションデータを削除
    clearSessionData();

    return MemoListConst.REDIRECT_MEMOS;
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
    String tagNames = form.getTagNames();

    if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(distortionIds) && !distortionIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(tagNames) && !tagNames.trim().isEmpty()) {
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
    session.removeAttribute("tagNames");
  }

  /**
   * 新規登録
   *
   * @param cbtCr 認知再構成法エンティティ
   * @param negativeFeelIds ネガティブ感情のIDリスト
   * @param positiveFeelIds ポジティブ感情のIDリスト
   * @param distortionIds 思考の歪みのIDリスト
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  @Transactional
  public void save(
      CbtCr cbtCr,
      List<Long> negativeFeelIds,
      List<Long> positiveFeelIds,
      List<Long> distortionIds,
      String tagNames)
      throws DatabaseException {
    // 登録
    cbtCrMapper.insert(cbtCr);

    // ネガティブ感情の関連付け
    insertNegativeFeelJoinTable(cbtCr, negativeFeelIds);

    // ポジティブ感情の関連付け
    insertPositiveFeelsJoinTable(cbtCr, positiveFeelIds);

    // 思考の歪みの関連付け
    insertDistortionRelationsJoinTable(cbtCr, distortionIds);

    // タグの保存と関連付け
    saveTags(cbtCr, cbtCr.getUserId(), tagNames);
  }

  /**
   * ネガティブ感情の関連付け
   *
   * @param cbtCr 認知再構成法
   * @param negativeFeelIds ネガティブ感情のIDリスト
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  private void insertNegativeFeelJoinTable(CbtCr cbtCr, List<Long> negativeFeelIds)
      throws DatabaseException {
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
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  private void insertPositiveFeelsJoinTable(CbtCr cbtCr, List<Long> positiveFeelIds)
      throws DatabaseException {
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
   * タグを保存し、認知再構成法との関連付けを行う
   *
   * @param cbtCr 関連付け対象の認知再構成法エンティティ
   * @param userId ユーザーID
   * @param tagNames スペース区切りのタグ名
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  private void saveTags(CbtCr cbtCr, Long userId, String tagNames) throws DatabaseException {
    // タグが付与されていない場合、処理を終了
    if (Objects.isNull(tagNames)) {
      return;
    }

    // タグ処理の実行
    // 1. タグエンティティリストを作成
    TagList tagList = new TagList(tagNames, userId, tagMapper);
    // 2. タグを新規登録（未作成のタグのみ）
    tagList.insertTagList();
    // 3. タグの中間テーブルへの関連付け
    tagList.insertMonitoringTagRelation(cbtCrTagRelationMapper, cbtCr.getId());
  }

  /**
   * 思考の歪みの関連付け
   *
   * @param cbtCr 認知再構成法
   * @param distortionIds 思考の歪みのIDリスト
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  private void insertDistortionRelationsJoinTable(CbtCr cbtCr, List<Long> distortionIds)
      throws DatabaseException {
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
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  @Transactional
  public void update(
      CbtCr cbtCr,
      List<Long> negativeFeelIds,
      List<Long> positiveFeelIds,
      List<Long> distortionIds,
      String tagNames)
      throws DatabaseException {
    // 既存の感情テーブルと思考の歪みテーブルの関連を削除
    deleteCbtCrRelation(cbtCr.getId());

    // 更新
    cbtCrMapper.updateByPrimaryKey(cbtCr);

    // ネガティブ感情の関連付け
    insertNegativeFeelJoinTable(cbtCr, negativeFeelIds);

    // ポジティブ感情の関連付け
    insertPositiveFeelsJoinTable(cbtCr, positiveFeelIds);

    // 思考の歪みの関連付け
    insertDistortionRelationsJoinTable(cbtCr, distortionIds);

    // タグの保存と関連付け
    saveTags(cbtCr, cbtCr.getUserId(), tagNames);
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
      return MemoListConst.REDIRECT_MEMOS;
    }

    // 削除
    delete(id);
    return MemoListConst.REDIRECT_MEMOS;
  }

  /**
   * 指定されたIDの認知再構成法を削除
   *
   * @param id 削除する認知再構成法のID
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  @Transactional
  public void delete(Long id) throws DatabaseException {
    // 関連する中間テーブルを削除
    deleteCbtCrRelation(id);

    // 認知再構成法を削除
    cbtCrMapper.deleteByPrimaryKey(id);
  }

  private void deleteCbtCrRelation(Long id) {
    // 関連するネガティブ感情、ポジティブ感情、思考の歪み、タグの中間テーブルを削除
    cbtCrNegativeFeelMapper.deleteByCbtCrId(id);
    cbtCrPositiveFeelMapper.deleteByCbtCrId(id);
    cbtCrDistortionRelationMapper.deleteByCbtCrId(id);
    cbtCrTagRelationMapper.deleteByMonitoringId(id);
  }
}
