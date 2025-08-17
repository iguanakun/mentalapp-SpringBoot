package com.mentalapp.cbt_basic.data;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CBT基本情報とそれに関連するネガティブ/ポジティブ感情のリストを保持するデータオブジェクト
 */
public class CbtBasicsObject {
    
    private Long id;
    private String fact;
    private String mind;
    private String body;
    private String behavior;
    private Long userId;
    private LocalDateTime createdAt;
    private List<NegativeFeel> negativeFeels;
    private List<PositiveFeel> positiveFeels;
    
    // コンストラクタ
    public CbtBasicsObject() {
    }
    
    // ゲッターとセッター
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFact() {
        return fact;
    }
    
    public void setFact(String fact) {
        this.fact = fact;
    }
    
    public String getMind() {
        return mind;
    }
    
    public void setMind(String mind) {
        this.mind = mind;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getBehavior() {
        return behavior;
    }
    
    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<NegativeFeel> getNegativeFeels() {
        return negativeFeels;
    }
    
    public void setNegativeFeels(List<NegativeFeel> negativeFeels) {
        this.negativeFeels = negativeFeels;
    }
    
    public List<PositiveFeel> getPositiveFeels() {
        return positiveFeels;
    }
    
    public void setPositiveFeels(List<PositiveFeel> positiveFeels) {
        this.positiveFeels = positiveFeels;
    }
    
    @Override
    public String toString() {
        return "CbtBasicsObject{" +
                "id=" + id +
                ", fact='" + fact + '\'' +
                ", mind='" + mind + '\'' +
                ", body='" + body + '\'' +
                ", behavior='" + behavior + '\'' +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", negativeFeels=" + negativeFeels +
                ", positiveFeels=" + positiveFeels +
                '}';
    }
}
