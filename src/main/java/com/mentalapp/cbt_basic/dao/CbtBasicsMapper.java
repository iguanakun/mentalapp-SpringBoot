package com.mentalapp.cbt_basic.dao;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CbtBasicsMapper {
    // 全件取得
    List<CbtBasics> selectAll();

    // １件取得
    CbtBasics selectByPrimaryKey(Long id);
    
    // ユーザーIDによる取得
    List<CbtBasics> selectByUserId(Long userId);

    // 登録
    int insert(CbtBasics cbtBasics);

    // 更新
    int updateByPrimaryKey(CbtBasics cbtBasics);

    // 削除
    int deleteByPrimaryKey(Long id);
    
    /**
     * ユーザーIDに基づいてCbtBasicsと関連する感情を取得
     * @param userId ユーザーID
     * @return CbtBasicsのリスト（感情情報を含む）
     */
    List<CbtBasics> findCbtBasicsFeelsListByUserId(@Param("userId") Long userId);

    /**
     * CbtBasics IDに基づいてCbtBasicsと関連する感情を取得
     * @param cbtBasicId CbtBasics ID
     * @return 指定されたIDのCbtBasics（感情情報を含む）
     */
    CbtBasics selectByPrimaryKeyWithFeels(Long cbtBasicId);
    
    /**
     * ユーザーIDに基づいてCbtBasicsと関連する感情を取得（互換性のため）
     * @param userId ユーザーID
     * @return CbtBasicsのリスト（感情情報を含む）
     * @deprecated findCbtBasicsFeelsListByUserIdを使用してください
     */
    @Deprecated
    default List<CbtBasics> findCbtBasicsFeelsByUserId(@Param("userId") Long userId) {
        return findCbtBasicsFeelsListByUserId(userId);
    }
    
    /**
     * ユーザーIDに基づいて、ネガティブ感情の出現回数上位3つを取得
     * @param userId ユーザーID
     * @return ネガティブ感情名と出現回数のマップのリスト
     */
    List<Map<String, Object>> findTopNegativeFeelingsByUserId(@Param("userId") Long userId);
}