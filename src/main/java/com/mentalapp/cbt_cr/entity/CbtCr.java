package com.mentalapp.cbt_cr.entity;

import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 認知再構成法のエンティティクラス
 */
@Entity
@Table(name = "cbt_cr")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"cbtCrNegativeFeels", "cbtCrPositiveFeels", "cbtCrDistortionRelations", "negativeFeels", "positiveFeels", "distortionLists"})
public class CbtCr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fact")
    private String fact;

    @Column(name = "mind")
    private String mind;

    @Column(name = "why_correct")
    private String whyCorrect;

    @Column(name = "why_doubt")
    private String whyDoubt;

    @Column(name = "new_thought")
    private String newThought;

    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "cbtCr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CbtCrNegativeFeel> cbtCrNegativeFeels;
    
    @OneToMany(mappedBy = "cbtCr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CbtCrPositiveFeel> cbtCrPositiveFeels;
    
    @OneToMany(mappedBy = "cbtCr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CbtCrDistortionRelation> cbtCrDistortionRelations;
    
    @ManyToMany
    @JoinTable(
        name = "cbt_cr_negative_feels",
        joinColumns = @JoinColumn(name = "cbt_cr_id"),
        inverseJoinColumns = @JoinColumn(name = "negative_feel_id")
    )
    private List<NegativeFeel> negativeFeels;
    
    @ManyToMany
    @JoinTable(
        name = "cbt_cr_positive_feels",
        joinColumns = @JoinColumn(name = "cbt_cr_id"),
        inverseJoinColumns = @JoinColumn(name = "positive_feel_id")
    )
    private List<PositiveFeel> positiveFeels;
    
    @ManyToMany
    @JoinTable(
        name = "cbt_cr_distortion_relations",
        joinColumns = @JoinColumn(name = "cbt_cr_id"),
        inverseJoinColumns = @JoinColumn(name = "distortion_list_id")
    )
    private List<DistortionList> distortionLists;
    
    /**
     * ユーザーIDを取得する（命名規則の互換性のため）
     * @return ユーザーID
     */
    public Long getUserId() {
        return user_id;
    }

    /**
     * ユーザーIDを設定する
     * @param userId ユーザーID
     */
    public void setUserId(Long userId) {
        this.user_id = userId;
    }
    
    /**
     * エンティティ作成時に自動的に呼び出される
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * エンティティ更新時に自動的に呼び出される
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}