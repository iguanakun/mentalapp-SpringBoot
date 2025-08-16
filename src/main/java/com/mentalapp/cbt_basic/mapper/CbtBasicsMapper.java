package com.mentalapp.cbt_basic.mapper;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CbtBasicsMapper {
    // 全件取得
    List<CbtBasics> selectAll();

    // １件取得
    CbtBasics selectByPrimaryKey(Long id);
    
    // ユーザーIDによる取得
    List<CbtBasics> selectByUserId(Long userId);

    // 登録
    int insert(CbtBasics cbtBasics);

    // 更新
    int updateByPrimaryKey(CbtBasics cbtBasics);

    // 削除
    int deleteByPrimaryKey(Long id);
}