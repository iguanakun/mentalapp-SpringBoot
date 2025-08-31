package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CbtBasicsとNegativeFeelの中間テーブルを表すエンティティクラス */
@Entity
@Table(name = "cbt_basics_negative_feels")
@IdClass(CbtBasicsNegativeFeel.CbtBasicsNegativeFeelId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsNegativeFeel {

  @Id
  @Column(name = "cbt_basic_id")
  private Long cbtBasicId;

  @Id
  @Column(name = "negative_feel_id")
  private Long negativeFeelingId;

  @ManyToOne
  @JoinColumn(name = "cbt_basic_id", insertable = false, updatable = false)
  private CbtBasics cbtBasics;

  @ManyToOne
  @JoinColumn(name = "negative_feel_id", insertable = false, updatable = false)
  private NegativeFeel negativeFeel;

  /** 複合主キーを表す内部クラス */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CbtBasicsNegativeFeelId implements Serializable {
    private Long cbtBasicId;
    private Long negativeFeelingId;
  }

  public CbtBasicsNegativeFeel(Long cbtBasicId, Long negativeFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.negativeFeelingId = negativeFeelingId;
  }
}
