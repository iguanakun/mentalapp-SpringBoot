package com.mentalapp.common;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.Tag;
import com.mentalapp.common.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/** テストユーティリティクラス テストで共通して使用される操作のためのユーティリティメソッドを提供します */
public class TestUtils {

  /**
   * セキュリティコンテキストにユーザー認証情報をセットアップします
   *
   * @param user 認証するユーザー
   */
  public static void setupSecurityContext(User user) {
    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  /** セキュリティコンテキストをクリアします テスト後のクリーンアップに使用します */
  public static void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  public static CbtBasics createCbtBasics() {
    CbtBasics cbtBasics = new CbtBasics();
    cbtBasics.setId(1L);
    cbtBasics.setFact("テスト状況");
    cbtBasics.setMind("テスト思考");
    cbtBasics.setBody("テスト身体反応");
    cbtBasics.setBehavior("テスト行動");
    cbtBasics.setNegativeFeels(createNegativeFeels());
    cbtBasics.setPositiveFeels(createPositiveFeels());
    cbtBasics.setTags(createTags());
    cbtBasics.setCreatedAt(getLocalDateTime());
    cbtBasics.setUpdatedAt(getLocalDateTime());
    cbtBasics.setUserId(1L);

    return cbtBasics;
  }

  public static List<NegativeFeel> createNegativeFeels() {
    List<NegativeFeel> negativeFeelList = new ArrayList<>();

    for (long id : createNegativeFeelIds()) {
      NegativeFeel negativeFeel = new NegativeFeel();
      negativeFeel.setId(id);
      negativeFeel.setNegativeFeelName("ネガティブ感情" + id);
      negativeFeel.setCreatedAt(getLocalDateTime());
      negativeFeel.setUpdatedAt(getLocalDateTime());
      negativeFeelList.add(negativeFeel);
    }

    return negativeFeelList;
  }

  private static LocalDateTime getLocalDateTime() {
    return LocalDateTime.now();
  }

  public static List<PositiveFeel> createPositiveFeels() {
    List<PositiveFeel> positiveFeelList = new ArrayList<>();

    for (long id : createPositiveFeelIds()) {
      PositiveFeel positiveFeel = new PositiveFeel();
      positiveFeel.setId(id);
      positiveFeel.setPositiveFeelName("ポジティブ感情" + id);
      positiveFeel.setCreatedAt(getLocalDateTime());
      positiveFeel.setUpdatedAt(getLocalDateTime());
      positiveFeelList.add(positiveFeel);
    }

    return positiveFeelList;
  }

  public static List<Tag> createTags() {
    List<Tag> tagList = new ArrayList<>();

    for (long i = 1; i <= 3; i++) {
      Tag tag = new Tag();
      tag.setId(i);
      tag.setTagName("タグ" + i);
      tag.setUserId(1L); // 必要に応じてユーザーIDを変える
      tag.setCreatedAt(getLocalDateTime());
      tag.setUpdatedAt(getLocalDateTime());
      tagList.add(tag);
    }

    return tagList;
  }

  public static CbtBasicsInputForm createCbtBasicsInputForm() {
    CbtBasicsInputForm form = new CbtBasicsInputForm();
    CbtBasics cbtBasics = createCbtBasicsWithForm();

    form.setCbtBasics(cbtBasics);
    form.setTagNames(createTagNames());
    form.setPositiveFeelIds(createPositiveFeelIds());
    form.setNegativeFeelIds(createNegativeFeelIds());

    return form;
  }

  public static String createTagNames() {
    return "タグ1 タグ2 タグ3";
  }

  public static List<Long> createPositiveFeelIds() {
    List<Long> positiveFeelIds = new ArrayList<>();
    positiveFeelIds.add(1L);
    positiveFeelIds.add(2L);
    positiveFeelIds.add(3L);

    return positiveFeelIds;
  }

  public static List<Long> createNegativeFeelIds() {
    List<Long> negativeFeelIds = new ArrayList<>();
    negativeFeelIds.add(1L);
    negativeFeelIds.add(2L);
    negativeFeelIds.add(3L);

    return negativeFeelIds;
  }

  private static CbtBasics createCbtBasicsWithForm() {
    CbtBasics cbtBasics = createCbtBasics();

    // 画面の入力値に合わせてnullをセット
    cbtBasics.setNegativeFeels(null);
    cbtBasics.setPositiveFeels(null);
    cbtBasics.setTags(null);
    cbtBasics.setUserId(null);

    return cbtBasics;
  }

  public static User createUser() {
    User user = new User();
    user.setId(1L);
    user.setUserName("testuser");

    return user;
  }

  public static CbtCr createCbtCr() {
    CbtCr cbtCr = new CbtCr();
    cbtCr.setId(1L);
    cbtCr.setFact("テスト状況");
    cbtCr.setMind("テスト思考");
    cbtCr.setWhyCorrect("テスト正しい証拠");
    cbtCr.setWhyDoubt("テスト間違いの証拠");
    cbtCr.setNewThought("テスト新しい考え方");
    cbtCr.setNegativeFeels(createNegativeFeels());
    cbtCr.setPositiveFeels(createPositiveFeels());
    cbtCr.setDistortionLists(createDistortions());
    cbtCr.setTags(createTags());
    cbtCr.setCreatedAt(getLocalDateTime());
    cbtCr.setUpdatedAt(getLocalDateTime());
    cbtCr.setUserId(1L);

    return cbtCr;
  }

  public static List<DistortionList> createDistortions() {
    List<DistortionList> distortionList = new ArrayList<>();

    for (Long id : createDistortionIds()) {
      DistortionList distortion = new DistortionList();
      distortion.setId(id);
      distortion.setDistortionName("思考の歪み" + id);
      distortion.setCreatedAt(getLocalDateTime());
      distortion.setUpdatedAt(getLocalDateTime());
      distortionList.add(distortion);
    }

    return distortionList;
  }

  public static List<Long> createDistortionIds() {
    List<Long> distortionIds = new ArrayList<>();
    distortionIds.add(1L);
    distortionIds.add(2L);
    distortionIds.add(3L);

    return distortionIds;
  }

  public static CbtCrInputForm createCbtCrInputForm() {
    CbtCrInputForm form = new CbtCrInputForm();
    CbtCr cbtCr = createCbtCrWithForm();

    form.setId(cbtCr.getId());
    form.setFact(cbtCr.getFact());
    form.setMind(cbtCr.getMind());
    form.setWhyCorrect(cbtCr.getWhyCorrect());
    form.setWhyDoubt(cbtCr.getWhyDoubt());
    form.setNewThought(cbtCr.getNewThought());
    form.setTagNames(createTagNames());
    form.setPositiveFeelIds(createPositiveFeelIds());
    form.setNegativeFeelIds(createNegativeFeelIds());
    form.setDistortionIds(createDistortionIds());

    return form;
  }

  private static CbtCr createCbtCrWithForm() {
    CbtCr cbtCr = createCbtCr();

    // 画面の入力値に合わせてnullをセット
    cbtCr.setNegativeFeels(null);
    cbtCr.setPositiveFeels(null);
    cbtCr.setDistortionLists(null);
    cbtCr.setTags(null);
    cbtCr.setUserId(null);

    return cbtCr;
  }
}
