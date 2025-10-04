package com.mentalapp.cbt_cr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_cr.dao.CbtCrDistortionRelationMapper;
import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.dao.CbtCrNegativeFeelMapper;
import com.mentalapp.cbt_cr.dao.CbtCrPositiveFeelMapper;
import com.mentalapp.cbt_cr.dao.CbtCrTagRelationMapper;
import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.entity.CbtCrNegativeFeel;
import com.mentalapp.cbt_cr.entity.CbtCrPositiveFeel;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.TestUtils;
import com.mentalapp.common.entity.User;
import com.mentalapp.common.exception.MentalSystemException;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import com.mentalapp.user_memo_list.data.MemoListConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/** 認知再構成法の登録・更新・削除処理を行うサービスクラスのテスト */
@ExtendWith(MockitoExtension.class)
public class CbtCrRegistServiceTest {

  @Mock private CbtCrMapper cbtCrMapper;

  @Mock private CbtCrNegativeFeelMapper cbtCrNegativeFeelMapper;

  @Mock private CbtCrPositiveFeelMapper cbtCrPositiveFeelMapper;

  @Mock private CbtCrDistortionRelationMapper cbtCrDistortionRelationMapper;

  @Mock private CbtCrTagRelationMapper cbtCrTagRelationMapper;

  @Mock private CbtCrCommonUtils cbtCrCommonUtils;

  @Mock private MentalCommonUtils mentalCommonUtils;

  @Mock private Model model;

  @Mock private BindingResult bindingResult;

  @InjectMocks private CbtCrRegistService cbtCrRegistService;

  private CbtCrInputForm form;
  private CbtCr cbtCr;
  private User user;
  MockHttpSession mockSession;

  @BeforeEach
  public void setup() {
    // テスト用データの準備
    form = TestUtils.createCbtCrInputForm();
    user = TestUtils.createUser();
    cbtCr = TestUtils.createCbtCr();

    // メッセージプロパティに値をセット
    // TODO:バリデーションエラーメッセージの実装後に有効
    // ReflectionTestUtils.setField(cbtCrRegistService, "messages", messages);

    // モックセッションの作成
    mockSession = new MockHttpSession();
    ReflectionTestUtils.setField(cbtCrRegistService, "session", mockSession);
  }

  // new TagList() をモック化する共通処理
  private MockedConstruction<TagList> mockTagListConstruction() {
    return mockConstruction(
        TagList.class,
        (mock, ctx) -> {
          doNothing().when(mock).insertTagList();
          doNothing().when(mock).insertMonitoringTagRelation(any(), anyLong());
        });
  }

  // バリデーションエラーありの共通設定
  private void setupValidationError() {
    when(cbtCrCommonUtils.checkValidationError(bindingResult)).thenReturn(true);
    when(cbtCrCommonUtils.createAllFeelsAndDistortionsViewData()).thenReturn(new CbtCrViewData());
    // TODO:バリデーションエラーメッセージの実装後に有効
    //  when(messages.getMessage("error.atleastone.required", null,Locale.JAPAN)).thenReturn("エラー");
    setupValidationErrorForm();
  }

  private void setupValidationErrorForm() {
    form.setWhyCorrect(null);
    form.setWhyDoubt(null);
    form.setNewThought(null);
    form.setDistortionIds(null);
    form.setTagNames(null);
  }

