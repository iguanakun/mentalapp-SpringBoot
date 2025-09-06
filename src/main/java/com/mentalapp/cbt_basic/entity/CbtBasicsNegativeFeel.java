package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CbtBasicsとNegativeFeelの中間テーブルを表すエンティティクラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsNegativeFeel {

  private Long cbtBasicId;
  private Long negativeFeelingId;
  private CbtBasics cbtBasics;
  private NegativeFeel negativeFeel;

  /**
   * 複合主キーを表す内部クラス
   * 注: このクラスは現在MyBatisの実装では直接使用されていませんが、
   * JPA互換性のために保持されています。将来的にJPAを使用する可能性がある場合に備えて残しています。
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CbtBasicsNegativeFeelId implements Serializable {
    private Long cbtBasicId;
    private Long negativeFeelingId;
  }

  /**
   * IDを指定して中間テーブルエンティティを作成するコンストラクタ
   *
   * @param cbtBasicId CBT基礎モニタリングのID
   * @param negativeFeelingId ネガティブ感情のID
   */
  public CbtBasicsNegativeFeel(Long cbtBasicId, Long negativeFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.negativeFeelingId = negativeFeelingId;
  }
}
