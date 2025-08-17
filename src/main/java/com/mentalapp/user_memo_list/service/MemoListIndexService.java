package com.mentalapp.user_memo_list.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.mapper.CbtBasicsMapper;
import com.mentalapp.user_memo_list.mapper.MemoListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * メモ一覧に関するサービスクラス
 */
@Service
public class MemoListIndexService {

    private final MemoListMapper memoListMapper;
    private final CbtBasicsMapper cbtBasicsMapper;

    @Autowired
    public MemoListIndexService(MemoListMapper memoListMapper, CbtBasicsMapper cbtBasicsMapper) {
        this.memoListMapper = memoListMapper;
        this.cbtBasicsMapper = cbtBasicsMapper;
    }

    /**
     * ユーザーIDに基づいてCbtBasicsのリストを作成
     * @param userId ユーザーID
     * @return CbtBasicsのリスト
     */
    public List<CbtBasics> createCbtBasicsObjectList(Long userId) {
        // CbtBasicsの基本情報を取得
        List<CbtBasics> cbtBasicsList = cbtBasicsMapper.selectByUserId(userId);
        
        // 感情情報を取得
        List<CbtBasics> cbtBasicsWithFeels = memoListMapper.createCbtBasicsObjectList(userId);
        
        // 感情情報をcbtBasicsListに統合
        Map<Long, CbtBasics> feelsMap = cbtBasicsWithFeels.stream()
                .collect(Collectors.toMap(CbtBasics::getId, Function.identity(), (existing, replacement) -> existing));
        
        // 各CbtBasicsに感情情報を設定
        cbtBasicsList.forEach(cbtBasics -> Optional.ofNullable(feelsMap.get(cbtBasics.getId()))
                .ifPresent(withFeels -> {
                    cbtBasics.setNegativeFeels(withFeels.getNegativeFeels());
                    cbtBasics.setPositiveFeels(withFeels.getPositiveFeels());
                }));
        
        return cbtBasicsList;
    }
}
