package com.mentalapp.common.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PositiveFeel {

  private Long id;
  private String positiveFeelName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
