package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.PositiveFeel;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CbtBasicsとPositiveFeelの中間テーブルを表すエンティティクラス */
@Entity
@Table(name = "cbt_basics_positive_feels")
@IdClass(CbtBasicsPositiveFeel.CbtBasicsPositiveFeelId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsPositiveFeel {

  @Id
  @Column(name = "cbt_basic_id")
  private Long cbtBasicId;

  @Id
  @Column(name = "positive_feel_id")
  private Long positiveFeelingId;

  @ManyToOne
  @JoinColumn(name = "cbt_basic_id", insertable = false, updatable = false)
  private CbtBasics cbtBasics;

  @ManyToOne
  @JoinColumn(name = "positive_feel_id", insertable = false, updatable = false)
  private PositiveFeel positiveFeel;

  /** 複合主キーを表す内部クラス */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CbtBasicsPositiveFeelId implements Serializable {
    private Long cbtBasicId;
    private Long positiveFeelingId;
  }

  public CbtBasicsPositiveFeel(Long cbtBasicId, Long positiveFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.positiveFeelingId = positiveFeelingId;
  }
}
