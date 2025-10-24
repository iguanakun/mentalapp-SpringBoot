package com.mentalapp.cbt_cr.form;

import java.util.List;
import java.util.Objects;
import lombok.Data;

/** 認知再構成法のフォームデータを扱うクラス */
@Data
public class CbtCrInputForm {

  /** ID */
  private Long id;

  /** 状況 */
  private String fact;

  /** 思考 */
  private String mind;

  /** 思考が正しいと思う事実・根拠 */
  private String whyCorrect;

  /** 思考が間違っていると思う事実・根拠 */
  private String whyDoubt;

  /** 新しい考え方 */
  private String newThought;

  /** ネガティブ感情のID */
  private List<Long> negativeFeelIds;

  /** ポジティブ感情のID */
  private List<Long> positiveFeelIds;

  /** 思考の歪みのID */
  private List<Long> distortionIds;

  /** ユーザーID */
  private Long userId;

  /** タグ名（スペース区切り） */
  private String tagNames;

  /**
   * バリデーション用のメソッド - いずれかの項目が入力されているか
   *
   * @return いずれかの項目が入力されている場合はtrue
   */
  public boolean hasAnyContent() {
    // タグ名のチェック
    if (Objects.nonNull(tagNames) && !tagNames.trim().isEmpty()) {
      return true;
    }

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

    // その他チェック
    return (Objects.nonNull(fact) && !fact.trim().isEmpty())
        || (Objects.nonNull(mind) && !mind.trim().isEmpty())
        || (Objects.nonNull(whyCorrect) && !whyCorrect.trim().isEmpty())
        || (Objects.nonNull(whyDoubt) && !whyDoubt.trim().isEmpty())
        || (Objects.nonNull(newThought) && !newThought.trim().isEmpty());
  }
}
