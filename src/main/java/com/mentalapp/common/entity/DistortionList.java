package com.mentalapp.common.entity;

import java.time.LocalDateTime;
import lombok.Data;

/** 思考の歪みリストのエンティティクラス */
@Data
public class DistortionList {

  private Long id;
  private String distortionName;
  private String info;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  /** エンティティ作成時に呼び出される */
  public void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /** エンティティ更新時に呼び出される */
  public void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
