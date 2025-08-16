package com.mentalapp.common.mapper;

import com.mentalapp.common.entity.NegativeFeel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NegativeFeelMapper {
    // 全件取得
    List<NegativeFeel> selectAll();

    // １件取得
    NegativeFeel selectByPrimaryKey(Long id);
    
    // 名前による取得
    NegativeFeel selectByName(String negativeFeelName);

    // 登録
    int insert(NegativeFeel negativeFeel);

    // 更新
    int updateByPrimaryKey(NegativeFeel negativeFeel);

    // 削除
    int deleteByPrimaryKey(Long id);
}