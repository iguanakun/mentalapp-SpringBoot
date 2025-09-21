package com.mentalapp.cbt_basic.form;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/** CBT Basicsのフォームデータを扱うクラス */
@Data
public class CbtBasicsInputForm {

  private CbtBasics cbtBasics;
  private String tagNames;
  private List<Long> negativeFeelIds;
  private List<Long> positiveFeelIds;

  /**
   * バリデーション用のメソッド - いずれかの項目が入力されているか
   *
   * @return いずれかの項目が入力されている場合はtrue
   */
  public boolean hasAnyContent() {
    // Check for negativeFeelIds and positiveFeelIds first
    if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
      return true;
    }
    if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
      return true;
    }

    // Check for tagNames
    if (Objects.nonNull(tagNames) && !tagNames.trim().isEmpty()) {
      return true;
    }

    // Then check cbtBasics fields if cbtBasics is not null
    return Objects.nonNull(cbtBasics)
        && (Objects.nonNull(cbtBasics.getFact()) && !cbtBasics.getFact().trim().isEmpty()
            || Objects.nonNull(cbtBasics.getMind()) && !cbtBasics.getMind().trim().isEmpty()
            || Objects.nonNull(cbtBasics.getBody()) && !cbtBasics.getBody().trim().isEmpty()
            || Objects.nonNull(cbtBasics.getBehavior())
                && !cbtBasics.getBehavior().trim().isEmpty());
  }

  /**
   * ユーザーIDを取得
   *
   * @return ユーザーID
   */
  public Long getUserId() {
    return Objects.nonNull(cbtBasics) ? cbtBasics.getUserId() : null;
  }
}
