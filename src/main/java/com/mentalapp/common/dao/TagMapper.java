package com.mentalapp.common.dao;

import com.mentalapp.common.entity.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/** タグのデータアクセスインターフェース */
@Mapper
public interface TagMapper {

  /**
   * タグを主キーで検索する
   *
   * @param id タグID
   * @return タグエンティティ
   */
  Tag selectByPrimaryKey(Long id);

  /**
   * タグ名でタグを検索する
   *
   * @param tagName タグ名
   * @return タグエンティティ
   */
  Tag findByTagName(String tagName);

  Tag findByTagNameAndUserId(String tagName, Long userId);

  /**
   * ユーザーIDに紐づくタグを全て取得する
   *
   * @param userId ユーザーID
   * @return タグエンティティのリスト
   */
  List<Tag> findByUserId(Long userId);

  /**
   * 全てのタグを取得する
   *
   * @return タグエンティティのリスト
   */
  List<Tag> findAll();

  /**
   * タグを新規登録する
   *
   * @param record タグエンティティ
   * @return 登録件数
   */
  int insert(Tag record);

  /**
   * タグを更新する
   *
   * @param record タグエンティティ
   * @return 更新件数
   */
  int updateByPrimaryKey(Tag record);

  /**
   * タグを削除する
   *
   * @param id タグID
   * @return 削除件数
   */
  int deleteByPrimaryKey(Long id);

  /**
   * モニタリングIDに紐づくタグを取得する
   *
   * @param monitoringId モニタリングID
   * @param tableName 中間テーブル名
   * @param columnName モニタリングIDのカラム名
   * @return タグエンティティのリスト
   */
  List<Tag> findByMonitoringId(Long monitoringId, String tableName, String columnName);
}
