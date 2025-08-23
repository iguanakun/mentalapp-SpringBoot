package com.mentalapp.user_memo_list.mapper;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * メモ一覧に関するデータアクセスを提供するマッパーインターフェース
 */
@Mapper
public interface MemoListMapper {
    
    /**
     * ユーザーIDに基づいてCbtBasicsと関連する感情を取得
     * @param userId ユーザーID
     * @return CbtBasicsのリスト（感情情報を含む）
     */
    List<CbtBasics> findCbtBasicsFeelsByUserId(@Param("userId") Long userId);

}