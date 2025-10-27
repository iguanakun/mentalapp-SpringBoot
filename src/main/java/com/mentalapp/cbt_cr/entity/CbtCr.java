package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.Distortion;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 認知再構成法のエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
public class CbtCr {

  private Long id;
  private String fact;
  private String mind;
  private String whyCorrect;
  private String whyDoubt;
  private String newThought;
  private Long userId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private List<CbtCrNegativeFeel> cbtCrNegativeFeels;
  private List<CbtCrPositiveFeel> cbtCrPositiveFeels;
  private List<CbtCrDistortionRelation> cbtCrDistortionRelations;
  private List<CbtCrTagRelation> cbtCrTagRelations;
  private List<NegativeFeel> negativeFeels;
  private List<PositiveFeel> positiveFeels;
  private List<Distortion> distortions;
  private List<Tag> tags;
}
