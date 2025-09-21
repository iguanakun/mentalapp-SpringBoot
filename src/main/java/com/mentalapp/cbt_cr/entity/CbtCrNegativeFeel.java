package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.NegativeFeel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 認知再構成法とネガティブ感情の中間テーブルのエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
public class CbtCrNegativeFeel {

  private Long id;
  private Long cbtCrId;
  private Integer negativeFeelId;
  private CbtCr cbtCr;
  private NegativeFeel negativeFeel;
}
