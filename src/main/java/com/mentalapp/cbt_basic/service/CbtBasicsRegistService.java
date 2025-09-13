package com.mentalapp.cbt_basic.service;

import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsPositiveFeelMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsTagRelationMapper;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.util.CbtBasicsTagRelationOperator;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.util.EntityTagProcessor;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import com.mentalapp.user_memo_list.data.MemoListConst;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/** CBT Basicsの登録・更新・削除機能を提供するサービスクラス */
@Service
@RequiredArgsConstructor
public class CbtBasicsRegistService {

  private final CbtBasicsMapper cbtBasicsMapper;
  private final CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper;
  private final CbtBasicsPositiveFeelMapper cbtBasicsPositiveFeelMapper;
  private final CbtBasicsTagRelationMapper cbtBasicsTagRelationMapper;
  private final TagMapper tagMapper;
  private final MentalCommonUtils mentalCommonUtils;
  private final CbtBasicCommonUtils cbtBasicCommonUtils;
  private final EntityTagProcessor entityTagProcessor;
  private final CbtBasicsTagRelationOperator tagRelationOperator;

  /**
   * 新規作成処理
   *
   * @param form フォームデータ
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @return 遷移先のパス
   */
  public String processRegist(CbtBasicsInputForm form, BindingResult bindingResult, Model model) {
    // バリデーションエラーがある場合
    if (hasValidationError(form, bindingResult, model)) {
      return CbtBasicsConst.NEW_PATH;
    }

    // フォームからモニタリング情報を取得
    CbtBasics cbtBasics = form.getCbtBasics();

    // ログインユーザーの取得
    Long userId = mentalCommonUtils.getUser().getId();
    cbtBasics.setUserId(userId);

    // 保存（ネガティブ感情、ポジティブ感情、タグの関連付けも行う）
    save(cbtBasics, form, userId);

    return MemoListConst.REDIRECT_MEMOS;
  }

  /**
   * 新規CBT Basicsを保存
   *
   * @param cbtBasics 保存するCBT Basics
   * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
   * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
   * @param tagNames スペース区切りのタグ名
   * @return 保存されたCBT Basics
   */
  @Transactional
  public CbtBasics save(CbtBasics cbtBasics, CbtBasicsInputForm form, Long userId) {
    // ネガティブ感情IDリスト
    List<Long> negativeFeelIds = form.getNegativeFeelIds();
    // ポジティブ感情IDリスト
    List<Long> positiveFeelIds = form.getPositiveFeelIds();
    // タグ名一覧
    String tagNames = form.getTagNames();

    // モニタリングの保存
    cbtBasicsMapper.insert(cbtBasics);

    // ネガティブ感情の中間テーブルへの関連付け
    insertNegativeFeelJoinTable(cbtBasics, negativeFeelIds);

    // ポジティブ感情の中間テーブルへの関連付け
    insertPositiveFeelsJoinTable(cbtBasics, positiveFeelIds);

    // タグの保存
    if (Objects.nonNull(tagNames)) {
      // タグエンティティリストを作成
      TagList tagList = new TagList(tagNames, userId, tagMapper);
      // タグを新規登録（未作成のタグのみ）
      tagList.insertTagList();
      // タグIDリストを抽出
      List<Long> tagIdList = tagList.extractTagIdList();
      // タグの中間テーブルへの関連付け
      tagIdList.forEach(tagId -> cbtBasicsTagRelationMapper.insert(cbtBasics.getId(), tagId));
    }

    // タグ処理を追加（ユーティリティクラスを使用）
    //    entityTagProcessor.processEntityTags(
    //        cbtBasics.getId(), tagNames, cbtBasics.getUserId(), tagRelationOperator);

    return cbtBasics;
  }

