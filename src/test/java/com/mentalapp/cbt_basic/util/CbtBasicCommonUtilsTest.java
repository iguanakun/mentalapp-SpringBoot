package com.mentalapp.cbt_basic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.TestUtils;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
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

/** CBT Basicsの共通ユーティリティクラスのテスト */
@ExtendWith(MockitoExtension.class)
public class CbtBasicCommonUtilsTest {

  @Mock private NegativeFeelMapper negativeFeelMapper;

  @Mock private PositiveFeelMapper positiveFeelMapper;

  @Mock private MentalCommonUtils mentalCommonUtils;

  @Mock private BindingResult bindingResult;

  @InjectMocks private CbtBasicCommonUtils cbtBasicCommonUtils;

  private CbtBasics cbtBasics;

  @BeforeEach
  public void setup() {
    // テスト用データの準備
    cbtBasics = TestUtils.createCbtBasics();
  }

  // new TagList() をモック化する共通処理
  private MockedConstruction<TagList> mockTagListConstruction() {
    return mockConstruction(
        TagList.class,
        (mock, ctx) -> {
          when(mock.tagNamesToString()).thenReturn(TestUtils.createTagNames()); // ← 修正
        });
  }

  /** エンティティからフォームへの変換テスト - タグ情報あり */
  @Test
  public void testConvertToForm() throws MentalSystemException {
    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      CbtBasicsInputForm result = cbtBasicCommonUtils.convertToForm(cbtBasics);
      // 結果確認
      assertEquals(cbtBasics, result.getCbtBasics());
      assertEquals(TestUtils.createTagNames(), result.getTagNames());
    }
  }

  /** エンティティからフォームへの変換テスト - タグ情報あり、ネガティブ感情なし */
  @Test
  public void testConvertToForm_WithoutNegativeFeel() throws MentalSystemException {
    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      cbtBasics.setNegativeFeels(null);
      CbtBasicsInputForm result = cbtBasicCommonUtils.convertToForm(cbtBasics);
      // 結果確認
      assertEquals(cbtBasics, result.getCbtBasics());
      assertEquals(TestUtils.createTagNames(), result.getTagNames());
    }
  }

  /** エンティティからフォームへの変換テスト - タグ情報あり、ポジティブ感情なし */
  @Test
  public void testConvertToForm_WithoutPositiveFeel() throws MentalSystemException {
    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      cbtBasics.setPositiveFeels(null);
      CbtBasicsInputForm result = cbtBasicCommonUtils.convertToForm(cbtBasics);
      // 結果確認
      assertEquals(cbtBasics, result.getCbtBasics());
      assertEquals(TestUtils.createTagNames(), result.getTagNames());
    }
  }

  /** エンティティからフォームへの変換テスト - タグ情報なし */
  @Test
  public void testConvertToForm_WithoutTags() throws MentalSystemException {
    // モックの設定
    cbtBasics.setTags(null);

    // 実行
    CbtBasicsInputForm result = cbtBasicCommonUtils.convertToForm(cbtBasics);

    // 検証
    assertEquals(cbtBasics, result.getCbtBasics());
    assertNull(result.getTagNames());
  }

  /** エンティティからフォームへの変換テスト - 空のタグリスト */
  @Test
  public void testConvertToForm_WithEmptyTags() throws MentalSystemException {
    // モックの設定
    cbtBasics.setTags(new ArrayList<>());

    // 実行
    CbtBasicsInputForm result = cbtBasicCommonUtils.convertToForm(cbtBasics);

    // 検証
    assertEquals(cbtBasics, result.getCbtBasics());
    assertNull(result.getTagNames());
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
    CbtBasicsViewData result = cbtBasicCommonUtils.createAllFeelsViewData();

    // 検証
    assertEquals(negativeFeels, result.getNegativeFeels());
    assertEquals(positiveFeels, result.getPositiveFeels());
    verify(negativeFeelMapper).selectAll();
    verify(positiveFeelMapper).selectAll();
  }

  /** バリデーションエラーチェックのテスト - エラーがある場合 */
  @Test
  public void testCheckValidationError_HasErrors() {
    // モックの設定
    when(bindingResult.hasErrors()).thenReturn(true);

    // 実行
    boolean result = cbtBasicCommonUtils.checkValidationError(bindingResult);

    // 検証
    assertTrue(result);
  }

  /** バリデーションエラーチェックのテスト - エラーがない場合 */
  @Test
  public void testCheckValidationError_NoErrors() {
    // モックの設定
    when(bindingResult.hasErrors()).thenReturn(false);

    // 実行
    boolean result = cbtBasicCommonUtils.checkValidationError(bindingResult);

    // 検証
    assertFalse(result);
  }

  /** アクセス権限チェックのテスト - 権限がある場合 */
  @Test
  public void testCheckAccessPermission_Authorized() {
    // モックの設定
    when(mentalCommonUtils.isAuthorized(cbtBasics.getUserId())).thenReturn(true);

    // 実行
    boolean result = cbtBasicCommonUtils.checkAccessPermission(cbtBasics);

    // 検証
    assertTrue(result);
    verify(mentalCommonUtils).isAuthorized(cbtBasics.getUserId());
  }

  /** アクセス権限チェックのテスト - 権限がない場合 */
  @Test
  public void testCheckAccessPermission_Unauthorized() {
    // モックの設定
    when(mentalCommonUtils.isAuthorized(cbtBasics.getUserId())).thenReturn(false);

    // 実行
    boolean result = cbtBasicCommonUtils.checkAccessPermission(cbtBasics);

    // 検証
    assertFalse(result);
    verify(mentalCommonUtils).isAuthorized(cbtBasics.getUserId());
  }

  /** アクセス権限チェックのテスト - エンティティがnullの場合 */
  @Test
  public void testCheckAccessPermission_NullEntity() {
    // 実行
    boolean result = cbtBasicCommonUtils.checkAccessPermission(null);

    // 検証
    assertFalse(result);
    verify(mentalCommonUtils, never()).isAuthorized(anyLong());
  }
}
