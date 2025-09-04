package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import java.io.Serializable;
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

  /** 複合主キーを表す内部クラス */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CbtBasicsNegativeFeelId implements Serializable {
    private Long cbtBasicId;
    private Long negativeFeelingId;
  }

  public CbtBasicsNegativeFeel(Long cbtBasicId, Long negativeFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.negativeFeelingId = negativeFeelingId;
  }
}
