package com.mentalapp.common.util;

/** タグ関連の中間テーブル操作を定義するインターフェース */
public interface TagRelationOperator {

  /**
   * タグ関連を登録する
   *
   * @param entityId エンティティID
   * @param tagId タグID
   * @return 登録件数
   */
  int insertTagRelation(Long entityId, Long tagId);

  /**
   * エンティティIDに紐づくタグ関連をすべて削除する
   *
   * @param entityId エンティティID
   * @return 削除件数
   */
  int deleteTagRelationsByEntityId(Long entityId);
}
