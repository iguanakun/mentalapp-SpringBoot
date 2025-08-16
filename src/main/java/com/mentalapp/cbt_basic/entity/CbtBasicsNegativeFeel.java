package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * CbtBasicsとNegativeFeelの中間テーブルを表すエンティティクラス
 */
@Entity
@Table(name = "cbt_basics_negative_feels")
@IdClass(CbtBasicsNegativeFeel.CbtBasicsNegativeFeelId.class)
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

    /**
     * 複合主キーを表す内部クラス
     */
    public static class CbtBasicsNegativeFeelId implements Serializable {
        private Long cbtBasicId;
        private Long negativeFeelingId;

        public CbtBasicsNegativeFeelId() {
        }

        public CbtBasicsNegativeFeelId(Long cbtBasicId, Long negativeFeelingId) {
            this.cbtBasicId = cbtBasicId;
            this.negativeFeelingId = negativeFeelingId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CbtBasicsNegativeFeelId that = (CbtBasicsNegativeFeelId) o;

            if (!cbtBasicId.equals(that.cbtBasicId)) return false;
            return negativeFeelingId.equals(that.negativeFeelingId);
        }

        @Override
        public int hashCode() {
            int result = cbtBasicId.hashCode();
            result = 31 * result + negativeFeelingId.hashCode();
            return result;
        }
    }

    // コンストラクタ
    public CbtBasicsNegativeFeel() {
    }

    public CbtBasicsNegativeFeel(Long cbtBasicId, Long negativeFeelingId) {
        this.cbtBasicId = cbtBasicId;
        this.negativeFeelingId = negativeFeelingId;
    }

    // ゲッターとセッター
    public Long getCbtBasicId() {
        return cbtBasicId;
    }

    public void setCbtBasicId(Long cbtBasicId) {
        this.cbtBasicId = cbtBasicId;
    }

    public Long getNegativeFeelingId() {
        return negativeFeelingId;
    }

    public void setNegativeFeelingId(Long negativeFeelingId) {
        this.negativeFeelingId = negativeFeelingId;
    }

    public CbtBasics getCbtBasics() {
        return cbtBasics;
    }

    public void setCbtBasics(CbtBasics cbtBasics) {
        this.cbtBasics = cbtBasics;
    }

    public NegativeFeel getNegativeFeel() {
        return negativeFeel;
    }

    public void setNegativeFeel(NegativeFeel negativeFeel) {
        this.negativeFeel = negativeFeel;
    }
}