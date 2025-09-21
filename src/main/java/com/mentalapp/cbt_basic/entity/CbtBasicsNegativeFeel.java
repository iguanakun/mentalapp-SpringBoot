package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CbtBasicsとNegativeFeelの中間テーブルを表すエンティティクラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsNegativeFeel {

  private Long cbtBasicId;
  private Long negativeFeelingId;
  private CbtBasics cbtBasics;
  private NegativeFeel negativeFeel;

  /**
   * IDを指定して中間テーブルエンティティを作成するコンストラクタ
   *
   * @param cbtBasicId CBT基礎モニタリングのID
   * @param negativeFeelingId ネガティブ感情のID
   */
  public CbtBasicsNegativeFeel(Long cbtBasicId, Long negativeFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.negativeFeelingId = negativeFeelingId;
  }
}
