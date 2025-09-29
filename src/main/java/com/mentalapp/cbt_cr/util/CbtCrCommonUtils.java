package com.mentalapp.cbt_cr.util;

import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.dao.DistortionListMapper;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/** 認知再構成法の共通ユーティリティクラス */
@Component
@RequiredArgsConstructor
public class CbtCrCommonUtils {

  private final CbtCrMapper cbtCrMapper;
  private final NegativeFeelMapper negativeFeelMapper;
  private final PositiveFeelMapper positiveFeelMapper;
  private final DistortionListMapper distortionListMapper;
  private final TagMapper tagMapper;
  private final MentalCommonUtils mentalCommonUtils;

  /**
   * バリデーションエラーをチェックする
   *
   * @param bindingResult バインディング結果
   * @return エラーがある場合はtrue
   */
  public Boolean checkValidationError(BindingResult bindingResult) {
    return bindingResult.hasErrors();
  }

  /**
   * アクセス権限をチェックする
   *
   * @param cbtCr 認知再構成法エンティティ
   * @return アクセス権限がある場合はtrue
   */
  public Boolean checkAccessPermission(CbtCr cbtCr) {
    return mentalCommonUtils.isAuthorized(cbtCr.getUserId());
  }

  /**
   * 全ての感情と思考の歪みを含むビューデータを作成する
   *
   * @return ビューデータ
   */
  public CbtCrViewData createAllFeelsAndDistortionsViewData() {
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setNegativeFeels(negativeFeelMapper.selectAll());
    viewData.setPositiveFeels(positiveFeelMapper.selectAll());
    viewData.setDistortionLists(distortionListMapper.findAll());
    return viewData;
  }

  /**
   * 全ての感情を含むビューデータを作成する
   *
   * @return ビューデータ
   */
  public CbtCrViewData createAllFeelsViewData() {
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setNegativeFeels(negativeFeelMapper.selectAll());
    viewData.setPositiveFeels(positiveFeelMapper.selectAll());
    return viewData;
  }

  /**
   * 全ての思考の歪みを含むビューデータを作成する
   *
   * @return ビューデータ
   */
  public CbtCrViewData createAllDistortionsViewData() {
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setDistortionLists(distortionListMapper.findAll());
    return viewData;
  }

  /**
   * CbtCrオブジェクトの存在確認とアクセス権限チェックを行う（関連する感情情報を含む）
   *
   * @param id 認知再構成法のID
   * @return 権限チェック結果（成功時はCbtCrオブジェクト、失敗時はnull）
   */
  public CbtCr validateAccessPermission(Long id) {
    // モニタリング情報を取得
    CbtCr cbtCr = cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(id);

    // 存在チェック
    if (Objects.isNull(cbtCr)) {
      return null;
    }

    // アクセス権限チェック
    if (!checkAccessPermission(cbtCr)) {
      return null;
    }

    return cbtCr;
  }

  /**
   * CbtCrからTagListを作成する
   *
   * @param cbtCr TagListを作成する認知再構成法エンティティ
   * @return 作成されたTagList
   */
  public TagList getTagList(CbtCr cbtCr) {
    return new TagList(cbtCr.getTags(), tagMapper);
  }

  /**
   * CbtCrエンティティから思考の歪みIDリストを抽出する
   *
   * @param cbtCr 認知再構成法エンティティ
   * @return 思考の歪みIDのリスト、または歪みがない場合はnull
   */
  public List<Long> extractDistortionIds(CbtCr cbtCr) {
    if (Objects.isNull(cbtCr.getDistortionLists())) {
      return null;
    }

    return cbtCr.getDistortionLists().stream().map(DistortionList::getId).toList();
  }

  /**
   * CbtCrエンティティからタグ名の文字列を抽出する
   *
   * @param cbtCr 認知再構成法エンティティ
   * @return タグ名の文字列、またはタグがない場合はnull
   */
  public String extractTagNamesToString(CbtCr cbtCr) {
    if (Objects.isNull(cbtCr.getTags())) {
      return null;
    }
    TagList tagList = getTagList(cbtCr);
    return tagList.tagNamesToString();
  }
}
