package com.mentalapp.cbt_basic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsPositiveFeelMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsTagRelationMapper;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@ExtendWith(MockitoExtension.class)
class CbtBasicRegistServiceTest {
  @InjectMocks private CbtBasicsRegistService cbtBasicsRegistService;

  @Mock private CbtBasicCommonUtils cbtBasicCommonUtils;
  @Mock private MentalCommonUtils mentalCommonUtils;
  @Mock private CbtBasicsMapper cbtBasicsMapper;
  @Mock private CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper;
  @Mock private CbtBasicsPositiveFeelMapper cbtBasicsPositiveFeelMapper;
  @Mock private CbtBasicsTagRelationMapper cbtBasicsTagRelationMapper;
  @Mock private Model model;
  @Mock private BindingResult bindingResult;

  private CbtBasicsInputForm form;
  private User user;
  private CbtBasics cbtBasics;

  @BeforeEach
  void setup() {
    // テストデータ生成
    form = TestUtils.createCbtBasicsInputForm();
    user = TestUtils.createUser();
    cbtBasics = TestUtils.createCbtBasics();
  }

  /** 共通ヘルパーメソッド */

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
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(true);
    when(cbtBasicCommonUtils.createAllFeelsViewData()).thenReturn(new CbtBasicsViewData());
    doNothing().when(mentalCommonUtils).addValidationErrorMessage(model);
    form.setCbtBasics(null);
    form.setTagNames(null);
    form.setPositiveFeelIds(null);
    form.setNegativeFeelIds(null);
  }

  // 認可可否を切り替える共通設定
  private void setupAuthorized(boolean authorized) {
    when(mentalCommonUtils.isAuthorized(anyLong())).thenReturn(authorized);
  }

  /** テストケース */
  @Test
  // バリデーションエラーなし
  void testProcessRegist() throws MentalSystemException {
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    when(mentalCommonUtils.getUser()).thenReturn(user);
    when(cbtBasicsMapper.insert(form.getCbtBasics())).thenReturn(1);
    when(cbtBasicsNegativeFeelMapper.insert(anyLong(), anyLong())).thenReturn(1);
    when(cbtBasicsPositiveFeelMapper.insert(anyLong(), anyLong())).thenReturn(1);

    // new TagList()をモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      String result = cbtBasicsRegistService.processRegist(form, bindingResult, model);

      // 結果確認
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
      verify(cbtBasicsMapper).insert(form.getCbtBasics());
    }
  }

  @Test
  // バリデーションエラーあり
  void testProcessRegist_ValidationError() throws MentalSystemException {
    setupValidationError();
    String result = cbtBasicsRegistService.processRegist(form, bindingResult, model);
    assertEquals(CbtBasicsConst.NEW_PATH, result);
  }

  @Test
  // 更新処理（バリデーションエラーなし、認可あり）
  void testProcessUpdate_Authorized() throws MentalSystemException {
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    setupAuthorized(true);
    when(cbtBasicsMapper.updateByPrimaryKey(any())).thenReturn(1);

    // TagListのモック化
    try (MockedConstruction<TagList> ignored = mockTagListConstruction()) {
      // UPDATE向けにformを更新
      CbtBasicsInputForm updateForm = setupUpdateForm(form);
      String result = cbtBasicsRegistService.processUpdate(updateForm, bindingResult, model, 1L);
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    }
  }

  private CbtBasicsInputForm setupUpdateForm(CbtBasicsInputForm form) {
    CbtBasicsInputForm updateForm = new CbtBasicsInputForm();
    // formの値をコピー
    updateForm.setCbtBasics(form.getCbtBasics());
    updateForm.setTagNames(form.getTagNames());
    updateForm.setPositiveFeelIds(form.getPositiveFeelIds());
    updateForm.setNegativeFeelIds(form.getNegativeFeelIds());

    // 編集なので、編集対象のユーザIDをセット
    updateForm.getCbtBasics().setUserId(user.getId());

    return updateForm;
  }

  @Test
  // 更新処理（バリデーションエラーあり）
  void testProcessUpdate_ValidationError() throws MentalSystemException {
    // UPDATE向けにformを更新
    CbtBasicsInputForm updateForm = setupUpdateForm(form);
    setupValidationError();
    String result = cbtBasicsRegistService.processUpdate(updateForm, bindingResult, model, 1L);
    assertEquals(CbtBasicsConst.EDIT_PATH, result);
  }

  @Test
  // 更新処理（認可エラー）
  void testProcessUpdate_Unauthorized() throws MentalSystemException {
    // UPDATE向けにformを更新
    CbtBasicsInputForm updateForm = setupUpdateForm(form);
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    setupAuthorized(false);
    String result = cbtBasicsRegistService.processUpdate(updateForm, bindingResult, model, 1L);
    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
  }

  @Test
  // 削除処理（認可あり）
  void testProcessDelete() throws MentalSystemException {
    when(cbtBasicsMapper.selectByPrimaryKey(anyLong())).thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(any(CbtBasics.class))).thenReturn(true);
    when(cbtBasicsMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
    when(cbtBasicsTagRelationMapper.deleteByMonitoringId(cbtBasics.getId())).thenReturn(1);

    String result = cbtBasicsRegistService.processDelete(1L);
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
  }

  @Test
  // 削除処理（認可エラー）
  void testProcessDelete_Unauthorized() throws MentalSystemException {
    when(cbtBasicsMapper.selectByPrimaryKey(anyLong())).thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(any(CbtBasics.class))).thenReturn(false);

    String result = cbtBasicsRegistService.processDelete(1L);
    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
  }
}
