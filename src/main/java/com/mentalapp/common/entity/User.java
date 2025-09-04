package com.mentalapp.common.entity;

import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

  public User(String userName, String password, boolean enabled) {
    this.userName = userName;
    this.password = password;
    this.enabled = enabled;
  }

  public User(String userName, String password, boolean enabled, Collection<Role> roles) {
    this.userName = userName;
    this.password = password;
    this.enabled = enabled;
    this.roles = roles;
  }
}
