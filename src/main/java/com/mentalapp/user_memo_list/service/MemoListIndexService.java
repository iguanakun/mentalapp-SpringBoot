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
   * ユーザーIDに基づいてCbtBasicsのリストを作成
   *
   * @param userId ユーザーID
   * @return CbtBasicsのリスト（感情情報を含む）
   */
  public List<CbtBasics> createCbtBasicsObjectList(Long userId) {
    // ユーザIDに紐づくCbtBasicsと感情情報を一度に取得
    return cbtBasicsMapper.findCbtBasicsFeelsListByUserId(userId);
  }

  /**
   * ユーザーIDに基づいてCbtCrのリストを作成
   *
   * @param userId ユーザーID
   * @return CbtCrのリスト（感情・思考の歪み情報を含む）
   */
  public List<CbtCr> createCbtCrList(Long userId) {
    // ユーザIDに紐づくCbtCrと感情・思考の歪み情報を一度に取得
    return cbtCrMapper.findCbtCrFeelsListByUserId(userId);
  }

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

    // 両方の結果を集計するマップ
    Map<String, Integer> combinedFeelingsMap = new HashMap<>();

    // CbtBasicsの結果を集計
    for (Map<String, Object> feel : cbtBasicsFeels) {
      String feelName = (String) feel.get("negativeFeelName");
      Integer count = ((Number) feel.get("count")).intValue();
      combinedFeelingsMap.put(feelName, count);
    }

    // CbtCrの結果を集計（同じ感情名がある場合はカウントを加算）
    for (Map<String, Object> feel : cbtCrFeels) {
      String feelName = (String) feel.get("negativeFeelName");
      Integer count = ((Number) feel.get("count")).intValue();

      // 既に存在する感情の場合はカウントを加算、そうでなければ新規追加
      combinedFeelingsMap.merge(feelName, count, Integer::sum);
    }

    // 結果をカウント降順でソートして上位3つを取得
    return combinedFeelingsMap.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(3)
        .map(
            entry -> {
              Map<String, Object> resultMap = new HashMap<>();
              resultMap.put("negativeFeelName", entry.getKey());
              resultMap.put("count", entry.getValue());
              return resultMap;
            })
        .collect(Collectors.toList());
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
    List<CbtBasics> cbtBasicsList = createCbtBasicsObjectList(userId);
    result.put("cbtBasics", cbtBasicsList);

    // ユーザーのCBT CRを取得
    List<CbtCr> cbtCrList = createCbtCrList(userId);
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
