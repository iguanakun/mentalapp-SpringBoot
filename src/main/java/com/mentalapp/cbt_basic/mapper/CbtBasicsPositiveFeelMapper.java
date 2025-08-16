package com.mentalapp.cbt_basic.mapper;

import com.mentalapp.cbt_basic.entity.CbtBasicsPositiveFeel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CbtBasicsPositiveFeelのマッパーインターフェース
 */
@Mapper
public interface CbtBasicsPositiveFeelMapper {
    
    /**
     * CbtBasicsIDによる取得
     * @param cbtBasicId CbtBasicsのID
     * @return CbtBasicsPositiveFeelのリスト
     */
    List<CbtBasicsPositiveFeel> selectByCbtBasicId(Long cbtBasicId);
    
    /**
     * 登録
     * @param cbtBasicId CbtBasicsのID
     * @param positiveFeelingId PositiveFeelのID
     * @return 影響を受けた行数
     */
    int insert(@Param("cbtBasicId") Long cbtBasicId, @Param("positiveFeelingId") Long positiveFeelingId);
    
    /**
     * CbtBasicsIDによる削除
     * @param cbtBasicId CbtBasicsのID
     * @return 影響を受けた行数
     */
    int deleteByCbtBasicId(Long cbtBasicId);
}