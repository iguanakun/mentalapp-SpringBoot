package com.mentalapp.cbt_basic.service;

import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsPositiveFeelMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsTagRelationMapper;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.exception.DatabaseException;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import com.mentalapp.user_memo_list.data.MemoListConst;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/** CBT Basicsの登録・更新・削除機能を提供するサービスクラス */
@Service
@RequiredArgsConstructor
@Slf4j
public class CbtBasicsRegistService {

  private final CbtBasicsMapper cbtBasicsMapper;
  private final CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper;
  private final CbtBasicsPositiveFeelMapper cbtBasicsPositiveFeelMapper;
  private final CbtBasicsTagRelationMapper cbtBasicsTagRelationMapper;
  private final TagMapper tagMapper;
  private final MentalCommonUtils mentalCommonUtils;
  private final CbtBasicCommonUtils cbtBasicCommonUtils;

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
   * @param form フォームデータ（ネガティブ感情、ポジティブ感情、タグ情報を含む）
   * @param userId ユーザーID
   * @return 保存されたCBT Basics
   */
  @Transactional
  public CbtBasics save(CbtBasics cbtBasics, CbtBasicsInputForm form, Long userId) {
    try {
      // フォームからデータを取得
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
      saveTags(cbtBasics, userId, tagNames);

      return cbtBasics;
    } catch (DatabaseException e) {
      // 既存のDatabaseExceptionを再スロー
      throw e;
    } catch (Exception e) {
      log.error("CBT Basics保存中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("CBT Basics保存中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * タグを保存し、CBT Basicsとの関連付けを行う
   *
   * @param cbtBasics 関連付け対象のCBT Basicsエンティティ
   * @param userId ユーザーID
   * @param tagNames スペース区切りのタグ名
   * @throws DatabaseException データベース操作中にエラーが発生した場合
   */
  private void saveTags(CbtBasics cbtBasics, Long userId, String tagNames)
      throws DatabaseException {
    // タグが付与されていない場合、処理を終了
    if (Objects.isNull(tagNames)) {
      return;
    }

    try {
      // タグ処理の実行
      // 1. タグエンティティリストを作成
      TagList tagList = new TagList(tagNames, userId, tagMapper);
      // 2. タグを新規登録（未作成のタグのみ）
      tagList.insertTagList();
      // 3. タグの中間テーブルへの関連付け
      tagList.insertMonitoringTagRelation(cbtBasicsTagRelationMapper, cbtBasics.getId());
    } catch (Exception e) {
      log.error("タグ処理中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("タグ処理中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * ネガティブ感情の中間テーブルへの関連付けを行う
   *
   * @param cbtBasics 関連付け対象のCBT Basicsエンティティ
   * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
   */
  private void insertNegativeFeelJoinTable(CbtBasics cbtBasics, List<Long> negativeFeelIds) {
    try {
      // ネガティブ感情が入力されているときのみ、処理を実施
      if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
        negativeFeelIds.forEach(
            negativeFeelId ->
                cbtBasicsNegativeFeelMapper.insert(cbtBasics.getId(), negativeFeelId));
      }
    } catch (Exception e) {
      log.error("ネガティブ感情関連付け中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("ネガティブ感情関連付け中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * ポジティブ感情の中間テーブルへの関連付けを行う
   *
   * @param cbtBasics 関連付け対象のCBT Basicsエンティティ
   * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
   */
  private void insertPositiveFeelsJoinTable(CbtBasics cbtBasics, List<Long> positiveFeelIds) {
    try {
      // ポジティブ感情が入力されているときのみ、処理を実施
      if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
        positiveFeelIds.forEach(
            positiveFeelId ->
                cbtBasicsPositiveFeelMapper.insert(cbtBasics.getId(), positiveFeelId));
      }
    } catch (Exception e) {
      log.error("ポジティブ感情関連付け中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("ポジティブ感情関連付け中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * 更新処理
   *
   * @param form フォームデータ
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @param id 更新対象のCBT BasicsのID
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

  /**
   * バリデーションエラーの有無を確認する
   *
   * @param form フォームデータ
   * @param bindingResult バリデーション結果
   * @param model モデル
   * @return バリデーションエラーがある場合はtrue、ない場合はfalse
   */
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
    try {
      // 中間テーブルの関連を削除
      deleteCbtBasicsRelation(cbtBasics.getId());

      // 更新
      cbtBasicsMapper.updateByPrimaryKey(cbtBasics);

      // ネガティブ感情の関連付け
      insertNegativeFeelJoinTable(cbtBasics, negativeFeelIds);

      // ポジティブ感情の関連付け
      insertPositiveFeelsJoinTable(cbtBasics, positiveFeelIds);

      // タグの保存
      saveTags(cbtBasics, cbtBasics.getUserId(), tagNames);

      return cbtBasics;
    } catch (DatabaseException e) {
      // 既存のDatabaseExceptionを再スロー
      throw e;
    } catch (Exception e) {
      log.error("CBT Basics更新中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("CBT Basics更新中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * CBT Basicsに関連する中間テーブルのデータを削除する
   *
   * @param cbtBasics CBT BasicsのID
   */
  private void deleteCbtBasicsRelation(Long cbtBasics) {
    try {
      // 関連する中間テーブルのデータをすべて削除
      // ネガティブ感情の中間テーブルを削除
      cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(cbtBasics);
      // ポジティブ感情の中間テーブルを削除
      cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(cbtBasics);
      // タグの中間テーブルを削除
      cbtBasicsTagRelationMapper.deleteByMonitoringId(cbtBasics);
    } catch (Exception e) {
      log.error("CBT Basics関連削除中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("CBT Basics関連削除中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * 削除処理と権限チェック
   *
   * @param id CBT BasicsのID
   * @return 遷移先のパス
   */
  public String processDelete(Long id) {
    try {
      // 削除対象の取得
      CbtBasics cbtBasics = cbtBasicsMapper.selectByPrimaryKey(id);
      // アクセス権チェック
      if (!cbtBasicCommonUtils.checkAccessPermission(cbtBasics)) {
        return MentalCommonUtils.REDIRECT_TOP_PAGE;
      }

      // 削除
      deleteCbtBasics(id);
      return MemoListConst.REDIRECT_MEMOS;
    } catch (DatabaseException e) {
      // 既存のDatabaseExceptionを再スロー
      throw e;
    } catch (Exception e) {
      log.error("CBT Basics削除処理中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("CBT Basics削除処理中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * 指定されたIDのCBT Basicsを削除
   *
   * @param id 削除するCBT BasicsのID
   */
  @Transactional
  public void deleteCbtBasics(Long id) {
    try {
      // 関連するネガティブ感情、ポジティブ感情、タグの関連を削除
      deleteCbtBasicsRelation(id);

      // CBT Basicsを削除
      cbtBasicsMapper.deleteByPrimaryKey(id);
    } catch (DatabaseException e) {
      // 既存のDatabaseExceptionを再スロー
      throw e;
    } catch (Exception e) {
      log.error("CBT Basics削除中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("CBT Basics削除中にデータベースエラーが発生しました", e);
    }
  }
}
