package com.mentalapp.user_memo_list.service;

import com.mentalapp.cbt_basic.data.CbtBasicsObject;
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
     * ユーザーIDに基づいてCbtBasicsObjectのリストを作成
     * @param userId ユーザーID
     * @return CbtBasicsObjectのリスト
     */
    public List<CbtBasicsObject> createCbtBasicsObjectList(Long userId) {
        return memoListMapper.createCbtBasicsObjectList(userId);
    }
}
