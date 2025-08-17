package com.mentalapp.user_memo_list.mapper;

import com.mentalapp.cbt_basic.data.CbtBasicsObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * メモ一覧に関するデータアクセスを提供するマッパーインターフェース
 */
@Mapper
public interface MemoListMapper {
    
    /**
     * ユーザーIDに基づいてCbtBasicsObjectのリストを取得
     * @param userId ユーザーID
     * @return CbtBasicsObjectのリスト
     */
    List<CbtBasicsObject> createCbtBasicsObjectList(@Param("userId") Long userId);
}