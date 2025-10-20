package com.mentalapp.common.util;

import com.mentalapp.common.dao.UserMapper;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@RequiredArgsConstructor
public class MentalCommonUtils {

  public static final String REDIRECT_TOP_PAGE = "redirect:/";
  public static final String REDIRECT_MEMOS_PAGE = "redirect:/memos";

  private final UserMapper userMapper;

  @Autowired MessageSource messages;

  /**
   * ログイン中のユーザー情報を取得
   *
   * @return ログイン中のユーザーエンティティ
   */
  public User getUser() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    return userMapper.selectByPrimaryKey(userName);
  }

  /**
   * アクセス権チェック
   *
   * @param userId エンティティのユーザーID
   * @return アクセス権がある場合はtrue
   */
  public boolean isAuthorized(Long userId) {
    return userId.equals(getUser().getId());
  }

  /**
   * ネガティブ感情IDリストを抽出
   *
   * @param negativeFeelList 抽出元のCBT Basicsエンティティ
   * @return ネガティブ感情IDのリスト、関連付けがない場合はnull
   */
  public List<Long> extractedNegativeFeelsIdList(List<NegativeFeel> negativeFeelList) {
    if (Objects.isNull(negativeFeelList) || negativeFeelList.isEmpty()) {
      return null;
    }

    return negativeFeelList.stream().map(NegativeFeel::getId).toList();
  }

  /**
   * ネガティブ感情IDリストを抽出
   *
   * @param positiveFeelList 抽出元のCBT Basicsエンティティ
   * @return ネガティブ感情IDのリスト、関連付けがない場合はnull
   */
  public List<Long> extractedPositiveFeelsIdList(List<PositiveFeel> positiveFeelList) {
    if (Objects.isNull(positiveFeelList) || positiveFeelList.isEmpty()) {
      return null;
    }

    return positiveFeelList.stream().map(PositiveFeel::getId).toList();
  }

  /**
   * バリデーションエラーメッセージをモデルに追加
   *
   * @param model モデル
   */
  public void addValidationErrorMessage(Model model) {
    String errMsg = messages.getMessage("error.atleastone.required", null, Locale.JAPAN);
    model.addAttribute("errMsg", errMsg);
  }
}
