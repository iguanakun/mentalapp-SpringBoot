package com.mentalapp.cbt_cr.dao;

import com.mentalapp.cbt_cr.entity.CbtCr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 認知再構成法のデータアクセスインターフェース
 */
@Mapper
public interface CbtCrMapper {

    /**
     * 認知再構成法を主キーで検索する
     * @param id 認知再構成法ID
     * @return 認知再構成法エンティティ
     */
    CbtCr selectByPrimaryKey(Long id);

    /**
     * ユーザーIDに紐づく認知再構成法を全て取得する
     * @param userId ユーザーID
     * @return 認知再構成法エンティティのリスト
     */
    List<CbtCr> selectByUserId(Long userId);

    /**
     * 認知再構成法を新規登録する
     * @param record 認知再構成法エンティティ
     * @return 登録件数
     */
    int insert(CbtCr record);

    /**
     * 認知再構成法を更新する
     * @param record 認知再構成法エンティティ
     * @return 更新件数
     */
    int updateByPrimaryKey(CbtCr record);

    /**
     * 認知再構成法を削除する
     * @param id 認知再構成法ID
     * @return 削除件数
     */
    int deleteByPrimaryKey(Long id);
}