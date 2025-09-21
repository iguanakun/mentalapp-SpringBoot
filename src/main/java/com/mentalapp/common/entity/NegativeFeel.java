package com.mentalapp.common.entity;

import java.time.LocalDateTime;
import lombok.Data;

/** ネガティブ感情のエンティティクラス */
@Data
public class NegativeFeel {

  private Long id;
  private String negativeFeelName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
