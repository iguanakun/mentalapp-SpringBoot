package com.mentalapp.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/** ユーザーロールのエンティティクラス */
@Data
@NoArgsConstructor
public class Role {

  private Long id;
  private String name;

  /**
   * 名前を指定してロールを作成するコンストラクタ
   *
   * @param name ロール名
   */
  public Role(String name) {
    this.name = name;
  }
}
