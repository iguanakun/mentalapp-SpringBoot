package com.mentalapp.cbt_cr.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.TestUtils;
import com.mentalapp.common.dao.DistortionListMapper;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.exception.MentalSystemException;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

/** 認知再構成法の共通ユーティリティクラスのテスト */
@ExtendWith(MockitoExtension.class)
public class CbtCrCommonUtilsTest {

  @Mock private CbtCrMapper cbtCrMapper;

  @Mock private NegativeFeelMapper negativeFeelMapper;

  @Mock private PositiveFeelMapper positiveFeelMapper;

  @Mock private DistortionListMapper distortionListMapper;

  @Mock private MentalCommonUtils mentalCommonUtils;

  @Mock private BindingResult bindingResult;

  @InjectMocks private CbtCrCommonUtils cbtCrCommonUtils;

  private CbtCr cbtCr;

  @BeforeEach
  public void setup() {
    // テスト用データの準備
    cbtCr = TestUtils.createCbtCr();
  }

  // new TagList() をモック化する共通処理
  private MockedConstruction<TagList> mockTagListConstruction() {
    return mockConstruction(
        TagList.class,
        (mock, ctx) -> {
          when(mock.tagNamesToString()).thenReturn(TestUtils.createTagNames());
        });
  }

  /** バリデーションエラーチェックのテスト - エラーがある場合 */
  @Test
  public void testCheckValidationError_HasErrors() {
    // モックの設定
    when(bindingResult.hasErrors()).thenReturn(true);

    // 実行
    boolean result = cbtCrCommonUtils.checkValidationError(bindingResult);

    // 検証
    assertTrue(result);
  }

  /** バリデーションエラーチェックのテスト - エラーがない場合 */
  @Test
  public void testCheckValidationError_NoErrors() {
    // モックの設定
    when(bindingResult.hasErrors()).thenReturn(false);

    // 実行
    boolean result = cbtCrCommonUtils.checkValidationError(bindingResult);

    // 検証
    assertFalse(result);
  }

  /** アクセス権限チェックのテスト - 権限がある場合 */
  @Test
  public void testCheckAccessPermission_Authorized() {
    // モックの設定
    when(mentalCommonUtils.isAuthorized(cbtCr.getUserId())).thenReturn(true);

    // 実行
    boolean result = cbtCrCommonUtils.checkAccessPermission(cbtCr);

    // 検証
    assertTrue(result);
    verify(mentalCommonUtils).isAuthorized(cbtCr.getUserId());
  }

  /** アクセス権限チェックのテスト - 権限がない場合 */
  @Test
  public void testCheckAccessPermission_Unauthorized() {
    // モックの設定
    when(mentalCommonUtils.isAuthorized(cbtCr.getUserId())).thenReturn(false);

    // 実行
    boolean result = cbtCrCommonUtils.checkAccessPermission(cbtCr);

    // 検証
    assertFalse(result);
    verify(mentalCommonUtils).isAuthorized(cbtCr.getUserId());
  }

  /** アクセス権限チェックのテスト - エンティティがnullの場合 */
  @Test
  public void testCheckAccessPermission_NullEntity() {
    // 実行
    boolean result = cbtCrCommonUtils.checkAccessPermission(null);

    // 検証
    assertFalse(result);
    verify(mentalCommonUtils, never()).isAuthorized(anyLong());
  }

  /** 全ての感情と思考の歪みを含むビューデータ作成のテスト */
  @Test
  public void testCreateAllFeelsAndDistortionsViewData() {
    // 戻り値の設定
    List<NegativeFeel> negativeFeels = TestUtils.createNegativeFeels();
    List<PositiveFeel> positiveFeels = TestUtils.createPositiveFeels();
    List<DistortionList> distortionLists = TestUtils.createDistortions();

    // モックの設定
    when(negativeFeelMapper.selectAll()).thenReturn(negativeFeels);
    when(positiveFeelMapper.selectAll()).thenReturn(positiveFeels);
    when(distortionListMapper.findAll()).thenReturn(distortionLists);

    // 実行
    CbtCrViewData result = cbtCrCommonUtils.createAllFeelsAndDistortionsViewData();

    // 検証
    assertNotNull(result);
    assertEquals(negativeFeels, result.getNegativeFeels());
    assertEquals(positiveFeels, result.getPositiveFeels());
    assertEquals(distortionLists, result.getDistortionLists());
    verify(negativeFeelMapper).selectAll();
    verify(positiveFeelMapper).selectAll();
    verify(distortionListMapper).findAll();
  }

