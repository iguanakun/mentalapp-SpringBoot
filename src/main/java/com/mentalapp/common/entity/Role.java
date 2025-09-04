package com.mentalapp.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {

  private Long id;
  private String name;

  public Role(String name) {
    this.name = name;
  }
}
