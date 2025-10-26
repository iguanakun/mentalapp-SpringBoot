package com.mentalapp.user_memo_list.service;

import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.entity.CbtCr;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** メモ一覧に関するサービスクラス */
@Service
@RequiredArgsConstructor
public class MemoListIndexService {

  private final CbtBasicsMapper cbtBasicsMapper;
  private final CbtCrMapper cbtCrMapper;

  /**
   * ユーザーIDに基づいてネガティブ感情の上位3つを取得
   *
   * @param userId ユーザーID
   * @return ネガティブ感情の名前と出現回数のマップのリスト
   */
  public List<Map<String, Object>> findTopNegativeFeelings(Long userId) {
    // CbtBasicsからネガティブ感情を取得
    List<Map<String, Object>> cbtBasicsFeels =
        cbtBasicsMapper.findTopNegativeFeelingsByUserId(userId);

    // CbtCrからネガティブ感情を取得
    List<Map<String, Object>> cbtCrFeels = cbtCrMapper.findTopNegativeFeelingsByUserId(userId);

    // 両方の結果を集計
    Map<String, Integer> combinedFeelingsMap = new HashMap<>();
    aggregateFeelingsToMap(cbtBasicsFeels, combinedFeelingsMap);
    aggregateFeelingsToMap(cbtCrFeels, combinedFeelingsMap);

    // 結果をカウント降順でソートして上位3つを取得
    return combinedFeelingsMap.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(3)
        .map(entry -> createFeelResultMap(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  /**
   * 感情リストを集計マップに追加
   *
   * @param feels 感情リスト
   * @param resultMap 集計結果を格納するマップ
   */
  private void aggregateFeelingsToMap(
      List<Map<String, Object>> feels, Map<String, Integer> resultMap) {
    for (Map<String, Object> feel : feels) {
      String feelName = (String) feel.get("negativeFeelName");
      Integer count = ((Number) feel.get("count")).intValue();
      resultMap.merge(feelName, count, Integer::sum);
    }
  }

  /**
   * 感情名とカウントからマップを作成
   *
   * @param feelName 感情名
   * @param count 出現回数
   * @return マップ
   */
  private Map<String, Object> createFeelResultMap(String feelName, Integer count) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("negativeFeelName", feelName);
    resultMap.put("count", count);
    return resultMap;
  }

  /**
   * ユーザーのメモデータと関連データを取得
   *
   * @param userId ユーザーID
   * @return メモデータと関連データを含むマップ
   */
  public Map<String, Object> getUserMemoList(Long userId) {
    Map<String, Object> result = new HashMap<>();

    // ユーザーのCBT Basicsを取得
    List<CbtBasics> cbtBasicsList = cbtBasicsMapper.findCbtBasicsFeelsAndTagsListByUserId(userId);
    result.put("cbtBasics", cbtBasicsList);

    // ユーザーのCBT CRを取得
    List<CbtCr> cbtCrList = cbtCrMapper.findCbtCrFeelsAndTagsListByUserId(userId);
    result.put("cbtCrs", cbtCrList);

    // 全てのメモを更新日時の降順でソートしたリストを作成
    List<Object> allMemos = sortAllMemosByUpdatedAt(cbtBasicsList, cbtCrList);
    result.put("allMemos", allMemos);

    // ネガティブ感情の上位3つを取得
    List<Map<String, Object>> topNegativeFeels = findTopNegativeFeelings(userId);
    result.put("negativeFeels", topNegativeFeels);

    return result;
  }

  /**
   * CbtBasicsとCbtCrを更新日時の降順でソートした統合リストを作成
   *
   * @param cbtBasicsList CbtBasicsのリスト
   * @param cbtCrList CbtCrのリスト
   * @return 更新日時の降順でソートされた統合リスト
   */
  private List<Object> sortAllMemosByUpdatedAt(
      List<CbtBasics> cbtBasicsList, List<CbtCr> cbtCrList) {
    List<Object> allMemos = new ArrayList<>();

    // CbtBasicsをリストに追加
    allMemos.addAll(cbtBasicsList);

    // CbtCrをリストに追加
    allMemos.addAll(cbtCrList);

    // 更新日時の降順でソート
    allMemos.sort(
        (a, b) -> {
          LocalDateTime timeA = getUpdatedAt(a);
          LocalDateTime timeB = getUpdatedAt(b);
          return timeB.compareTo(timeA);
        });

    return allMemos;
  }

  /**
   * オブジェクトの更新日時を取得
   *
   * @param obj CbtBasicsまたはCbtCrオブジェクト
   * @return 更新日時
   */
  private LocalDateTime getUpdatedAt(Object obj) {
    if (obj instanceof CbtBasics) {
      return ((CbtBasics) obj).getUpdatedAt();
    } else if (obj instanceof CbtCr) {
      return ((CbtCr) obj).getUpdatedAt();
    }
    return LocalDateTime.MIN; // デフォルト値（通常は到達しない）
  }
}
