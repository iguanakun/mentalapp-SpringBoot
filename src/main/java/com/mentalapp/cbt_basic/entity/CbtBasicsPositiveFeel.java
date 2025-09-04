package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.PositiveFeel;
import java.io.Serializable;
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

  /** 複合主キーを表す内部クラス */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CbtBasicsPositiveFeelId implements Serializable {
    private Long cbtBasicId;
    private Long positiveFeelingId;
  }

  public CbtBasicsPositiveFeel(Long cbtBasicId, Long positiveFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.positiveFeelingId = positiveFeelingId;
  }
}
