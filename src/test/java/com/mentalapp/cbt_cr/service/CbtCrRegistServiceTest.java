package com.mentalapp.cbt_cr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.User;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import com.mentalapp.user_memo_list.data.MemoListConst;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
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

  @Mock private TagMapper tagMapper;

  @Mock private CbtCrCommonUtils cbtCrCommonUtils;

  @Mock private MentalCommonUtils mentalCommonUtils;

  @Mock private HttpSession session;

  @Mock private Model model;

  @Mock private BindingResult bindingResult;

  @Mock private MessageSource messages;

  @InjectMocks private CbtCrRegistService cbtCrRegistService;

  private CbtCrInputForm form;
  private CbtCr cbtCr;
  private User user;
  MockHttpSession mockSession;

  private List<Long> negativeFeelIds;
  private List<Long> positiveFeelIds;
  private List<Long> distortionIds;

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
    when(cbtCrCommonUtils.createAllFeelsViewData()).thenReturn(new CbtCrViewData());
    when(messages.getMessage("error.atleastone.required", null, Locale.JAPAN)).thenReturn("エラー");
    form.setId(null);
    form.setFact(null);
    form.setMind(null);
    form.setWhyCorrect(null);
    form.setWhyDoubt(null);
    form.setNewThought(null);
    form.setTagNames(null);
    form.setPositiveFeelIds(null);
    form.setNegativeFeelIds(null);
    form.setDistortionIds(null);
  }

  /** 新規登録処理のテスト - 正常系 */
  @Test
  public void testProcessRegist_Success() {
    // モックの設定
    when(mentalCommonUtils.getUser()).thenReturn(user);
    when(cbtCrCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    when(cbtCrMapper.insert(any(CbtCr.class))).thenReturn(1);
    when(cbtCrNegativeFeelMapper.insert(any(CbtCrNegativeFeel.class))).thenReturn(1);
    when(cbtCrPositiveFeelMapper.insert(any(CbtCrPositiveFeel.class))).thenReturn(1);

    // セッションの値を設定
    mockSession.setAttribute("fact", "テスト状況");
    mockSession.setAttribute("mind", "テスト思考");
    mockSession.setAttribute("negativeFeelIds", TestUtils.createNegativeFeelIds());
    mockSession.setAttribute("positiveFeelIds", TestUtils.createPositiveFeelIds());

    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      String result = cbtCrRegistService.processRegist(form, bindingResult, model);

      // 結果確認
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
      verify(cbtCrMapper).insert(any(CbtCr.class));
    }
  }

  /** 新規登録処理のテスト - バリデーションエラーの場合 */
  @Test
  public void testProcessRegist_ValidationError() {
    // モックの設定
    when(cbtCrCommonUtils.checkValidationError(bindingResult)).thenReturn(true);
    when(cbtCrCommonUtils.createAllFeelsAndDistortionsViewData()).thenReturn(new CbtCrViewData());

    // 実行
    String result = cbtCrRegistService.processRegist(form, bindingResult, model);

    // 検証
    assertEquals(CbtCrConst.STEP2_PATH, result);
    verify(model).addAttribute(eq("viewData"), any(CbtCrViewData.class));
    verify(cbtCrMapper, never()).insert(any(CbtCr.class));
  }

  /** 更新処理のテスト - 正常系 */
  @Test
  public void testProcessUpdate_Success() {
    // モックの設定
    // Only mock what's actually used in the method
    when(cbtCrMapper.selectByPrimaryKey(1L)).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(true);

    // 実行
    String result = cbtCrRegistService.processUpdate(form, bindingResult, model, 1L);

    // 検証
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    verify(cbtCrNegativeFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrPositiveFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrDistortionRelationMapper).deleteByCbtCrId(1L);
    verify(cbtCrMapper).updateByPrimaryKey(cbtCr);
  }

  /** 更新処理のテスト - バリデーションエラーの場合 */
  @Test
  public void testProcessUpdate_ValidationError() {
    // モックの設定
    when(cbtCrCommonUtils.checkValidationError(bindingResult)).thenReturn(true);
    when(cbtCrCommonUtils.createAllFeelsAndDistortionsViewData()).thenReturn(new CbtCrViewData());

    // 実行
    String result = cbtCrRegistService.processUpdate(form, bindingResult, model, 1L);

    // 検証
    assertEquals(CbtCrConst.EDIT_STEP2_PATH, result);
    verify(model).addAttribute(eq("viewData"), any(CbtCrViewData.class));
    verify(cbtCrMapper, never()).updateByPrimaryKey(any(CbtCr.class));
  }

  /** 更新処理のテスト - 存在しない場合 */
  @Test
  public void testProcessUpdate_NotFound() {
    // モックの設定
    // Only mock what's necessary
    when(cbtCrMapper.selectByPrimaryKey(1L)).thenReturn(null);

    // 実行
    String result = cbtCrRegistService.processUpdate(form, bindingResult, model, 1L);

    // 検証
    assertEquals("redirect:/memos", result);
    verify(cbtCrMapper, never()).updateByPrimaryKey(any(CbtCr.class));
  }

  /** 更新処理のテスト - アクセス権限なしの場合 */
  @Test
  public void testProcessUpdate_Unauthorized() {
    // モックの設定
    // Only mock what's necessary
    when(cbtCrMapper.selectByPrimaryKey(1L)).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(false);

    // 実行
    String result = cbtCrRegistService.processUpdate(form, bindingResult, model, 1L);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
    verify(cbtCrMapper, never()).updateByPrimaryKey(any(CbtCr.class));
  }

  /** 削除処理のテスト - 正常系 */
  @Test
  public void testProcessDelete_Success() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKey(1L)).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(true);

    // 実行
    String result = cbtCrRegistService.processDelete(1L);

    // 検証
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    verify(cbtCrNegativeFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrPositiveFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrDistortionRelationMapper).deleteByCbtCrId(1L);
    verify(cbtCrMapper).deleteByPrimaryKey(1L);
  }

  /** 削除処理のテスト - 存在しない場合 */
  @Test
  public void testProcessDelete_NotFound() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKey(1L)).thenReturn(null);

    // 実行
    String result = cbtCrRegistService.processDelete(1L);

    // 検証
    assertEquals("redirect:/memos", result);
    verify(cbtCrMapper, never()).deleteByPrimaryKey(anyLong());
  }

  /** 削除処理のテスト - アクセス権限なしの場合 */
  @Test
  public void testProcessDelete_Unauthorized() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKey(1L)).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(false);

    // 実行
    String result = cbtCrRegistService.processDelete(1L);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
    verify(cbtCrMapper, never()).deleteByPrimaryKey(anyLong());
  }

  /** 新規登録処理のテスト - セッションデータの取得 */
  @Test
  public void testProcessRegist_WithSessionData() {
    // モックの設定
    lenient().when(bindingResult.hasErrors()).thenReturn(false);
    lenient().when(session.getAttribute("negativeFeelIds")).thenReturn(negativeFeelIds);
    lenient().when(session.getAttribute("positiveFeelIds")).thenReturn(positiveFeelIds);
    lenient().when(session.getAttribute("fact")).thenReturn("テスト状況");
    lenient().when(session.getAttribute("mind")).thenReturn("テスト思考");
    doAnswer(
            invocation -> {
              CbtCr cbtCr = invocation.getArgument(0);
              cbtCr.setId(1L);
              return 1;
            })
        .when(cbtCrMapper)
        .insert(any(CbtCr.class));

    // 実行
    String result = cbtCrRegistService.processRegist(form, bindingResult, model);

    // 検証
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    verify(session).getAttribute("negativeFeelIds");
    verify(session).getAttribute("positiveFeelIds");
    verify(session).getAttribute("fact");
    verify(session).getAttribute("mind");
    verify(session).removeAttribute("negativeFeelIds");
    verify(session).removeAttribute("positiveFeelIds");
    verify(session).removeAttribute("fact");
    verify(session).removeAttribute("mind");
    verify(cbtCrMapper).insert(any(CbtCr.class));
  }

  /** hasAnyContent()メソッドのテスト - コンテンツがある場合 */
  @Test
  public void testHasAnyContent_WithContent() {
    // モックの設定
    when(session.getAttribute("negativeFeelIds")).thenReturn(Arrays.asList(1L, 2L));
    when(session.getAttribute("positiveFeelIds")).thenReturn(null);
    when(session.getAttribute("fact")).thenReturn(null);
    when(session.getAttribute("mind")).thenReturn(null);

    // フォームの設定
    CbtCrInputForm form = new CbtCrInputForm();
    form.setWhyCorrect(null);
    form.setWhyDoubt(null);
    form.setNewThought(null);
    form.setDistortionIds(null);

    // 実行
    boolean result = cbtCrRegistService.hasAnyContent(form);

    // 検証
    assertEquals(true, result);
  }

  /** hasAnyContent()メソッドのテスト - コンテンツがない場合 */
  @Test
  public void testHasAnyContent_WithoutContent() {
    // モックの設定
    when(session.getAttribute("negativeFeelIds")).thenReturn(null);
    when(session.getAttribute("positiveFeelIds")).thenReturn(null);
    when(session.getAttribute("fact")).thenReturn(null);
    when(session.getAttribute("mind")).thenReturn(null);

    // フォームの設定
    CbtCrInputForm form = new CbtCrInputForm();
    form.setWhyCorrect(null);
    form.setWhyDoubt(null);
    form.setNewThought(null);
    form.setDistortionIds(null);

    // 実行
    boolean result = cbtCrRegistService.hasAnyContent(form);

    // 検証
    assertEquals(false, result);
  }

  /** insert()メソッドのテスト */
  @Test
  public void testInsert() {
    // テストデータの準備
    CbtCr cbtCr = new CbtCr();
    cbtCr.setId(1L);
    cbtCr.setUserId(1L);
    List<Long> negativeFeelIds = Arrays.asList(1L, 2L);
    List<Long> positiveFeelIds = Arrays.asList(3L, 4L);
    List<Long> distortionIds = Arrays.asList(5L, 6L);
    String tagNames = "タグ1 タグ2";

    // モックの設定
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong())).thenReturn(null);
    when(tagMapper.insert(any())).thenReturn(1);

    // 実行
    cbtCrRegistService.save(cbtCr, negativeFeelIds, positiveFeelIds, distortionIds, tagNames);

    // 検証
    verify(cbtCrMapper).insert(cbtCr);
    verify(cbtCrNegativeFeelMapper, times(2)).insert(any());
    verify(cbtCrPositiveFeelMapper, times(2)).insert(any());
    verify(cbtCrDistortionRelationMapper, times(2)).insert(any());
    verify(cbtCrTagRelationMapper, times(2)).insert(anyLong(), anyLong());
  }

  /** update()メソッドのテスト */
  @Test
  public void testUpdate() {
    // テストデータの準備
    CbtCr cbtCr = new CbtCr();
    cbtCr.setId(1L);
    cbtCr.setUserId(1L);
    List<Long> negativeFeelIds = Arrays.asList(1L, 2L);
    List<Long> positiveFeelIds = Arrays.asList(3L, 4L);
    List<Long> distortionIds = Arrays.asList(5L, 6L);
    String tagNames = "タグ1 タグ2";

    // モックの設定
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong())).thenReturn(null);
    when(tagMapper.insert(any())).thenReturn(1);

    // 実行
    cbtCrRegistService.update(cbtCr, negativeFeelIds, positiveFeelIds, distortionIds, tagNames);

    // 検証
    verify(cbtCrNegativeFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrPositiveFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrDistortionRelationMapper).deleteByCbtCrId(1L);
    verify(cbtCrTagRelationMapper).deleteByMonitoringId(1L);
    verify(cbtCrMapper).updateByPrimaryKey(cbtCr);
    verify(cbtCrNegativeFeelMapper, times(2)).insert(any());
    verify(cbtCrPositiveFeelMapper, times(2)).insert(any());
    verify(cbtCrDistortionRelationMapper, times(2)).insert(any());
    verify(cbtCrTagRelationMapper, times(2)).insert(anyLong(), anyLong());
  }

  /** delete()メソッドのテスト */
  @Test
  public void testDelete() {
    // 実行
    cbtCrRegistService.delete(1L);

    // 検証
    verify(cbtCrNegativeFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrPositiveFeelMapper).deleteByCbtCrId(1L);
    verify(cbtCrDistortionRelationMapper).deleteByCbtCrId(1L);
    verify(cbtCrTagRelationMapper).deleteByMonitoringId(1L);
    verify(cbtCrMapper).deleteByPrimaryKey(1L);
  }
}
