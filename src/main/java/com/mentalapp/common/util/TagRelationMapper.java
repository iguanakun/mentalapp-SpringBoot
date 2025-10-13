package com.mentalapp.common.util;

/** タグ中間テーブル操作のための共通インターフェース */
public interface TagRelationMapper {
  /**
   * タグと関連エンティティの関連を中間テーブルに登録する
   *
   * @param monitoringId モニタリングID
   * @param tagId タグID
   * @return 登録された行数
   */
  int insert(Long monitoringId, Long tagId);
}
