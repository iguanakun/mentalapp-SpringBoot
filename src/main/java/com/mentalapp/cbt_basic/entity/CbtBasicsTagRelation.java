package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.Tag;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** CBT BasicsとTagの関連を表すエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CbtBasicsTagRelation {

  private Long cbtBasicId;
  private Long tagId;
  private CbtBasics cbtBasics;
  private Tag tag;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
