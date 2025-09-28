package com.mentalapp.cbt_basic.util;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/** CBT Basicsの共通ユーティリティクラス */
@Component
@RequiredArgsConstructor
public class CbtBasicCommonUtils {

  private final NegativeFeelMapper negativeFeelMapper;
  private final PositiveFeelMapper positiveFeelMapper;
  private final TagMapper tagMapper;
  private final MentalCommonUtils mentalCommonUtils;

  /**
   * エンティティからフォームへの変換
   *
   * @param cbtBasics 変換元のCBT Basicsエンティティ
   * @return 変換後のフォーム
   */
  public CbtBasicsInputForm convertToForm(CbtBasics cbtBasics) {
    // ネガティブ感情IDリストの抽出
    List<Long> negativeFeelIds = extractedNegativeFeelsIdList(cbtBasics);

    // ポジティブ感情IDリストの抽出
    List<Long> positiveFeelIds = extractedPositiveFeelsIdList(cbtBasics);

    // CbtBasicのフォーム作成し、値をセット
    CbtBasicsInputForm form = new CbtBasicsInputForm();
    // モニタリング情報
    form.setCbtBasics(cbtBasics);
    // ネガティブ感情
    form.setNegativeFeelIds(negativeFeelIds);
    // ポジティブ感情
    form.setPositiveFeelIds(positiveFeelIds);

    // タグ情報
    if (Objects.nonNull(cbtBasics.getTags()) && !cbtBasics.getTags().isEmpty()) {
      // tagListのインスタンス生成
      TagList tagList = getTagList(cbtBasics);
      // タグ一覧をセット
      form.setTagNames(tagList.tagNamesToString());
    }

    return form;
  }

  /**
   * CbtBasicsからTagListを作成する
   *
   * @param cbtBasics TagListを作成するCBT Basicsエンティティ
   * @return 作成されたTagList
   */
  public TagList getTagList(CbtBasics cbtBasics) {
    return new TagList(cbtBasics.getTags(), tagMapper);
  }

  /**
   * CBT Basicsからネガティブ感情IDリストを抽出
   *
   * @param cbtBasics 抽出元のCBT Basicsエンティティ
   * @return ネガティブ感情IDのリスト、関連付けがない場合はnull
   */
  private List<Long> extractedNegativeFeelsIdList(CbtBasics cbtBasics) {
    if (Objects.isNull(cbtBasics.getNegativeFeels()) || cbtBasics.getNegativeFeels().isEmpty()) {
      return null;
    }

    return cbtBasics.getNegativeFeels().stream().map(NegativeFeel::getId).toList();
  }

  /**
   * CBT Basicsからポジティブ感情IDリストを抽出
   *
   * @param cbtBasics 抽出元のCBT Basicsエンティティ
   * @return ポジティブ感情IDのリスト、関連付けがない場合はnull
   */
  private List<Long> extractedPositiveFeelsIdList(CbtBasics cbtBasics) {
    if (Objects.isNull(cbtBasics.getPositiveFeels()) || cbtBasics.getPositiveFeels().isEmpty()) {
      return null;
    }

    return cbtBasics.getPositiveFeels().stream().map(PositiveFeel::getId).toList();
  }

  /**
   * 感情一覧表示ビューデータを作成
   *
   * @return 作成されたビューデータ
   */
  public CbtBasicsViewData createAllFeelsViewData() {
    // 感情データを取得
    List<NegativeFeel> negativeFeels = negativeFeelMapper.selectAll();
    List<PositiveFeel> positiveFeels = positiveFeelMapper.selectAll();

    // ビューデータにセット
    CbtBasicsViewData viewData = new CbtBasicsViewData();
    viewData.setNegativeFeels(negativeFeels);
    viewData.setPositiveFeels(positiveFeels);

    return viewData;
  }

  /**
   * CBT Basicsのアクセス権チェック
   *
   * @param cbtBasics チェック対象のCBT Basics
   * @return アクセス可能な場合はtrue、それ以外はfalse
   */
  public Boolean checkAccessPermission(CbtBasics cbtBasics) {
    // NULLチェック
    if (Objects.isNull(cbtBasics)) {
      return false;
    }

    // モニタリング情報へのユーザへのアクセス権チェック
    return mentalCommonUtils.isAuthorized(cbtBasics.getUserId());
  }

  /**
   * バリデーションエラーチェック
   *
   * @param bindingResult バリデーション結果
   * @return エラーがある場合はtrue、それ以外はfalse
   */
  public Boolean checkValidationError(BindingResult bindingResult) {
    // バリデーションエラーがある場合
    return bindingResult.hasErrors();
  }
}
