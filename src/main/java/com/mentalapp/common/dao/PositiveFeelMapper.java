package com.mentalapp.common.dao;

import com.mentalapp.common.entity.PositiveFeel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PositiveFeelMapper {
    // 全件取得
    List<PositiveFeel> selectAll();

    // １件取得
    PositiveFeel selectByPrimaryKey(Long id);
    
    // 名前による取得
    PositiveFeel selectByName(String positiveFeelName);

    // 登録
    int insert(PositiveFeel positiveFeel);

    // 更新
    int updateByPrimaryKey(PositiveFeel positiveFeel);

    // 削除
    int deleteByPrimaryKey(Long id);
}