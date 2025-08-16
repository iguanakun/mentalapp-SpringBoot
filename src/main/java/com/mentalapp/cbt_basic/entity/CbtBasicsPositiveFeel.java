package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.PositiveFeel;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * CbtBasicsとPositiveFeelの中間テーブルを表すエンティティクラス
 */
@Entity
@Table(name = "cbt_basics_positive_feels")
@IdClass(CbtBasicsPositiveFeel.CbtBasicsPositiveFeelId.class)
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

    /**
     * 複合主キーを表す内部クラス
     */
    public static class CbtBasicsPositiveFeelId implements Serializable {
        private Long cbtBasicId;
        private Long positiveFeelingId;

        public CbtBasicsPositiveFeelId() {
        }

        public CbtBasicsPositiveFeelId(Long cbtBasicId, Long positiveFeelingId) {
            this.cbtBasicId = cbtBasicId;
            this.positiveFeelingId = positiveFeelingId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CbtBasicsPositiveFeelId that = (CbtBasicsPositiveFeelId) o;

            if (!cbtBasicId.equals(that.cbtBasicId)) return false;
            return positiveFeelingId.equals(that.positiveFeelingId);
        }

        @Override
        public int hashCode() {
            int result = cbtBasicId.hashCode();
            result = 31 * result + positiveFeelingId.hashCode();
            return result;
        }
    }

    // コンストラクタ
    public CbtBasicsPositiveFeel() {
    }

    public CbtBasicsPositiveFeel(Long cbtBasicId, Long positiveFeelingId) {
        this.cbtBasicId = cbtBasicId;
        this.positiveFeelingId = positiveFeelingId;
    }

    // ゲッターとセッター
    public Long getCbtBasicId() {
        return cbtBasicId;
    }

    public void setCbtBasicId(Long cbtBasicId) {
        this.cbtBasicId = cbtBasicId;
    }

    public Long getPositiveFeelingId() {
        return positiveFeelingId;
    }

    public void setPositiveFeelingId(Long positiveFeelingId) {
        this.positiveFeelingId = positiveFeelingId;
    }

    public CbtBasics getCbtBasics() {
        return cbtBasics;
    }

    public void setCbtBasics(CbtBasics cbtBasics) {
        this.cbtBasics = cbtBasics;
    }

    public PositiveFeel getPositiveFeel() {
        return positiveFeel;
    }

    public void setPositiveFeel(PositiveFeel positiveFeel) {
        this.positiveFeel = positiveFeel;
    }
}