  /** 新規登録処理のテスト - 正常系 */
  @Test
  public void testProcessRegist_Success() throws MentalSystemException {
    // モックの設定
    when(mentalCommonUtils.getUser()).thenReturn(user);
    when(cbtCrCommonUtils.checkValidationError(bindingResult)).thenReturn(false);

    when(cbtCrMapper.insert(any(CbtCr.class))).thenReturn(1);
    when(cbtCrNegativeFeelMapper.insert(any(CbtCrNegativeFeel.class))).thenReturn(1);
    when(cbtCrPositiveFeelMapper.insert(any(CbtCrPositiveFeel.class))).thenReturn(1);

    // セッションの値を設定
    setupSessionStep1();

    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      String result = cbtCrRegistService.processRegist(form, bindingResult, model);

      // 結果確認
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
      verify(cbtCrMapper).insert(any(CbtCr.class));
    }
  }

  private void setupSessionStep1() {
    mockSession.setAttribute("fact", "テスト状況");
    mockSession.setAttribute("mind", "テスト思考");
    mockSession.setAttribute("negativeFeelIds", TestUtils.createNegativeFeelIds());
    mockSession.setAttribute("positiveFeelIds", TestUtils.createPositiveFeelIds());
  }

  /** 新規登録処理のテスト - バリデーションエラーの場合 */
  @Test
  public void testProcessRegist_ValidationError() throws MentalSystemException {
    // モックの設定
    setupValidationError();

    // 実行
    String result = cbtCrRegistService.processRegist(form, bindingResult, model);

    // 検証
    assertEquals(CbtCrConst.NEW_PATH, result);
  }

  /** 更新処理のテスト - 正常系 */
  @Test
  public void testProcessUpdate_Success() throws MentalSystemException {
    // モックの設定
    when(mentalCommonUtils.getUser()).thenReturn(user);
    when(cbtCrCommonUtils.checkValidationError(bindingResult)).thenReturn(false);

    when(cbtCrNegativeFeelMapper.deleteByCbtCrId(cbtCr.getId())).thenReturn(1);
    when(cbtCrPositiveFeelMapper.deleteByCbtCrId(cbtCr.getId())).thenReturn(1);
    when(cbtCrDistortionRelationMapper.deleteByCbtCrId(cbtCr.getId())).thenReturn(1);
    when(cbtCrTagRelationMapper.deleteByMonitoringId(cbtCr.getId())).thenReturn(1);

    when(cbtCrMapper.updateByPrimaryKey(any(CbtCr.class))).thenReturn(1);
    when(cbtCrNegativeFeelMapper.insert(any(CbtCrNegativeFeel.class))).thenReturn(1);
    when(cbtCrPositiveFeelMapper.insert(any(CbtCrPositiveFeel.class))).thenReturn(1);

    // セッションの値を設定
    setupSessionStep1();

    // 実行
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      String result = cbtCrRegistService.processUpdate(form, bindingResult, model, cbtCr.getId());

      // 検証
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
      verify(cbtCrMapper).updateByPrimaryKey(any(CbtCr.class));
    }
  }

  /** 更新処理のテスト - バリデーションエラーの場合 */
  @Test
  public void testProcessUpdate_ValidationError() throws MentalSystemException {
    // モックの設定
    setupValidationError();

    // 実行
    String result = cbtCrRegistService.processUpdate(form, bindingResult, model, cbtCr.getId());

    // 検証
    assertEquals(CbtCrConst.EDIT_PATH, result);
  }

  /** 削除処理のテスト - 正常系 */
  @Test
  public void testProcessDelete_Success() throws MentalSystemException {
    // モックの設定
    when(cbtCrCommonUtils.validateAccessPermission(cbtCr.getId())).thenReturn(cbtCr);
    when(cbtCrMapper.deleteByPrimaryKey(cbtCr.getId())).thenReturn(1);

    // 実行
    String result = cbtCrRegistService.processDelete(cbtCr.getId());

    // 検証
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    verify(cbtCrMapper).deleteByPrimaryKey(cbtCr.getId());
  }

  /** 削除処理のテスト - 存在しない場合 */
  @Test
  public void testProcessDelete_NotFound() throws MentalSystemException {
    // モックの設定
    when(cbtCrCommonUtils.validateAccessPermission(cbtCr.getId())).thenReturn(null);

    // 実行
    String result = cbtCrRegistService.processDelete(cbtCr.getId());

    // 検証
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    verify(cbtCrMapper, never()).deleteByPrimaryKey(cbtCr.getId());
  }

  /** バリデーションテスト */
  @Test
  void testHasAnyContent_negativeFeelIds() {
    setupValidationErrorForm();
    mockSession.setAttribute("negativeFeelIds", TestUtils.createNegativeFeelIds());
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_positiveFeelIds() {
    setupValidationErrorForm();
    mockSession.setAttribute("positiveFeelIds", TestUtils.createPositiveFeelIds());
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_fact() {
    setupValidationErrorForm();
    mockSession.setAttribute("fact", "テスト状況");

    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_mind() {
    setupValidationErrorForm();
    mockSession.setAttribute("mind", "テスト思考");

    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_distortionIds() {
    setupValidationErrorForm();
    form.setDistortionIds(TestUtils.createDistortionIds());
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_tagNames() {
    setupValidationErrorForm();
    form.setTagNames(TestUtils.createTagNames());
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_whyCorrect() {
    setupValidationErrorForm();
    form.setWhyCorrect("テスト正しい証拠");
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_whyDoubt() {
    setupValidationErrorForm();
    form.setWhyDoubt("テスト間違いの証拠");
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_newThought() {
    setupValidationErrorForm();
    form.setNewThought("テスト新しい考え方");
    assertTrue(cbtCrRegistService.hasAnyContent(form));
  }

  @Test
  void testHasAnyContent_emptyAll() {
    setupValidationErrorForm();
    assertFalse(cbtCrRegistService.hasAnyContent(form));
  }
}
