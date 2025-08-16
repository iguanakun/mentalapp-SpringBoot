package com.mentalapp.cbt_basic.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.mapper.CbtBasicsMapper;
import com.mentalapp.cbt_basic.mapper.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.mapper.CbtBasicsPositiveFeelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CBT Basicsの登録・更新・削除機能を提供するサービスクラス
 */
@Service
public class CbtBasicsRegistService {

    private final CbtBasicsMapper cbtBasicsMapper;
    private final CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper;
    private final CbtBasicsPositiveFeelMapper cbtBasicsPositiveFeelMapper;

    @Autowired
    public CbtBasicsRegistService(CbtBasicsMapper cbtBasicsMapper,
                                 CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper,
                                 CbtBasicsPositiveFeelMapper cbtBasicsPositiveFeelMapper) {
        this.cbtBasicsMapper = cbtBasicsMapper;
        this.cbtBasicsNegativeFeelMapper = cbtBasicsNegativeFeelMapper;
        this.cbtBasicsPositiveFeelMapper = cbtBasicsPositiveFeelMapper;
    }

    /**
     * 新規CBT Basicsを保存
     * @param cbtBasics 保存するCBT Basics
     * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
     * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
     * @return 保存されたCBT Basics
     */
    @Transactional
    public CbtBasics save(CbtBasics cbtBasics, List<Long> negativeFeelIds, List<Long> positiveFeelIds) {
        // 現在時刻を設定
        LocalDateTime now = LocalDateTime.now();
        cbtBasics.setCreatedAt(now);
        cbtBasics.setUpdatedAt(now);
        
        // 保存
        cbtBasicsMapper.insert(cbtBasics);
        
        // ネガティブ感情の関連付け
        if (negativeFeelIds != null && !negativeFeelIds.isEmpty()) {
            for (Long negativeFeelId : negativeFeelIds) {
                cbtBasicsNegativeFeelMapper.insert(cbtBasics.getId(), negativeFeelId);
            }
        }
        
        // ポジティブ感情の関連付け
        if (positiveFeelIds != null && !positiveFeelIds.isEmpty()) {
            for (Long positiveFeelId : positiveFeelIds) {
                cbtBasicsPositiveFeelMapper.insert(cbtBasics.getId(), positiveFeelId);
            }
        }
        
        return cbtBasics;
    }

    /**
     * 既存のCBT Basicsを更新
     * @param cbtBasics 更新するCBT Basics
     * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
     * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
     * @return 更新されたCBT Basics
     */
    @Transactional
    public CbtBasics update(CbtBasics cbtBasics, List<Long> negativeFeelIds, List<Long> positiveFeelIds) {
        // 更新時刻を設定
        cbtBasics.setUpdatedAt(LocalDateTime.now());
        
        // 更新
        cbtBasicsMapper.updateByPrimaryKey(cbtBasics);
        
        // 既存の関連を削除
        cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(cbtBasics.getId());
        cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(cbtBasics.getId());
        
        // ネガティブ感情の関連付け
        if (negativeFeelIds != null && !negativeFeelIds.isEmpty()) {
            for (Long negativeFeelId : negativeFeelIds) {
                cbtBasicsNegativeFeelMapper.insert(cbtBasics.getId(), negativeFeelId);
            }
        }
        
        // ポジティブ感情の関連付け
        if (positiveFeelIds != null && !positiveFeelIds.isEmpty()) {
            for (Long positiveFeelId : positiveFeelIds) {
                cbtBasicsPositiveFeelMapper.insert(cbtBasics.getId(), positiveFeelId);
            }
        }
        
        return cbtBasics;
    }

    /**
     * 指定されたIDのCBT Basicsを削除
     * @param id 削除するCBT BasicsのID
     */
    @Transactional
    public void deleteById(Long id) {
        // 関連するネガティブ感情とポジティブ感情の関連を削除
        cbtBasicsNegativeFeelMapper.deleteByCbtBasicId(id);
        cbtBasicsPositiveFeelMapper.deleteByCbtBasicId(id);
        
        // CBT Basicsを削除
        cbtBasicsMapper.deleteByPrimaryKey(id);
    }
}