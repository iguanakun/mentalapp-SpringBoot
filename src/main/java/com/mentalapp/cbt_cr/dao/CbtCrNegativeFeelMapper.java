package com.mentalapp.cbt_cr.dao;

import com.mentalapp.cbt_cr.entity.CbtCrNegativeFeel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 認知再構成法とネガティブ感情の中間テーブルのデータアクセスインターフェース
 */
@Mapper
public interface CbtCrNegativeFeelMapper {

    /**
     * 認知再構成法とネガティブ感情の関連を新規登録する
     * @param record 認知再構成法とネガティブ感情の関連エンティティ
     * @return 登録件数
     */
    int insert(CbtCrNegativeFeel record);

    /**
     * 認知再構成法IDに紐づくネガティブ感情の関連を全て取得する
     * @param cbtCrId 認知再構成法ID
     * @return 認知再構成法とネガティブ感情の関連エンティティのリスト
     */
    List<CbtCrNegativeFeel> selectByCbtCrId(Long cbtCrId);

    /**
     * 認知再構成法IDに紐づくネガティブ感情の関連を全て削除する
     * @param cbtCrId 認知再構成法ID
     * @return 削除件数
     */
    int deleteByCbtCrId(Long cbtCrId);
}