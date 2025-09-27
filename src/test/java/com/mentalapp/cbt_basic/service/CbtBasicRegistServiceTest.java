package com.mentalapp.cbt_basic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
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
import com.mentalapp.common.exception.DatabaseException;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.common.util.TagList;
import com.mentalapp.user_memo_list.data.MemoListConst;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@ExtendWith(MockitoExtension.class)
public class CbtBasicRegistServiceTest {
  @InjectMocks private CbtBasicsRegistService cbtBasicsRegistService;

  @Mock private CbtBasicCommonUtils cbtBasicCommonUtils;

  @Mock private MentalCommonUtils mentalCommonUtils;

  @Mock private CbtBasicsMapper cbtBasicsMapper;

  @Mock private CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper;

  @Mock private CbtBasicsPositiveFeelMapper cbtBasicsPositiveFeelMapper;

  @Mock private CbtBasicsTagRelationMapper cbtBasicsTagRelationMapper;

  @Mock private Model model;

  @Mock private BindingResult bindingResult;

  @Mock private MessageSource messages;

  private CbtBasicsInputForm form;

  private User user;

  private CbtBasics cbtBasics;

  @BeforeEach
  void setup() {
    // テストデータ生成
    form = TestUtils.createCbtBasicsInputForm();
    user = TestUtils.createUser();
    cbtBasics = TestUtils.createCbtBasics();
    // メッセージプロパティに値をセット
    ReflectionTestUtils.setField(cbtBasicsRegistService, "messages", messages);
  }

  @Test
  // バリデーションエラーなし
  void testProcessRegist() {
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    when(mentalCommonUtils.getUser()).thenReturn(user);
    when(cbtBasicsMapper.insert(form.getCbtBasics())).thenReturn(1);
    when(cbtBasicsNegativeFeelMapper.insert(anyLong(), anyLong())).thenReturn(1);
    when(cbtBasicsPositiveFeelMapper.insert(anyLong(), anyLong())).thenReturn(1);

    // new TagList()をモック化
    try (MockedConstruction<TagList> mocked =
        mockConstruction(
            TagList.class,
            (mock, context) -> {
              doNothing().when(mock).insertTagList();
              doNothing().when(mock).insertMonitoringTagRelation(any(), anyLong());
            })) {

      String result = cbtBasicsRegistService.processRegist(form, bindingResult, model);

      // 結果確認
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
      verify(cbtBasicsMapper).insert(form.getCbtBasics());
      verify(cbtBasicsNegativeFeelMapper, times(form.getNegativeFeelIds().size()))
          .insert(anyLong(), anyLong());
      verify(cbtBasicsPositiveFeelMapper, times(form.getPositiveFeelIds().size()))
          .insert(anyLong(), anyLong());
    }
  }

  @Test
  // バリデーションエラーあり
  void testProcessRegist_ValidationError() {
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(true);
    when(cbtBasicCommonUtils.createAllFeelsViewData()).thenReturn(new CbtBasicsViewData());
    when(messages.getMessage("error.atleastone.required", null, Locale.JAPAN)).thenReturn("エラー");

    // すべての入力値が空
    form.setCbtBasics(null);
    form.setTagNames(null);
    form.setPositiveFeelIds(null);
    form.setNegativeFeelIds(null);

    String result = cbtBasicsRegistService.processRegist(form, bindingResult, model);

    assertEquals(CbtBasicsConst.NEW_PATH, result);
  }

  @Test
  void testProcessUpdate() {
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    when(mentalCommonUtils.isAuthorized(anyLong())).thenReturn(true);
    when(cbtBasicsMapper.updateByPrimaryKey(any(CbtBasics.class))).thenReturn(1);
    when(cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(anyLong())).thenReturn(1);
    when(cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(anyLong())).thenReturn(1);
    when(cbtBasicsTagRelationMapper.deleteByMonitoringId(anyLong())).thenReturn(1);
    when(cbtBasicsNegativeFeelMapper.insert(anyLong(), anyLong())).thenReturn(1);
    when(cbtBasicsPositiveFeelMapper.insert(anyLong(), anyLong())).thenReturn(1);

    // フォームにユーザーIDを設定
    form.getCbtBasics().setUserId(user.getId());

    // TagListのモック化
    try (MockedConstruction<TagList> mocked =
        mockConstruction(
            TagList.class,
            (mock, context) -> {
              doNothing().when(mock).insertTagList();
              doNothing().when(mock).insertMonitoringTagRelation(any(), anyLong());
            })) {

      // テスト実行
      String result = cbtBasicsRegistService.processUpdate(form, bindingResult, model, 1L);

      // 検証
      assertEquals(MemoListConst.REDIRECT_MEMOS, result);
    }
  }

