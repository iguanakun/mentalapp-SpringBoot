package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.PositiveFeel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 認知再構成法とポジティブ感情の中間テーブルのエンティティクラス
 */
@Entity
@Table(name = "cbt_cr_positive_feels")
@Getter
@Setter
@NoArgsConstructor
public class CbtCrPositiveFeel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cbt_cr_id", insertable = false, updatable = false)
    private Long cbtCrId;

    @Column(name = "positive_feel_id", insertable = false, updatable = false)
    private Long positiveFeelId;

    @ManyToOne
    @JoinColumn(name = "cbt_cr_id")
    private CbtCr cbtCr;

    @ManyToOne
    @JoinColumn(name = "positive_feel_id")
    private PositiveFeel positiveFeel;
}