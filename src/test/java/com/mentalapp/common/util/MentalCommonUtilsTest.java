package com.mentalapp.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.common.TestUtils;
import com.mentalapp.common.dao.UserMapper;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/** 共通ユーティリティクラスのテスト */
@ExtendWith(MockitoExtension.class)
public class MentalCommonUtilsTest {

  @Mock private UserMapper userMapper;

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;

  @InjectMocks private MentalCommonUtils mentalCommonUtils;

  private User user;
  private List<NegativeFeel> negativeFeels;
  private List<PositiveFeel> positiveFeels;

  @BeforeEach
  public void setup() {
    user = TestUtils.createUser();
    negativeFeels = TestUtils.createNegativeFeels();
    positiveFeels = TestUtils.createPositiveFeels();
  }

  /**
   * ログイン中のユーザー情報取得のテスト
   *
   * <p>SecurityContextからユーザー名を取得し、UserMapperで検索することを検証
   */
  @Test
  public void testGetUser() {
    // SecurityContextHolderの静的メソッドをモック化
    try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder =
        mockStatic(SecurityContextHolder.class)) {

      // モックの設定
      String userName = user.getUserName();
      when(authentication.getName()).thenReturn(userName);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedSecurityContextHolder
          .when(SecurityContextHolder::getContext)
          .thenReturn(securityContext);
      when(userMapper.selectByPrimaryKey(userName)).thenReturn(user);

      // 実行
      User result = mentalCommonUtils.getUser();

      // 検証
      assertNotNull(result);
      assertEquals(user, result);
      verify(userMapper).selectByPrimaryKey(userName);
    }
  }

  /**
   * アクセス権チェックのテスト - 権限がある場合
   *
   * <p>ログインユーザーのIDと引数のuserIdが一致する場合にtrueを返すことを検証
   */
  @Test
  public void testIsAuthorized_Authorized() {
    // SecurityContextHolderの静的メソッドをモック化
    try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder =
        mockStatic(SecurityContextHolder.class)) {

      // モックの設定
      String userName = user.getUserName();
      when(authentication.getName()).thenReturn(userName);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedSecurityContextHolder
          .when(SecurityContextHolder::getContext)
          .thenReturn(securityContext);
      when(userMapper.selectByPrimaryKey(userName)).thenReturn(user);

      // 実行: ログインユーザーのIDと同じIDで検証
      boolean result = mentalCommonUtils.isAuthorized(user.getId());

      // 検証
      assertTrue(result);
    }
  }

  /**
   * アクセス権チェックのテスト - 権限がない場合
   *
   * <p>ログインユーザーのIDと引数のuserIdが一致しない場合にfalseを返すことを検証
   */
  @Test
  public void testIsAuthorized_Unauthorized() {
    // SecurityContextHolderの静的メソッドをモック化
    try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder =
        mockStatic(SecurityContextHolder.class)) {

      // モックの設定
      String userName = user.getUserName();
      when(authentication.getName()).thenReturn(userName);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedSecurityContextHolder
          .when(SecurityContextHolder::getContext)
          .thenReturn(securityContext);
      when(userMapper.selectByPrimaryKey(userName)).thenReturn(user);

      // 実行: 異なるユーザーIDで検証
      Long differentUserId = 999L;
      boolean result = mentalCommonUtils.isAuthorized(differentUserId);

      // 検証
      assertFalse(result);
    }
  }

  /**
   * ネガティブ感情IDリスト抽出のテスト - 感情リストあり
   *
   * <p>ネガティブ感情リストからIDリストが正しく抽出されることを検証
   */
  @Test
  public void testExtractedNegativeFeelsIdList_WithFeels() {
    // 実行
    List<Long> result = mentalCommonUtils.extractedNegativeFeelsIdList(negativeFeels);

    // 検証
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals(1L, result.get(0));
    assertEquals(2L, result.get(1));
    assertEquals(3L, result.get(2));
  }

  /**
   * ネガティブ感情IDリスト抽出のテスト - nullの場合
   *
   * <p>ネガティブ感情リストがnullの場合にnullを返すことを検証
   */
  @Test
  public void testExtractedNegativeFeelsIdList_Null() {
    // 実行
    List<Long> result = mentalCommonUtils.extractedNegativeFeelsIdList(null);

    // 検証
    assertNull(result);
  }

  /**
   * ネガティブ感情IDリスト抽出のテスト - 空リストの場合
   *
   * <p>ネガティブ感情リストが空の場合にnullを返すことを検証
   */
  @Test
  public void testExtractedNegativeFeelsIdList_Empty() {
    // 実行
    List<Long> result = mentalCommonUtils.extractedNegativeFeelsIdList(new ArrayList<>());

    // 検証
    assertNull(result);
  }

  /**
   * ポジティブ感情IDリスト抽出のテスト - 感情リストあり
   *
   * <p>ポジティブ感情リストからIDリストが正しく抽出されることを検証
   */
  @Test
  public void testExtractedPositiveFeelsIdList_WithFeels() {
    // 実行
    List<Long> result = mentalCommonUtils.extractedPositiveFeelsIdList(positiveFeels);

    // 検証
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals(1L, result.get(0));
    assertEquals(2L, result.get(1));
    assertEquals(3L, result.get(2));
  }

  /**
   * ポジティブ感情IDリスト抽出のテスト - nullの場合
   *
   * <p>ポジティブ感情リストがnullの場合にnullを返すことを検証
   */
  @Test
  public void testExtractedPositiveFeelsIdList_Null() {
    // 実行
    List<Long> result = mentalCommonUtils.extractedPositiveFeelsIdList(null);

    // 検証
    assertNull(result);
  }

  /**
   * ポジティブ感情IDリスト抽出のテスト - 空リストの場合
   *
   * <p>ポジティブ感情リストが空の場合にnullを返すことを検証
   */
  @Test
  public void testExtractedPositiveFeelsIdList_Empty() {
    // 実行
    List<Long> result = mentalCommonUtils.extractedPositiveFeelsIdList(new ArrayList<>());

    // 検証
    assertNull(result);
  }
}
