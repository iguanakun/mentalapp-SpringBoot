package com.mentalapp.cbt_cr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.common.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

/** 認知再構成法のステップ2から戻る処理を行うサービスのテストクラス */
@ExtendWith(MockitoExtension.class)
public class CbtCrBackServiceTest {

  @Mock private Model model;

  @InjectMocks private CbtCrBackService cbtCrBackService;

  private CbtCr cbtCr;

  @BeforeEach
  public void setup() {
    // テスト用データの準備
    cbtCr = TestUtils.createCbtCr();
  }

  /** ステップ2からステップ1に戻る処理のテスト */
  @Test
  public void testProcessBackToStep1() {
    // 実行
    String result = cbtCrBackService.processBackToStep1(model);

    // 検証
    assertEquals(CbtCrConst.REDIRECT_NEW_PATH, result);
  }

  /** 編集時にステップ2からステップ1に戻る処理のテスト */
  @Test
  public void testProcessBackToEditStep1() {
    // テストデータの準備
    Long id = cbtCr.getId();

    // 実行
    String result = cbtCrBackService.processBackToEditStep1(id, model);

    // 検証
    assertEquals(CbtCrConst.REDIRECT_PREFIX + id + CbtCrConst.EDIT_SUFFIX, result);
  }
}
