package com.mentalapp.cbt_basic.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * CBT Basicsのフォームデータを扱うクラス
 */
@Data
public class CbtBasicsForm {
    
    private Long id;
    private String fact;
    private String mind;
    private String body;
    private String behavior;
    private Long userId;
    private List<Long> negativeFeelIds;
    private List<Long> positiveFeelIds;
    private String tagNames;
    
    /**
     * バリデーション用のメソッド - いずれかの項目が入力されているか
     * @return いずれかの項目が入力されている場合はtrue
     */
    public boolean hasAnyContent() {
        return Objects.nonNull(fact) && !fact.trim().isEmpty() ||
               Objects.nonNull(mind) && !mind.trim().isEmpty() ||
               Objects.nonNull(body) && !body.trim().isEmpty() ||
               Objects.nonNull(behavior) && !behavior.trim().isEmpty() ||
               Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty() ||
               Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty();
    }
}