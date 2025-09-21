package com.mentalapp.cbt_cr.dao;

import com.mentalapp.common.util.TagRelationMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 認知再構成法とタグの関連を扱うマッパーインターフェース */
@Mapper
public interface CbtCrTagRelationMapper extends TagRelationMapper {

  /**
   * 関連を登録する
   *
   * @param cbtCrId 認知再構成法のID
   * @param tagId タグID
   * @return 登録件数
   */
  @Override
  int insert(@Param("cbtCrId") Long cbtCrId, @Param("tagId") Long tagId);

  /**
   * 認知再構成法のIDに紐づく関連をすべて削除する
   *
   * @param cbtCrId 認知再構成法のID
   * @return 削除件数
   */
  @Override
  int deleteByMonitoringId(@Param("cbtCrId") Long cbtCrId);
}
