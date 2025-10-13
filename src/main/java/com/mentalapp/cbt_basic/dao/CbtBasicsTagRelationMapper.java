package com.mentalapp.cbt_basic.dao;

import com.mentalapp.common.util.TagRelationMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** CBT BasicsとTagの関連を扱うマッパーインターフェース */
@Mapper
public interface CbtBasicsTagRelationMapper extends TagRelationMapper {

  /**
   * 関連を登録する
   *
   * @param cbtBasicId CBT BasicsのID
   * @param tagId タグID
   * @return 登録件数
   */
  @Override
  int insert(@Param("cbtBasicId") Long cbtBasicId, @Param("tagId") Long tagId);

  /**
   * CBT BasicsのIDに紐づく関連をすべて削除する
   *
   * @param cbtBasicId CBT BasicsのID
   * @return 削除件数
   */
  int deleteByCbtBasicId(@Param("cbtBasicId") Long cbtBasicId);
}
