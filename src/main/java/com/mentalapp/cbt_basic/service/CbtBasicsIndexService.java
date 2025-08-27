package com.mentalapp.cbt_basic.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsForm;
import com.mentalapp.cbt_basic.mapper.CbtBasicsMapper;
import com.mentalapp.cbt_basic.mapper.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.mapper.NegativeFeelMapper;
import com.mentalapp.common.mapper.PositiveFeelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CBT Basicsの表示・検索機能を提供するサービスクラス
 */
@Service
public class CbtBasicsIndexService {

    private final CbtBasicsMapper cbtBasicsMapper;
    private final CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper;
    private final NegativeFeelMapper negativeFeelMapper;
    private final PositiveFeelMapper positiveFeelMapper;
    private final CbtBasicsViewData cbtBasicsViewData;

    @Autowired
    public CbtBasicsIndexService(CbtBasicsMapper cbtBasicsMapper,
                                 CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper,
                                 NegativeFeelMapper negativeFeelMapper, PositiveFeelMapper positiveFeelMapper, CbtBasicsViewData cbtBasicsViewData) {
        this.cbtBasicsMapper = cbtBasicsMapper;
        this.cbtBasicsNegativeFeelMapper = cbtBasicsNegativeFeelMapper;
        this.negativeFeelMapper = negativeFeelMapper;
        this.positiveFeelMapper = positiveFeelMapper;
        this.cbtBasicsViewData = cbtBasicsViewData;
    }

    public CbtBasicsViewData createViewData(){
        // 感情データを取得
        List<NegativeFeel> negativeFeels = negativeFeelMapper.selectAll();
        List<PositiveFeel> positiveFeels = positiveFeelMapper.selectAll();

        // ビューデータにセット
        cbtBasicsViewData.setNegativeFeels(negativeFeels);
        cbtBasicsViewData.setPositiveFeels(positiveFeels);
        
        return cbtBasicsViewData;
    }

    /**
     * 全件取得
     * @return CBT Basicsの全リスト
     */
    public List<CbtBasics> findAll() {
        return cbtBasicsMapper.selectAll();
    }

    /**
     * IDによる取得
     * @param id 取得するCBT BasicsのID
     * @return 指定されたIDのCBT Basics
     */
    public CbtBasics findById(Long id) {
        return cbtBasicsMapper.selectByPrimaryKey(id);
    }

    /**
     * ユーザーIDによる取得
     * @param userId 取得するユーザーのID
     * @return 指定されたユーザーIDに関連するCBT Basicsのリスト
     */
    public List<CbtBasics> findByUserId(Long userId) {
        return cbtBasicsMapper.selectByUserId(userId);
    }

    public CbtBasics selectByPrimaryKeyWithFeels(Long cbtBasicId){
        return cbtBasicsMapper.selectByPrimaryKeyWithFeels(cbtBasicId);
    }

    /**
     * ユーザーIDに関連するネガティブ感情の上位3つを取得
     * @param userId ユーザーID
     * @return ネガティブ感情の名前と出現回数のマップ
     */
    public List<Map<String, Object>> findTopNegativeFeelings(Long userId) {
        // ユーザーのCBT Basicsを取得
        List<CbtBasics> cbtBasicsList = findByUserId(userId);
        
        // CBT BasicsのIDリストを取得
        List<Long> cbtBasicsIds = cbtBasicsList.stream()
                .map(CbtBasics::getId)
                .collect(Collectors.toList());
        
        // TODO: ネガティブ感情の出現回数をカウントし、上位3つを返す
        // 実際の実装はデータベースの構造に依存するため、ここではダミーデータを返す
        return List.of(
            Map.of("negativeFeelName", "不安", "count", 5),
            Map.of("negativeFeelName", "怒り", "count", 3),
            Map.of("negativeFeelName", "悲しみ", "count", 2)
        );
    }
    
    /**
     * エンティティからフォームへの変換
     * @param cbtBasics 変換元のCBT Basicsエンティティ
     * @return 変換後のフォーム
     */
    public CbtBasicsForm convertToForm(CbtBasics cbtBasics) {
        CbtBasicsForm form = new CbtBasicsForm();
        form.setId(cbtBasics.getId());
        form.setFact(cbtBasics.getFact());
        form.setMind(cbtBasics.getMind());
        form.setBody(cbtBasics.getBody());
        form.setBehavior(cbtBasics.getBehavior());
        form.setUserId(cbtBasics.getUserId());
        
        // ネガティブ感情とポジティブ感情のIDを設定
        List<NegativeFeel> negativeFeels = cbtBasics.getNegativeFeels();
        if (negativeFeels != null && !negativeFeels.isEmpty()) {
            List<Long> negativeFeelIds = negativeFeels.stream()
                    .map(NegativeFeel::getId)
                    .toList();
            form.setNegativeFeelIds(negativeFeelIds);
        }

        List<PositiveFeel> positiveFeels = cbtBasics.getPositiveFeels();
        if (positiveFeels != null && !positiveFeels.isEmpty()) {
            List<Long> positiveFeelIds = positiveFeels.stream()
                    .map(PositiveFeel::getId)
                    .toList();
            form.setPositiveFeelIds(positiveFeelIds);
        }
        
        return form;
    }

    /**
     * フォームからエンティティへの変換
     * @param form 変換元のフォーム
     * @return 変換後のCBT Basicsエンティティ
     */
    public CbtBasics convertToEntity(CbtBasicsForm form) {
        CbtBasics cbtBasics = new CbtBasics();
        cbtBasics.setId(form.getId());
        cbtBasics.setFact(form.getFact());
        cbtBasics.setMind(form.getMind());
        cbtBasics.setBody(form.getBody());
        cbtBasics.setBehavior(form.getBehavior());
        cbtBasics.setUserId(form.getUserId());
        return cbtBasics;
    }
    
    /**
     * ユーザーのCBT Basicsデータと関連データを取得
     * @param userId ユーザーID
     * @return CbtBasicsデータと関連データを含むマップ
     */
    public Map<String, Object> getUserCbtBasicsData(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        // ユーザーのCBT Basicsを取得
        List<CbtBasics> cbtBasicsList = findByUserId(userId);
        result.put("cbtBasics", cbtBasicsList);
        
        // ネガティブ感情の上位3つを取得
        List<Map<String, Object>> topNegativeFeels = findTopNegativeFeelings(userId);
        result.put("negativeFeels", topNegativeFeels);
        
        return result;
    }
}