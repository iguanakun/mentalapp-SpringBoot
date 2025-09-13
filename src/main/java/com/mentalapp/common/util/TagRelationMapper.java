package com.mentalapp.common.util;

// タグ中間テーブル操作のための共通インターフェース
public interface TagRelationMapper {
  int insert(Long monitoringId, Long tagId);

  int deleteByMonitoringId(Long monitoringId);
}
