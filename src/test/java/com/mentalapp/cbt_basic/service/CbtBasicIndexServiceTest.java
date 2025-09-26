package com.mentalapp.cbt_basic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.TestUtils;
import com.mentalapp.common.util.MentalCommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
public class CbtBasicIndexServiceTest {
  @InjectMocks private CbtBasicsIndexService cbtBasicsIndexService;

  @Mock private CbtBasicCommonUtils cbtBasicCommonUtils;

  @Mock private CbtBasicsMapper cbtBasicsMapper;

  @Mock private Model model;

  private CbtBasics cbtBasics;

  @BeforeEach
  void setup() {
    // cbtBasicsのテストデータ生成
    cbtBasics = TestUtils.createCbtBasics();
  }

  @Test
  void testProcessNew() {
    CbtBasicsViewData expected = new CbtBasicsViewData();
    when(cbtBasicCommonUtils.createAllFeelsViewData()).thenReturn(expected);

    CbtBasicsViewData actual = cbtBasicsIndexService.processNew();

    assertSame(expected, actual);
    verify(cbtBasicCommonUtils).createAllFeelsViewData();
  }

  @Test
  // アクセス権チェック成功時
  void testProcessShow() {
    when(cbtBasicsMapper.selectByPrimaryKeyWithFeelsAndTags(cbtBasics.getId()))
        .thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(cbtBasics)).thenReturn(true);

    String result = cbtBasicsIndexService.processShow(cbtBasics.getId(), model);

    assertEquals(CbtBasicsConst.SHOW_PATH, result);
  }

  @Test
  void testProcessShow_AccessDenied() {
    when(cbtBasicsMapper.selectByPrimaryKeyWithFeelsAndTags(cbtBasics.getId()))
        .thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(cbtBasics)).thenReturn(false);

    String result = cbtBasicsIndexService.processShow(cbtBasics.getId(), model);

    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
  }

  @Test
  // アクセス権チェック成功時
  void testProcessEdit() {
    when(cbtBasicCommonUtils.createAllFeelsViewData()).thenReturn(new CbtBasicsViewData());
    when(cbtBasicCommonUtils.convertToForm(cbtBasics)).thenReturn(new CbtBasicsInputForm());
    when(cbtBasicsMapper.selectByPrimaryKeyWithFeelsAndTags(cbtBasics.getId()))
        .thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(cbtBasics)).thenReturn(true);

    String result = cbtBasicsIndexService.processEdit(cbtBasics.getId(), model);

    assertEquals(CbtBasicsConst.EDIT_PATH, result);
  }

  @Test
  // アクセス権チェック成功時
  void testProcessEdit_AccessDenied() {
    when(cbtBasicsMapper.selectByPrimaryKeyWithFeelsAndTags(cbtBasics.getId()))
        .thenReturn(cbtBasics);
    when(cbtBasicCommonUtils.checkAccessPermission(cbtBasics)).thenReturn(false);

    String result = cbtBasicsIndexService.processEdit(cbtBasics.getId(), model);

    assertEquals(MentalCommonUtils.REDIRECT_TOP_PAGE, result);
  }
}