  @Test
  void testProcessUpdate_ValidationError() {
    // モックの設定
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(true);
    when(cbtBasicCommonUtils.createAllFeelsViewData()).thenReturn(new CbtBasicsViewData());
    when(messages.getMessage("error.atleastone.required", null, Locale.JAPAN)).thenReturn("エラー");

    // すべての入力値が空
    form.setCbtBasics(null);
    form.setTagNames(null);
    form.setPositiveFeelIds(null);
    form.setNegativeFeelIds(null);

    // テスト実行
    String result = cbtBasicsRegistService.processUpdate(form, bindingResult, model, 1L);

    // 検証
    assertEquals(CbtBasicsConst.EDIT_PATH, result);
  }

  @Test
  void testProcessUpdate_Unauthorized() {
    // モックの設定
    when(cbtBasicCommonUtils.checkValidationError(bindingResult)).thenReturn(false);
    when(mentalCommonUtils.isAuthorized(anyLong())).thenReturn(false);

    // フォームにユーザーIDを設定
    form.getCbtBasics().setUserId(2L); // 異なるユーザーID

    // テスト実行
    String result = cbtBasicsRegistService.processUpdate(form, bindingResult, model, 1L);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
  }

  @Test
  void testProcessDelete() {
    // モックの設定
    when(cbtBasicsMapper.selectByPrimaryKey(anyLong())).thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(any(CbtBasics.class))).thenReturn(true);
    when(cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(anyLong())).thenReturn(1);
    when(cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(anyLong())).thenReturn(1);
    when(cbtBasicsTagRelationMapper.deleteByMonitoringId(anyLong())).thenReturn(1);
    when(cbtBasicsMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);

    // テスト実行
    String result = cbtBasicsRegistService.processDelete(1L);

    // 検証
    assertEquals(MemoListConst.REDIRECT_MEMOS, result);
  }

  @Test
  void testProcessDelete_Unauthorized() {
    // モックの設定
    when(cbtBasicsMapper.selectByPrimaryKey(anyLong())).thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(any(CbtBasics.class))).thenReturn(false);

    // テスト実行
    String result = cbtBasicsRegistService.processDelete(1L);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
  }

  @Test
  @Disabled
  void testSave_WithDatabaseException() {
    // モックの設定
    when(cbtBasicsMapper.insert(any(CbtBasics.class)))
        .thenThrow(new RuntimeException("Database error"));

    // テスト実行と検証
    assertThrows(
        DatabaseException.class,
        () -> {
          cbtBasicsRegistService.save(form.getCbtBasics(), form, user.getId());
        });
  }

  @Test
  @Disabled
  void testUpdate_WithDatabaseException() {
    // モックの設定
    when(cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(anyLong()))
        .thenThrow(new RuntimeException("Database error"));

    // テスト実行と検証
    assertThrows(
        DatabaseException.class,
        () -> {
          cbtBasicsRegistService.update(
              cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds(), form.getTagNames());
        });
  }

  @Test
  @Disabled
  void testProcessDelete_WithDatabaseException() {
    // モックの設定
    when(cbtBasicsMapper.selectByPrimaryKey(anyLong())).thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(any(CbtBasics.class))).thenReturn(true);
    when(cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(anyLong()))
        .thenThrow(new RuntimeException("Database error"));

    // テスト実行と検証
    assertThrows(
        DatabaseException.class,
        () -> {
          cbtBasicsRegistService.processDelete(1L);
        });
  }
}
