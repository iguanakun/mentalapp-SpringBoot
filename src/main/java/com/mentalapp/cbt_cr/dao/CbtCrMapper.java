package com.mentalapp.cbt_cr.dao;

import com.mentalapp.cbt_cr.entity.CbtCr;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 認知再構成法のデータアクセスインターフェース */
@Mapper
public interface CbtCrMapper {

  /**
   * 認知再構成法を主キーで検索する
   *
   * @param id 認知再構成法ID
   * @return 認知再構成法エンティティ
   */
  CbtCr selectByPrimaryKey(Long id);

  /**
   * ユーザーIDに紐づく認知再構成法を全て取得する
   *
   * @param userId ユーザーID
   * @return 認知再構成法エンティティのリスト
   */
  List<CbtCr> selectByUserId(Long userId);

  /**
   * 認知再構成法を新規登録する
   *
   * @param record 認知再構成法エンティティ
   * @return 登録件数
   */
  int insert(CbtCr record);

  /**
   * 認知再構成法を更新する
   *
   * @param record 認知再構成法エンティティ
   * @return 更新件数
   */
  int updateByPrimaryKey(CbtCr record);

  /**
   * 認知再構成法を削除する
   *
   * @param id 認知再構成法ID
   * @return 削除件数
   */
  int deleteByPrimaryKey(Long id);

  /**
   * ユーザーIDに基づいて認知再構成法と関連する感情・思考の歪みを取得
   *
   * @param userId ユーザーID
   * @return 認知再構成法のリスト（感情・思考の歪み情報を含む）
   */
  List<CbtCr> findCbtCrFeelsListByUserId(@Param("userId") Long userId);

  /**
   * 認知再構成法IDに基づいて認知再構成法と関連する感情・思考の歪みを取得
   *
   * @param cbtCrId 認知再構成法ID
   * @return 指定されたIDの認知再構成法（感情・思考の歪み情報を含む）
   */
  CbtCr selectByPrimaryKeyWithFeels(Long cbtCrId);

  /**
   * ユーザーIDに基づいて、ネガティブ感情の出現回数上位3つを取得
   *
   * @param userId ユーザーID
   * @return ネガティブ感情名と出現回数のマップのリスト
   */
  List<Map<String, Object>> findTopNegativeFeelingsByUserId(@Param("userId") Long userId);

  /**
   * 認知再構成法IDに基づいて認知再構成法と関連する感情・思考の歪み・タグを取得
   *
   * @param cbtCrId 認知再構成法ID
   * @return 指定されたIDの認知再構成法（感情・思考の歪み・タグ情報を含む）
   */
  CbtCr selectByPrimaryKeyWithFeelsAndTags(Long cbtCrId);
}