  /** 全ての感情を含むビューデータ作成のテスト */
  @Test
  public void testCreateAllFeelsViewData() {
    // 戻り値の設定
    List<NegativeFeel> negativeFeels = TestUtils.createNegativeFeels();
    List<PositiveFeel> positiveFeels = TestUtils.createPositiveFeels();

    // モックの設定
    when(negativeFeelMapper.selectAll()).thenReturn(negativeFeels);
    when(positiveFeelMapper.selectAll()).thenReturn(positiveFeels);

    // 実行
    CbtCrViewData result = cbtCrCommonUtils.createAllFeelsViewData();

    // 検証
    assertNotNull(result);
    assertEquals(negativeFeels, result.getNegativeFeels());
    assertEquals(positiveFeels, result.getPositiveFeels());
    assertNull(result.getDistortionLists());
    verify(negativeFeelMapper).selectAll();
    verify(positiveFeelMapper).selectAll();
    verify(distortionListMapper, never()).findAll();
  }

  /** アクセス権限検証のテスト - 正常系 */
  @Test
  public void testValidateAccessPermission_Success() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(1L)).thenReturn(cbtCr);
    when(mentalCommonUtils.isAuthorized(cbtCr.getUserId())).thenReturn(true);

    // 実行
    CbtCr result = cbtCrCommonUtils.validateAccessPermission(1L);

    // 検証
    assertNotNull(result);
    assertEquals(cbtCr.getId(), result.getId());
    verify(cbtCrMapper).selectByPrimaryKeyWithFeelsAndTags(1L);
    verify(mentalCommonUtils).isAuthorized(cbtCr.getUserId());
  }

  /** アクセス権限検証のテスト - エンティティが存在しない場合 */
  @Test
  public void testValidateAccessPermission_EntityNotFound() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(1L)).thenReturn(null);

    // 実行
    CbtCr result = cbtCrCommonUtils.validateAccessPermission(1L);

    // 検証
    assertNull(result);
    verify(cbtCrMapper).selectByPrimaryKeyWithFeelsAndTags(1L);
    verify(mentalCommonUtils, never()).isAuthorized(anyLong());
  }

  /** アクセス権限検証のテスト - 権限がない場合 */
  @Test
  public void testValidateAccessPermission_Unauthorized() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(1L)).thenReturn(cbtCr);
    when(mentalCommonUtils.isAuthorized(cbtCr.getUserId())).thenReturn(false);

    // 実行
    CbtCr result = cbtCrCommonUtils.validateAccessPermission(1L);

    // 検証
    assertNull(result);
    verify(cbtCrMapper).selectByPrimaryKeyWithFeelsAndTags(1L);
    verify(mentalCommonUtils).isAuthorized(cbtCr.getUserId());
  }

  /** TagList取得のテスト */
  @Test
  public void testGetTagList() {
    // new TagList()をモック化
    try (MockedConstruction<TagList> mockedConstruction = mockTagListConstruction()) {
      // 実行
      TagList result = cbtCrCommonUtils.getTagList(cbtCr);

      // 検証
      assertNotNull(result);
      assertEquals(1, mockedConstruction.constructed().size());
    }
  }

  /** 思考の歪みID抽出のテスト - 歪みリストあり */
  @Test
  public void testExtractDistortionIds_WithDistortions() {
    // 実行
    List<Long> result = cbtCrCommonUtils.extractDistortionIds(cbtCr);

    // 検証
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals(1L, result.get(0));
    assertEquals(2L, result.get(1));
    assertEquals(3L, result.get(2));
  }

  /** 思考の歪みID抽出のテスト - 歪みリストなし */
  @Test
  public void testExtractDistortionIds_WithoutDistortions() {
    // モックの設定
    cbtCr.setDistortionLists(null);

    // 実行
    List<Long> result = cbtCrCommonUtils.extractDistortionIds(cbtCr);

    // 検証
    assertNull(result);
  }

  /** タグ名文字列抽出のテスト - タグあり */
  @Test
  public void testExtractTagNamesToString_WithTags() throws MentalSystemException {
    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      // 実行
      String result = cbtCrCommonUtils.extractTagNamesToString(cbtCr);

      // 検証
      assertEquals(TestUtils.createTagNames(), result);
    }
  }

  /** タグ名文字列抽出のテスト - タグなし */
  @Test
  public void testExtractTagNamesToString_WithoutTags() throws MentalSystemException {
    // モックの設定
    cbtCr.setTags(null);

    // 実行
    String result = cbtCrCommonUtils.extractTagNamesToString(cbtCr);

    // 検証
    assertNull(result);
  }

  /** タグ名文字列抽出のテスト - 空のタグリスト */
  @Test
  public void testExtractTagNamesToString_WithEmptyTags() throws MentalSystemException {
    // モックの設定
    cbtCr.setTags(new ArrayList<>());

    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      // 実行
      String result = cbtCrCommonUtils.extractTagNamesToString(cbtCr);

      // 検証
      assertEquals(TestUtils.createTagNames(), result);
    }
  }
}
