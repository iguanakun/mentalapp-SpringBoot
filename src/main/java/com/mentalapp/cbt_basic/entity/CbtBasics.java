package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** CBT基礎モニタリングのエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
@ToString(
    exclude = {
      "cbtBasicsNegativeFeels",
      "cbtBasicsPositiveFeels",
      "cbtBasicsTagRelations",
      "negativeFeels",
      "positiveFeels",
      "tags"
    })
public class CbtBasics {

  private Long id;
  private String fact;
  private String mind;
  private String body;
  private String behavior;
  private Long user_id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<CbtBasicsNegativeFeel> cbtBasicsNegativeFeels;
  private List<CbtBasicsPositiveFeel> cbtBasicsPositiveFeels;
  private List<CbtBasicsTagRelation> cbtBasicsTagRelations;
  private List<NegativeFeel> negativeFeels;
  private List<PositiveFeel> positiveFeels;
  private List<Tag> tags;

  // getUserId()メソッドは命名規則の互換性を維持するために保持
  public Long getUserId() {
    return user_id;
  }

  public void setUserId(Long userId) {
    this.user_id = userId;
  }
}
