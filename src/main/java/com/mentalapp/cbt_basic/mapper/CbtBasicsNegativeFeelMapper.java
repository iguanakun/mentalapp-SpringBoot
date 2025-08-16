package com.mentalapp.cbt_basic.mapper;

import com.mentalapp.cbt_basic.entity.CbtBasicsNegativeFeel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CbtBasicsNegativeFeelのマッパーインターフェース
 */
@Mapper
public interface CbtBasicsNegativeFeelMapper {
    
    /**
     * CbtBasicsIDによる取得
     * @param cbtBasicId CbtBasicsのID
     * @return CbtBasicsNegativeFeelのリスト
     */
    List<CbtBasicsNegativeFeel> selectByCbtBasicId(Long cbtBasicId);
    
    /**
     * 登録
     * @param cbtBasicId CbtBasicsのID
     * @param negativeFeelingId NegativeFeelのID
     * @return 影響を受けた行数
     */
    int insert(@Param("cbtBasicId") Long cbtBasicId, @Param("negativeFeelingId") Long negativeFeelingId);
    
    /**
     * CbtBasicsIDによる削除
     * @param cbtBasicId CbtBasicsのID
     * @return 影響を受けた行数
     */
    int deleteByCbtBasicId(Long cbtBasicId);
}