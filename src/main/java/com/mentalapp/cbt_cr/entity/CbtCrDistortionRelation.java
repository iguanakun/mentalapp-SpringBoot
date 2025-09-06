package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.DistortionList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 認知再構成法と思考の歪みの中間テーブルのエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
public class CbtCrDistortionRelation {

  private Long id;
  private Long cbtCrId;
  private Long distortionListId;
  private CbtCr cbtCr;
  private DistortionList distortionList;
}
