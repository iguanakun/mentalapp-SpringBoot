package com.mentalapp.user_memo_list.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.mapper.CbtBasicsMapper;
import com.mentalapp.common.entity.NegativeFeel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * メモ一覧に関するサービスクラス
 */
@Service
public class MemoListIndexService {

    private final CbtBasicsMapper cbtBasicsMapper;

    @Autowired
    public MemoListIndexService(CbtBasicsMapper cbtBasicsMapper) {
        this.cbtBasicsMapper = cbtBasicsMapper;
    }

    /**
     * ユーザーIDに基づいてCbtBasicsのリストを作成
     * @param userId ユーザーID
     * @return CbtBasicsのリスト（感情情報を含む）
     */
    public List<CbtBasics> createCbtBasicsObjectList(Long userId) {
        // ユーザIDに紐づくCbtBasicsと感情情報を一度に取得
        List<CbtBasics> cbtBasicsList = cbtBasicsMapper.findCbtBasicsFeelsListByUserId(userId);
        // 更新日時の降順にソート
        cbtBasicsList.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
        
        return cbtBasicsList;
    }
    
    /**
     * ユーザーIDに基づいてネガティブ感情の上位3つを取得
     * @param userId ユーザーID
     * @return ネガティブ感情の名前と出現回数のマップのリスト
     */
    public List<Map<String, Object>> findTopNegativeFeelings(Long userId) {
        return cbtBasicsMapper.findTopNegativeFeelingsByUserId(userId);
    }
    
    /**
     * ユーザーのメモデータと関連データを取得
     * @param userId ユーザーID
     * @return メモデータと関連データを含むマップ
     */
    public Map<String, Object> getUserMemoData(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        // ユーザーのCBT Basicsを取得
        List<CbtBasics> cbtBasicsList = createCbtBasicsObjectList(userId);
        result.put("cbtBasics", cbtBasicsList);
        
        // ネガティブ感情の上位3つを取得（SQLを使用）
        List<Map<String, Object>> topNegativeFeels = findTopNegativeFeelings(userId);
        result.put("negativeFeels", topNegativeFeels);
        
        return result;
    }
}
