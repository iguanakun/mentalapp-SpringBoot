package com.mentalapp.common.controller;

import com.mentalapp.common.entity.User;
import com.mentalapp.common.service.UserServiceImpl;
import com.mentalapp.common.user.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
   * @param session HTTPセッション
   * @param theModel モデル
   * @return ログインページへのリダイレクトまたは登録フォームのビュー名
   */
  @PostMapping("/processRegistrationForm")
  public String processRegistrationForm(
      @Valid @ModelAttribute("webUser") WebUser theWebUser,
      BindingResult theBindingResult,
      HttpSession session,
      Model theModel) {

    String userName = theWebUser.getUserName();
    logger.info("Processing registration form for: " + userName);

    // フォームバリデーション
    if (theBindingResult.hasErrors()) {
      return "register/registration-form";
    }

    // データベースでユーザーが既に存在するかチェック
    User existing = userService.findByUserName(userName);
    if (Objects.nonNull(existing)) {
      theModel.addAttribute("webUser", new WebUser());
      theModel.addAttribute("registrationError", "このユーザー名は既に使用されています。");

      logger.warning("User name already exists.");
      return "register/registration-form";
    }

    // ユーザーアカウントを作成してデータベースに保存
    userService.save(theWebUser);

    logger.info("Successfully created user: " + userName);

    // 登録成功後は登録完了ページを表示
    return "register/registration-confirmation";
  }
}
