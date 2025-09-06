package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.PositiveFeel;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CbtBasicsとPositiveFeelの中間テーブルを表すエンティティクラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbtBasicsPositiveFeel {

  private Long cbtBasicId;
  private Long positiveFeelingId;
  private CbtBasics cbtBasics;
  private PositiveFeel positiveFeel;

  /**
   * 複合主キーを表す内部クラス
   * 注: このクラスは現在MyBatisの実装では直接使用されていませんが、
   * JPA互換性のために保持されています。将来的にJPAを使用する可能性がある場合に備えて残しています。
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CbtBasicsPositiveFeelId implements Serializable {
    private Long cbtBasicId;
    private Long positiveFeelingId;
  }

  /**
   * IDを指定して中間テーブルエンティティを作成するコンストラクタ
   *
   * @param cbtBasicId CBT基礎モニタリングのID
   * @param positiveFeelingId ポジティブ感情のID
   */
  public CbtBasicsPositiveFeel(Long cbtBasicId, Long positiveFeelingId) {
    this.cbtBasicId = cbtBasicId;
    this.positiveFeelingId = positiveFeelingId;
  }
}
