package com.mentalapp.cbt_basic.form;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * CBT Basicsのフォームデータを扱うクラス
 */
public class CbtBasicsForm {
    
    private Long id;
    
    private String fact;
    
    private String mind;
    
    private String body;
    
    private String behavior;
    
    @NotNull(message = "ユーザーIDは必須です")
    private Long userId;
    
    private List<Long> negativeFeelIds;
    
    private List<Long> positiveFeelIds;
    
    private String tagNames;
    
    /**
     * バリデーション用のメソッド - いずれかの項目が入力されているか
     * @return いずれかの項目が入力されている場合はtrue
     */
    public boolean hasAnyContent() {
        return fact != null && !fact.trim().isEmpty() ||
               mind != null && !mind.trim().isEmpty() ||
               body != null && !body.trim().isEmpty() ||
               behavior != null && !behavior.trim().isEmpty() ||
               negativeFeelIds != null && !negativeFeelIds.isEmpty() ||
               positiveFeelIds != null && !positiveFeelIds.isEmpty();
    }

    // コンストラクタ
    public CbtBasicsForm() {
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

    public List<Long> getNegativeFeelIds() {
        return negativeFeelIds;
    }

    public void setNegativeFeelIds(List<Long> negativeFeelIds) {
        this.negativeFeelIds = negativeFeelIds;
    }

    public List<Long> getPositiveFeelIds() {
        return positiveFeelIds;
    }

    public void setPositiveFeelIds(List<Long> positiveFeelIds) {
        this.positiveFeelIds = positiveFeelIds;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }
}