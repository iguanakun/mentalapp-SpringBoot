package com.mentalapp.common.entity;

import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** ユーザーのエンティティクラス */
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "roles")
public class User {

  private Long id;
  private String userName;
  private String password;
  private boolean enabled;
  private String firstName;
  private String lastName;
  private String email;
  private Collection<Role> roles;

  /**
   * 基本情報を指定してユーザーを作成するコンストラクタ
   *
   * @param userName ユーザー名
   * @param password パスワード
   * @param enabled 有効フラグ
   */
  public User(String userName, String password, boolean enabled) {
    this.userName = userName;
    this.password = password;
    this.enabled = enabled;
  }

  /**
   * 基本情報とロールを指定してユーザーを作成するコンストラクタ
   *
   * @param userName ユーザー名
   * @param password パスワード
   * @param enabled 有効フラグ
   * @param roles ユーザーロールのコレクション
   */
  public User(String userName, String password, boolean enabled, Collection<Role> roles) {
    this.userName = userName;
    this.password = password;
    this.enabled = enabled;
    this.roles = roles;
  }
}
