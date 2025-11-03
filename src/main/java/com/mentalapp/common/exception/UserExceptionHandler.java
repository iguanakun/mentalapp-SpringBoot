package com.mentalapp.common.exception;

import com.mentalapp.common.user.WebUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class UserExceptionHandler {

  /** ユーザー登録重複エラー時、エラーメッセージをセットし登録フォームへ遷移 */
  @ExceptionHandler(UserAlreadyRegisteredException.class)
  public ModelAndView handleException(Exception e) {
    ModelAndView mav = new ModelAndView();
    mav.addObject("webUser", new WebUser());
    mav.addObject("registrationError", "このユーザー名は既に使用されています。");

    mav.setViewName("register/registration-form");
    return mav;
  }
}
