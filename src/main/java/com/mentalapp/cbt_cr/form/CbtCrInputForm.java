package com.mentalapp.cbt_cr.form;

import com.mentalapp.cbt_cr.entity.CbtCr;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/** 認知再構成法のフォームデータを扱うクラス */
@Data
public class CbtCrInputForm {

  /** 認知再構成法エンティティ */
  private CbtCr cbtCr;

  /** タグ名（スペース区切り） */
  private String tagNames;

  /** ネガティブ感情のID */
  private List<Long> negativeFeelIds;

  /** ポジティブ感情のID */
  private List<Long> positiveFeelIds;

  /** 思考の歪みのID */
  private List<Long> distortionIds;

  /**
   * バリデーション用のメソッド - いずれかの項目が入力されているか
   *
   * @return いずれかの項目が入力されている場合はtrue
   */
  public boolean hasAnyContent() {
    // ネガティブ感情、ポジティブ感情、認知の歪みのチェック
    if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(distortionIds) && !distortionIds.isEmpty()) {
      return true;
    }

    // タグ名のチェック
    if (Objects.nonNull(tagNames) && !tagNames.trim().isEmpty()) {
      return true;
    }

    // エンティティのフィールドチェック
    return Objects.nonNull(cbtCr)
        && (Objects.nonNull(cbtCr.getFact()) && !cbtCr.getFact().trim().isEmpty()
            || Objects.nonNull(cbtCr.getMind()) && !cbtCr.getMind().trim().isEmpty()
            || Objects.nonNull(cbtCr.getWhyCorrect()) && !cbtCr.getWhyCorrect().trim().isEmpty()
            || Objects.nonNull(cbtCr.getWhyDoubt()) && !cbtCr.getWhyDoubt().trim().isEmpty()
            || Objects.nonNull(cbtCr.getNewThought()) && !cbtCr.getNewThought().trim().isEmpty());
  }
}
