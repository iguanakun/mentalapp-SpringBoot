package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.DistortionList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 認知再構成法と思考の歪みの中間テーブルのエンティティクラス
 */
@Entity
@Table(name = "cbt_cr_distortion_relations")
@Getter
@Setter
@NoArgsConstructor
public class CbtCrDistortionRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cbt_cr_id", insertable = false, updatable = false)
    private Long cbtCrId;

    @Column(name = "distortion_list_id", insertable = false, updatable = false)
    private Long distortionListId;

    @ManyToOne
    @JoinColumn(name = "cbt_cr_id")
    private CbtCr cbtCr;

    @ManyToOne
    @JoinColumn(name = "distortion_list_id")
    private DistortionList distortionList;
}