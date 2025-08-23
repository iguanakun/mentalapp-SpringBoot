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
        // CbtBasicsの基本情報と感情情報を一度に取得
        return memoListMapper.findCbtBasicsFeelsByUserId(userId);
    }
}
