package com.mentalapp.cbt_cr.viewdata;

import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import lombok.Data;

import java.util.List;

/**
 * 認知再構成法の画面表示用データクラス
 */
@Data
public class CbtCrViewData {
    
    /** ネガティブ感情のリスト */
    private List<NegativeFeel> negativeFeels;
    
    /** ポジティブ感情のリスト */
    private List<PositiveFeel> positiveFeels;
    
    /** 思考の歪みのリスト */
    private List<DistortionList> distortionLists;
}