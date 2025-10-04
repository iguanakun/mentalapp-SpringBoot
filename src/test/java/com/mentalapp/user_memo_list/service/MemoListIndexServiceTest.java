package com.mentalapp.user_memo_list.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.common.TestUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** メモ一覧処理サービスのテストクラス */
@ExtendWith(MockitoExtension.class)
public class MemoListIndexServiceTest {

  @Mock private CbtBasicsMapper cbtBasicsMapper;

  @Mock private CbtCrMapper cbtCrMapper;

  @InjectMocks private MemoListIndexService memoListIndexService;

  private Long userId;
  private List<CbtBasics> cbtBasicsList;
  private List<CbtCr> cbtCrList;

  @BeforeEach
  public void setup() {
    userId = 1L;
    cbtBasicsList = createTestCbtBasicsList();
    cbtCrList = createTestCbtCrList();
  }

  /**
   * CbtBasicsリスト作成のテスト
   *
   * <p>ユーザーIDに基づいてCbtBasicsのリストが正しく取得できることを検証
   */
  @Test
  public void testCreateCbtBasicsObjectList() {
    // モックの設定
    when(cbtBasicsMapper.findCbtBasicsFeelsAndTagsListByUserId(userId)).thenReturn(cbtBasicsList);

    // 実行
    List<CbtBasics> result = memoListIndexService.createCbtBasicsObjectList(userId);

    // 検証
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(cbtBasicsList, result);
    verify(cbtBasicsMapper).findCbtBasicsFeelsAndTagsListByUserId(userId);
  }

  /**
   * CbtCrリスト作成のテスト
   *
   * <p>ユーザーIDに基づいてCbtCrのリストが正しく取得できることを検証
   */
  @Test
  public void testCreateCbtCrList() {
    // モックの設定
    when(cbtCrMapper.findCbtCrFeelsListByUserId(userId)).thenReturn(cbtCrList);

    // 実行
    List<CbtCr> result = memoListIndexService.createCbtCrList(userId);

    // 検証
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(cbtCrList, result);
    verify(cbtCrMapper).findCbtCrFeelsListByUserId(userId);
  }

