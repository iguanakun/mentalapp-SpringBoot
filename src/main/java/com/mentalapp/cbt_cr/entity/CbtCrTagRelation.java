package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.Tag;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 認知再構成法とタグの関連を表すエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CbtCrTagRelation {

  private Long cbtCrId;
  private Long tagId;
  private CbtCr cbtCr;
  private Tag tag;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
