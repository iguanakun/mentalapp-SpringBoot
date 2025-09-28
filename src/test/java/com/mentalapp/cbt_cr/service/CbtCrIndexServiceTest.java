package com.mentalapp.cbt_cr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.TestUtils;
import com.mentalapp.common.dao.DistortionListMapper;
import com.mentalapp.common.util.MentalCommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

/** 認知再構成法の表示処理サービスのテストクラス */
@ExtendWith(MockitoExtension.class)
public class CbtCrIndexServiceTest {

  @Mock private CbtCrMapper cbtCrMapper;

  @Mock private DistortionListMapper distortionListMapper;

  @Mock private CbtCrCommonUtils cbtCrCommonUtils;

  @Mock private MentalCommonUtils mentalCommonUtils;

  @Mock private Model model;

  @InjectMocks private CbtCrIndexService cbtCrIndexService;

  private CbtCr cbtCr;
  MockHttpSession mockSession;

  @BeforeEach
  public void setup() {
    // テスト用データの準備
    cbtCr = TestUtils.createCbtCr();

    // モックセッションの作成
    mockSession = new MockHttpSession();
    ReflectionTestUtils.setField(cbtCrIndexService, "session", mockSession);
  }

  /** 新規作成画面の処理のテスト */
  @Test
  public void testProcessNew() {
    // 戻り値の設定
    CbtCrViewData expected = new CbtCrViewData();
    // モックの設定
    when(cbtCrCommonUtils.createAllFeelsViewData()).thenReturn(expected);

    // 実行
    String result = cbtCrIndexService.processNew(model);

    // 検証
    assertEquals(CbtCrConst.NEW_PATH, result);
  }

  /** 新規作成画面の処理のテスト、復元あり */
  @Test
  public void testProcessNew_Recovery() {
    // モックの設定
    when(cbtCrCommonUtils.createAllFeelsViewData()).thenReturn(new CbtCrViewData());
    // セッションの値を設定
    String testFact = "テスト状況";
    mockSession.setAttribute("fact", testFact);

    // 実行
    String result = cbtCrIndexService.processNew(model);

    // 検証
    assertEquals(CbtCrConst.NEW_PATH, result);

    // model からフォームを取得して、fact が入っているか確認
    ArgumentCaptor<CbtCrInputForm> formCaptor = ArgumentCaptor.forClass(CbtCrInputForm.class);
    verify(model).addAttribute(eq("cbtCrForm"), formCaptor.capture());

    CbtCrInputForm recoveryForm = formCaptor.getValue();
    assertEquals(testFact, recoveryForm.getFact());
  }

  /** ステップ2画面の処理のテスト */
  @Test
  public void testNextStep2() {
    // テスト用データの準備
    CbtCrInputForm form = getCbtCrInputFormStep1();

    // 実行
    String result = cbtCrIndexService.nextStep2(form, model);

    // 検証
    assertEquals(CbtCrConst.REDIRECT_STEP2_VIEW, result);
  }

  /** ステップ2画面の処理のテスト - セッションからデータを取得 */
  @Test
  public void testProcessStep2() {
    // モックの設定
    when(distortionListMapper.findAll()).thenReturn(TestUtils.createDistortions());

    // 実行
    String result = cbtCrIndexService.processStep2(model);

    // 検証
    assertEquals(CbtCrConst.STEP2_PATH, result);
  }

  /** 詳細画面の処理のテスト - 正常系 */
  @Test
  public void testProcessShow_Success() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(true);

    // 実行
    String result = cbtCrIndexService.processShow(cbtCr.getId(), model);