  /**
   * トップ3のネガティブ感情取得のテスト
   *
   * <p>CbtBasicsとCbtCrの両方からネガティブ感情を集計し、上位3つが正しく取得できることを検証
   */
  @Test
  public void testFindTopNegativeFeelings() {
    // テストデータ準備: CbtBasicsからのネガティブ感情
    List<Map<String, Object>> cbtBasicsFeels = new ArrayList<>();
    Map<String, Object> feel1 = new HashMap<>();
    feel1.put("negativeFeelName", "不安");
    feel1.put("count", 5);
    cbtBasicsFeels.add(feel1);

    Map<String, Object> feel2 = new HashMap<>();
    feel2.put("negativeFeelName", "悲しみ");
    feel2.put("count", 3);
    cbtBasicsFeels.add(feel2);

    // テストデータ準備: CbtCrからのネガティブ感情
    List<Map<String, Object>> cbtCrFeels = new ArrayList<>();
    Map<String, Object> feel3 = new HashMap<>();
    feel3.put("negativeFeelName", "不安"); // 重複する感情
    feel3.put("count", 4);
    cbtCrFeels.add(feel3);

    Map<String, Object> feel4 = new HashMap<>();
    feel4.put("negativeFeelName", "怒り");
    feel4.put("count", 6);
    cbtCrFeels.add(feel4);

    Map<String, Object> feel5 = new HashMap<>();
    feel5.put("negativeFeelName", "恐怖");
    feel5.put("count", 2);
    cbtCrFeels.add(feel5);

    // モックの設定
    when(cbtBasicsMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(cbtBasicsFeels);
    when(cbtCrMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(cbtCrFeels);

    // 実行
    List<Map<String, Object>> result = memoListIndexService.findTopNegativeFeelings(userId);

    // 検証
    assertNotNull(result);
    assertEquals(3, result.size());

    // 上位3つの順序を確認（不安:9, 怒り:6, 悲しみ:3）
    assertEquals("不安", result.get(0).get("negativeFeelName"));
    assertEquals(9, result.get(0).get("count"));

    assertEquals("怒り", result.get(1).get("negativeFeelName"));
    assertEquals(6, result.get(1).get("count"));

    assertEquals("悲しみ", result.get(2).get("negativeFeelName"));
    assertEquals(3, result.get(2).get("count"));

    verify(cbtBasicsMapper).findTopNegativeFeelingsByUserId(userId);
    verify(cbtCrMapper).findTopNegativeFeelingsByUserId(userId);
  }

  /**
   * トップ3のネガティブ感情取得のテスト（3件未満の場合）
   *
   * <p>感情が3件未満の場合でも正しく動作することを検証
   */
  @Test
  public void testFindTopNegativeFeelings_LessThanThree() {
    // テストデータ準備: 2件のみ
    List<Map<String, Object>> cbtBasicsFeels = new ArrayList<>();
    Map<String, Object> feel1 = new HashMap<>();
    feel1.put("negativeFeelName", "不安");
    feel1.put("count", 5);
    cbtBasicsFeels.add(feel1);

    List<Map<String, Object>> cbtCrFeels = new ArrayList<>();
    Map<String, Object> feel2 = new HashMap<>();
    feel2.put("negativeFeelName", "悲しみ");
    feel2.put("count", 3);
    cbtCrFeels.add(feel2);

    // モックの設定
    when(cbtBasicsMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(cbtBasicsFeels);
    when(cbtCrMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(cbtCrFeels);

    // 実行
    List<Map<String, Object>> result = memoListIndexService.findTopNegativeFeelings(userId);

    // 検証
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  /**
   * トップ3のネガティブ感情取得のテスト（感情が存在しない場合）
   *
   * <p>感情データが存在しない場合でも正しく動作することを検証
   */
  @Test
  public void testFindTopNegativeFeelings_Empty() {
    // モックの設定: 空のリストを返す
    when(cbtBasicsMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(new ArrayList<>());
    when(cbtCrMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(new ArrayList<>());

    // 実行
    List<Map<String, Object>> result = memoListIndexService.findTopNegativeFeelings(userId);

    // 検証
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  /**
   * ユーザーメモリスト取得のテスト
   *
   * <p>全てのメモデータと関連データが正しく取得され、ソートされることを検証
   */
  @Test
  public void testGetUserMemoList() {
    // モックの設定
    when(cbtBasicsMapper.findCbtBasicsFeelsAndTagsListByUserId(userId)).thenReturn(cbtBasicsList);
    when(cbtCrMapper.findCbtCrFeelsListByUserId(userId)).thenReturn(cbtCrList);
    when(cbtBasicsMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(new ArrayList<>());
    when(cbtCrMapper.findTopNegativeFeelingsByUserId(userId)).thenReturn(new ArrayList<>());

    // 実行
    Map<String, Object> result = memoListIndexService.getUserMemoList(userId);

    // 検証
    assertNotNull(result);
    assertTrue(result.containsKey("cbtBasics"));
    assertTrue(result.containsKey("cbtCrs"));
    assertTrue(result.containsKey("allMemos"));
    assertTrue(result.containsKey("negativeFeels"));

    @SuppressWarnings("unchecked")
    List<CbtBasics> resultCbtBasics = (List<CbtBasics>) result.get("cbtBasics");
    assertEquals(2, resultCbtBasics.size());

    @SuppressWarnings("unchecked")
    List<CbtCr> resultCbtCrs = (List<CbtCr>) result.get("cbtCrs");
    assertEquals(2, resultCbtCrs.size());

    @SuppressWarnings("unchecked")
    List<Object> allMemos = (List<Object>) result.get("allMemos");
    assertEquals(4, allMemos.size());

    // allMemosのソート順を検証（更新日時の降順）
    LocalDateTime previousTime = LocalDateTime.MAX;
    for (Object memo : allMemos) {
      LocalDateTime currentTime;
      if (memo instanceof CbtBasics) {
        currentTime = ((CbtBasics) memo).getUpdatedAt();
      } else if (memo instanceof CbtCr) {
        currentTime = ((CbtCr) memo).getUpdatedAt();
      } else {
        throw new IllegalStateException("予期しない型のオブジェクト");
      }
      assertTrue(
          currentTime.isBefore(previousTime) || currentTime.isEqual(previousTime),
          "メモが更新日時の降順でソートされていません");
      previousTime = currentTime;
    }

    verify(cbtBasicsMapper).findCbtBasicsFeelsAndTagsListByUserId(userId);
    verify(cbtCrMapper).findCbtCrFeelsListByUserId(userId);
    verify(cbtBasicsMapper).findTopNegativeFeelingsByUserId(userId);
    verify(cbtCrMapper).findTopNegativeFeelingsByUserId(userId);
  }

  /**
   * テスト用のCbtBasicsリストを作成
   *
   * @return CbtBasicsのリスト
   */
  private List<CbtBasics> createTestCbtBasicsList() {
    List<CbtBasics> list = new ArrayList<>();

    CbtBasics basics1 = TestUtils.createCbtBasics();
    basics1.setId(1L);
    basics1.setUpdatedAt(LocalDateTime.now().minusDays(1));
    list.add(basics1);

    CbtBasics basics2 = TestUtils.createCbtBasics();
    basics2.setId(2L);
    basics2.setUpdatedAt(LocalDateTime.now().minusDays(3));
    list.add(basics2);

    return list;
  }

  /**
   * テスト用のCbtCrリストを作成
   *
   * @return CbtCrのリスト
   */
  private List<CbtCr> createTestCbtCrList() {
    List<CbtCr> list = new ArrayList<>();

    CbtCr cr1 = TestUtils.createCbtCr();
    cr1.setId(1L);
    cr1.setUpdatedAt(LocalDateTime.now());
    list.add(cr1);

    CbtCr cr2 = TestUtils.createCbtCr();
    cr2.setId(2L);
    cr2.setUpdatedAt(LocalDateTime.now().minusDays(2));
    list.add(cr2);

    return list;
  }
}
