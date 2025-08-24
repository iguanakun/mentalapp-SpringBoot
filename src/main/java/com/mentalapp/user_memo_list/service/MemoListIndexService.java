package com.mentalapp.user_memo_list.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.user_memo_list.mapper.MemoListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * メモ一覧に関するサービスクラス
 */
@Service
public class MemoListIndexService {

    private final MemoListMapper memoListMapper;

    @Autowired
    public MemoListIndexService(MemoListMapper memoListMapper) {
        this.memoListMapper = memoListMapper;
    }

    /**
     * ユーザーIDに基づいてCbtBasicsのリストを作成
     * @param userId ユーザーID
     * @return CbtBasicsのリスト（感情情報を含む）
     */
    public List<CbtBasics> createCbtBasicsObjectList(Long userId) {
        // ユーザIDに紐づくCbtBasicsと感情情報を一度に取得
        List<CbtBasics> cbtBasicsList = memoListMapper.findCbtBasicsFeelsByUserId(userId);
        // 更新日時の降順にソート
        cbtBasicsList.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
        
        return cbtBasicsList;
    }
}
