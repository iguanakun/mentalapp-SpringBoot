package com.mentalapp.common.entity;

import java.time.LocalDateTime;
import lombok.Data;

/** タグのエンティティクラス */
@Data
public class Tag {

  private Long id;
  private String tagName;
  private Long userId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
