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

  /** 
   * エンティティ作成時に呼び出される
   * 注: このメソッドはMyBatisの実装では直接使用されていませんが、
   * エンティティのライフサイクル管理のために保持されています。
   */
  public void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /** 
   * エンティティ更新時に呼び出される
   * 注: このメソッドはMyBatisの実装では直接使用されていませんが、
   * エンティティのライフサイクル管理のために保持されています。
   */
  public void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
