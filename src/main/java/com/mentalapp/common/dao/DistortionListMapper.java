package com.mentalapp.common.dao;

import com.mentalapp.common.entity.DistortionList;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/** 思考の歪みリストのデータアクセスインターフェース */
@Mapper
public interface DistortionListMapper {

  /**
   * 思考の歪みリストを主キーで検索する
   *
   * @param id 思考の歪みリストID
   * @return 思考の歪みリストエンティティ
   */
  DistortionList selectByPrimaryKey(Long id);

  /**
   * 全ての思考の歪みリストを取得する
   *
   * @return 思考の歪みリストエンティティのリスト
   */
  List<DistortionList> findAll();

  /**
   * 思考の歪みリストを新規登録する
   *
   * @param record 思考の歪みリストエンティティ
   * @return 登録件数
   */
  int insert(DistortionList record);

  /**
   * 思考の歪みリストを更新する
   *
   * @param record 思考の歪みリストエンティティ
   * @return 更新件数
   */
  int updateByPrimaryKey(DistortionList record);

  /**
   * 思考の歪みリストを削除する
   *
   * @param id 思考の歪みリストID
   * @return 削除件数
   */
  int deleteByPrimaryKey(Long id);
}