    // 検証
    assertEquals(CbtCrConst.SHOW_PATH, result);
  }

  /** 詳細画面の処理のテスト - 存在しない場合 */
  @Test
  public void testProcessShow_NotFound() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(null);

    // 実行
    String result = cbtCrIndexService.processShow(cbtCr.getId(), model);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_MEMOS_PAGE, result);
  }

  /** 詳細画面の処理のテスト - アクセス権限なしの場合 */
  @Test
  public void testProcessShow_Unauthorized() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(false);

    // 実行
    String result = cbtCrIndexService.processShow(cbtCr.getId(), model);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_MEMOS_PAGE, result);
  }

  /** 編集画面の処理のテスト - 正常系 */
  @Test
  public void testProcessEdit_Success() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(true);
    when(cbtCrCommonUtils.createAllFeelsViewData()).thenReturn(new CbtCrViewData());

    // 実行
    String result = cbtCrIndexService.processEdit(cbtCr.getId(), model);

    // 検証
    assertEquals(CbtCrConst.EDIT_PATH, result);
  }

  /** 編集画面の処理のテスト - 正常系 */
  @Test
  public void testProcessEdit_Recovery() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(true);
    when(cbtCrCommonUtils.createAllFeelsViewData()).thenReturn(new CbtCrViewData());

    // セッションの値を設定
    String testFact = "テスト状況";
    mockSession.setAttribute("fact", testFact);

    // 実行
    String result = cbtCrIndexService.processEdit(cbtCr.getId(), model);

    // 検証
    assertEquals(CbtCrConst.EDIT_PATH, result);

    // model からフォームを取得して、fact が入っているか確認
    ArgumentCaptor<CbtCrInputForm> formCaptor = ArgumentCaptor.forClass(CbtCrInputForm.class);
    verify(model).addAttribute(eq("cbtCrForm"), formCaptor.capture());

    CbtCrInputForm recoveryForm = formCaptor.getValue();
    assertEquals(testFact, recoveryForm.getFact());
  }

  /** 編集画面の処理のテスト - 存在しない場合 */
  @Test
  public void testProcessEdit_NotFound() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(null);

    // 実行
    String result = cbtCrIndexService.processEdit(cbtCr.getId(), model);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_MEMOS_PAGE, result);
  }

  /** 編集画面の処理のテスト - アクセス権限なしの場合 */
  @Test
  public void testProcessEdit_Unauthorized() {
    // モックの設定
    when(cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(cbtCr.getId())).thenReturn(cbtCr);
    when(cbtCrCommonUtils.checkAccessPermission(cbtCr)).thenReturn(false);

    // 実行
    String result = cbtCrIndexService.processEdit(cbtCr.getId(), model);

    // 検証
    assertEquals(MentalCommonUtils.REDIRECT_MEMOS_PAGE, result);
  }

  /** 編集ステップ2画面の処理のテスト - 正常系 */
  @Test
  public void testProcessEditStep2_Success() {
    // テスト用データの準備
    CbtCrInputForm form = getCbtCrInputFormStep1();

    // 実行
    String result = cbtCrIndexService.processEditStep2(cbtCr.getId(), form, model);

    // 検証
    assertEquals(
        CbtCrConst.REDIRECT_PREFIX + cbtCr.getId() + CbtCrConst.REDIRECT_EDIT_STEP2_VIEW_SUFFIX,
        result);
  }

  /** 編集ステップ2画面の処理のテスト - セッションからデータを取得 */
  @Test
  public void testProcessEditStep2FromSession() {
    // モックの設定
    when(distortionListMapper.findAll()).thenReturn(TestUtils.createDistortions());

    // 実行
    String result = cbtCrIndexService.processEditStep2FromSession(cbtCr.getId(), model);

    // 検証
    assertEquals(CbtCrConst.EDIT_STEP2_PATH, result);
  }

  private static CbtCrInputForm getCbtCrInputFormStep1() {
    CbtCrInputForm form = new CbtCrInputForm();
    form.setFact("テスト状況");
    form.setMind("テスト思考");
    form.setNegativeFeelIds(TestUtils.createNegativeFeelIds());
    form.setPositiveFeelIds(TestUtils.createPositiveFeelIds());
    return form;
  }
}