  /**
   * ネガティブ感情の中間テーブルへの関連付けを行う
   *
   * @param cbtBasics 関連付け対象のCBT Basicsエンティティ
   * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
   */
  private void insertNegativeFeelJoinTable(CbtBasics cbtBasics, List<Long> negativeFeelIds) {
    // ネガティブ感情が入力されているときのみ、処理を実施
    if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
      negativeFeelIds.forEach(
          negativeFeelId -> cbtBasicsNegativeFeelMapper.insert(cbtBasics.getId(), negativeFeelId));
    }
  }

  /**
   * ポジティブ感情の中間テーブルへの関連付けを行う
   *
   * @param cbtBasics 関連付け対象のCBT Basicsエンティティ
   * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
   */
  private void insertPositiveFeelsJoinTable(CbtBasics cbtBasics, List<Long> positiveFeelIds) {
    // ポジティブ感情が入力されているときのみ、処理を実施
    if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
      positiveFeelIds.forEach(
          positiveFeelId -> cbtBasicsPositiveFeelMapper.insert(cbtBasics.getId(), positiveFeelId));
    }
  }

  /**
   * 更新処理
   *
   * @param form フォームデータ
   * @return 遷移先のパス
   */
  public String processUpdate(
      CbtBasicsInputForm form, BindingResult bindingResult, Model model, Long id) {
    // バリデーションエラーがある場合
    if (hasValidationError(form, bindingResult, model)) {
      return CbtBasicsConst.EDIT_PATH;
    }

    // アクセス権チェック
    if (!mentalCommonUtils.isAuthorized(form.getUserId())) {
      return MentalCommonUtils.REDIRECT_TOP_PAGE;
    }

    // モニタリング情報の取得
    CbtBasics cbtBasics = form.getCbtBasics();
    // モニタリングIDの設定
    cbtBasics.setId(id);

    // 更新
    update(cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds(), form.getTagNames());

    return MemoListConst.REDIRECT_MEMOS;
  }

  private Boolean hasValidationError(
      CbtBasicsInputForm form, BindingResult bindingResult, Model model) {
    // バリデーションエラーがある場合
    if (cbtBasicCommonUtils.checkValidationError(bindingResult) || !form.hasAnyContent()) {
      // ビューデータを作成して追加
      CbtBasicsViewData viewData = cbtBasicCommonUtils.createAllFeelsViewData();
      model.addAttribute("viewData", viewData);
      return true;
    }
    return false;
  }

  /**
   * 既存のCBT Basicsを更新
   *
   * @param cbtBasics 更新するCBT Basics
   * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
   * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
   * @param tagNames スペース区切りのタグ名
   * @return 更新されたCBT Basics
   */
  @Transactional
  public CbtBasics update(
      CbtBasics cbtBasics,
      List<Long> negativeFeelIds,
      List<Long> positiveFeelIds,
      String tagNames) {
    // 既存の感情テーブルの関連を削除
    cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(cbtBasics.getId());
    cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(cbtBasics.getId());
    cbtBasicsTagRelationMapper.deleteByCbtBasicId(cbtBasics.getId());

    // 更新
    cbtBasicsMapper.updateByPrimaryKey(cbtBasics);

    // ネガティブ感情の関連付け
    insertNegativeFeelJoinTable(cbtBasics, negativeFeelIds);

    // ポジティブ感情の関連付け
    insertPositiveFeelsJoinTable(cbtBasics, positiveFeelIds);

    // タグ処理（ユーティリティクラスを使用）
    entityTagProcessor.processEntityTags(
        cbtBasics.getId(), tagNames, cbtBasics.getUserId(), tagRelationOperator);

    return cbtBasics;
  }

  /**
   * 削除処理と権限チェック
   *
   * @param id CBT BasicsのID
   * @return 遷移先のパス
   */
  public String processDelete(Long id) {
    // 削除対象の取得
    CbtBasics cbtBasics = cbtBasicsMapper.selectByPrimaryKey(id);
    // アクセス権チェック
    if (!cbtBasicCommonUtils.checkAccessPermission(cbtBasics)) {
      return MentalCommonUtils.REDIRECT_TOP_PAGE;
    }

    // 削除
    deleteCbtBasics(id);
    return MemoListConst.REDIRECT_MEMOS;
  }

  /**
   * 指定されたIDのCBT Basicsを削除
   *
   * @param id 削除するCBT BasicsのID
   */
  @Transactional
  public void deleteCbtBasics(Long id) {
    // 関連するネガティブ感情、ポジティブ感情、タグの関連を削除
    cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(id);
    cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(id);
    cbtBasicsTagRelationMapper.deleteByCbtBasicId(id);

    // CBT Basicsを削除
    cbtBasicsMapper.deleteByPrimaryKey(id);
  }
}
