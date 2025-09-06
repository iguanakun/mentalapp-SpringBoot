package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 認知再構成法のエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
@ToString(
    exclude = {
      "cbtCrNegativeFeels",
      "cbtCrPositiveFeels",
      "cbtCrDistortionRelations",
      "negativeFeels",
      "positiveFeels",
      "distortionLists"
    })
public class CbtCr {

  private Long id;
  private String fact;
  private String mind;
  private String whyCorrect;
  private String whyDoubt;
  private String newThought;
  // スネークケースはデータベースのカラム名と一致させるために使用
  private Long user_id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private List<CbtCrNegativeFeel> cbtCrNegativeFeels;
  private List<CbtCrPositiveFeel> cbtCrPositiveFeels;
  private List<CbtCrDistortionRelation> cbtCrDistortionRelations;
  private List<NegativeFeel> negativeFeels;
  private List<PositiveFeel> positiveFeels;
  private List<DistortionList> distortionLists;

  /**
   * ユーザーIDを取得する（命名規則の互換性のため）
   *
   * @return ユーザーID
   */
  public Long getUserId() {
    return user_id;
  }

  /**
   * ユーザーIDを設定する
   *
   * @param userId ユーザーID
   */
  public void setUserId(Long userId) {
    this.user_id = userId;
  }

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
