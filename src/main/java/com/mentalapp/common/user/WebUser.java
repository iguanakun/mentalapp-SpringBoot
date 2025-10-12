package com.mentalapp.common.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebUser {

  @NotNull
  @Size(min = 1)
  private String userName;

  @NotNull
  @Size(min = 1)
  private String password;

  @NotNull
  @Size(min = 1)
  private String firstName;

  @NotNull
  @Size(min = 1)
  private String lastName;

  @NotNull
  @Size(min = 1)
  @Pattern(
      regexp =
          "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
  private String email;
}
