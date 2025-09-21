package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.PositiveFeel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 認知再構成法とポジティブ感情の中間テーブルのエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
public class CbtCrPositiveFeel {

  private Long id;
  private Long cbtCrId;
  private Integer positiveFeelId;
  private CbtCr cbtCr;
  private PositiveFeel positiveFeel;
}
