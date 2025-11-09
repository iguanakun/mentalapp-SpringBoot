package com.mentalapp.common.controller;

import com.mentalapp.common.service.UserServiceImpl;
import com.mentalapp.common.user.WebUser;
import jakarta.validation.Valid;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** ユーザー登録処理を行うコントローラークラス */
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class UserRegistrationController {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final UserServiceImpl userService;

  /**
   * 文字列トリミングエディタを登録
   *
   * @param dataBinder WebDataBinder
   */
  @InitBinder
  public void initBinder(WebDataBinder dataBinder) {

    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

    dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
  }

  /**
   * 登録フォームを表示
   *
   * @param theModel モデル
   * @return 登録フォームのビュー名
   */
  @GetMapping("/showRegistrationForm")
  public String showRegistrationForm(Model theModel) {

    theModel.addAttribute("webUser", new WebUser());

    return "register/registration-form";
  }

  /**
   * 登録フォームの処理
   *
   * @param theWebUser ユーザー情報
   * @param theBindingResult バリデーション結果
   * @param theModel モデル
   * @return ログインページへのリダイレクトまたは登録フォームのビュー名
   */
  @PostMapping("/processRegistrationForm")
  public String processRegistrationForm(
      @Valid @ModelAttribute("webUser") WebUser theWebUser,
      BindingResult theBindingResult,
      Model theModel)
      throws Exception {

    String userName = theWebUser.getUserName();
    logger.info("Processing registration form for: " + userName);

    // フォームバリデーション
    if (theBindingResult.hasErrors()) {
      return "register/registration-form";
    }

    userService.register(theWebUser);

    logger.info("Successfully created user: " + userName);

    // 登録成功後は登録完了ページを表示
    return "register/registration-confirmation";
  }
}
