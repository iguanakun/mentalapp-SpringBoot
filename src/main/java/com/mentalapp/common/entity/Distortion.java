package com.mentalapp.common.entity;

import java.time.LocalDateTime;
import lombok.Data;

/** 思考の歪みリストのエンティティクラス */
@Data
public class Distortion {

  private Long id;
  private String distortionName;
  private String info;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
