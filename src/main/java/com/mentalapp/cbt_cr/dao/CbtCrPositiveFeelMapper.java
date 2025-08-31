package com.mentalapp.cbt_cr.dao;

import com.mentalapp.cbt_cr.entity.CbtCrPositiveFeel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 認知再構成法とポジティブ感情の中間テーブルのデータアクセスインターフェース
 */
@Mapper
public interface CbtCrPositiveFeelMapper {

    /**
     * 認知再構成法とポジティブ感情の関連を新規登録する
     * @param record 認知再構成法とポジティブ感情の関連エンティティ
     * @return 登録件数
     */
    int insert(CbtCrPositiveFeel record);

    /**
     * 認知再構成法IDに紐づくポジティブ感情の関連を全て取得する
     * @param cbtCrId 認知再構成法ID
     * @return 認知再構成法とポジティブ感情の関連エンティティのリスト
     */
    List<CbtCrPositiveFeel> selectByCbtCrId(Long cbtCrId);

    /**
     * 認知再構成法IDに紐づくポジティブ感情の関連を全て削除する
     * @param cbtCrId 認知再構成法ID
     * @return 削除件数
     */
    int deleteByCbtCrId(Long cbtCrId);
}