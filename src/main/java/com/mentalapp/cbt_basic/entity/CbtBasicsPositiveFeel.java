package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.PositiveFeel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CbtBasicsとPositiveFeelの中間テーブルを表すエンティティクラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsPositiveFeel {

  private Long cbtBasicId;
  private Long positiveFeelingId;
  private CbtBasics cbtBasics;
  private PositiveFeel positiveFeel;

  /**
   * IDを指定して中間テーブルエンティティを作成するコンストラクタ
   *
   * @param cbtBasicId CBT基礎モニタリングのID
   * @param positiveFeelingId ポジティブ感情のID
   */
  public CbtBasicsPositiveFeel(Long cbtBasicId, Long positiveFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.positiveFeelingId = positiveFeelingId;
  }
}
