package com.mentalapp.cbt_basic.data;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.cbt_basic.entity.CbtBasics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CBT基本情報とそれに関連するネガティブ/ポジティブ感情のリストを保持するデータオブジェクト
 */
public class CbtBasicsObject {
    
    private CbtBasics cbtBasics;
    private List<NegativeFeel> negativeFeels;
    private List<PositiveFeel> positiveFeels;
    
    // コンストラクタ
    public CbtBasicsObject() {
        this.cbtBasics = new CbtBasics();
        this.negativeFeels = new ArrayList<>();
        this.positiveFeels = new ArrayList<>();
    }
    
    // ゲッターとセッター
    public Long getId() {
        return cbtBasics.getId();
    }
    
    public CbtBasics getCbtBasics() {
        return cbtBasics;
    }
    
    public void setCbtBasics(CbtBasics cbtBasics) {
        this.cbtBasics = cbtBasics;
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
}
