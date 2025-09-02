package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.NegativeFeel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 認知再構成法とネガティブ感情の中間テーブルのエンティティクラス */
@Entity
@Table(name = "cbt_cr_negative_feels")
@Getter
@Setter
@NoArgsConstructor
public class CbtCrNegativeFeel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "cbt_cr_id", insertable = false, updatable = false)
  private Long cbtCrId;

  @Column(name = "negative_feel_id", insertable = false, updatable = false)
  private Integer negativeFeelId;

  @ManyToOne
  @JoinColumn(name = "cbt_cr_id")
  private CbtCr cbtCr;

  @ManyToOne
  @JoinColumn(name = "negative_feel_id")
  private NegativeFeel negativeFeel;
}
