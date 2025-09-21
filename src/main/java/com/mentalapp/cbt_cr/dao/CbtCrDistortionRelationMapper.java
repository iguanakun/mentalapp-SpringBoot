package com.mentalapp.cbt_cr.dao;

import com.mentalapp.cbt_cr.entity.CbtCrDistortionRelation;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/** 認知再構成法と思考の歪みの中間テーブルのデータアクセスインターフェース */
@Mapper
public interface CbtCrDistortionRelationMapper {

  /**
   * 認知再構成法と思考の歪みの関連を新規登録する
   *
   * @param record 認知再構成法と思考の歪みの関連エンティティ
   * @return 登録件数
   */
  int insert(CbtCrDistortionRelation record);

  /**
   * 認知再構成法IDに紐づく思考の歪みの関連を全て取得する
   *
   * @param cbtCrId 認知再構成法ID
   * @return 認知再構成法と思考の歪みの関連エンティティのリスト
   */
  List<CbtCrDistortionRelation> selectByCbtCrId(Long cbtCrId);

  /**
   * 認知再構成法IDに紐づく思考の歪みの関連を全て削除する
   *
   * @param cbtCrId 認知再構成法ID
   * @return 削除件数
   */
  int deleteByCbtCrId(Long cbtCrId);
}
