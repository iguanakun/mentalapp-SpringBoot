package com.mentalapp.cbt_cr.util;

import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.dao.DistortionListMapper;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.util.MentalCommonUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/** 認知再構成法の共通ユーティリティクラス */
@Component
@RequiredArgsConstructor
public class CbtCrCommonUtils {

  private final NegativeFeelMapper negativeFeelMapper;
  private final PositiveFeelMapper positiveFeelMapper;
  private final DistortionListMapper distortionListMapper;
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
    if (Objects.isNull(cbtCr)) {
      return false;
    }
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
}
