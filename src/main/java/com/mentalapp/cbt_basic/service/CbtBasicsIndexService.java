package com.mentalapp.cbt_basic.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.form.CbtBasicsForm;
import com.mentalapp.cbt_basic.mapper.CbtBasicsMapper;
import com.mentalapp.cbt_basic.mapper.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import com.mentalapp.common.mapper.NegativeFeelMapper;
import com.mentalapp.common.mapper.PositiveFeelMapper;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import com.mentalapp.user_memo_list.data.MemoListConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final MentalCommonUtils mentalCommonUtils;

    @Autowired
    public CbtBasicsIndexService(CbtBasicsMapper cbtBasicsMapper,
                                 CbtBasicsNegativeFeelMapper cbtBasicsNegativeFeelMapper,
                                 NegativeFeelMapper negativeFeelMapper, 
                                 PositiveFeelMapper positiveFeelMapper, 
                                 CbtBasicsViewData cbtBasicsViewData,
                                 MentalCommonUtils mentalCommonUtils) {
        this.cbtBasicsMapper = cbtBasicsMapper;
        this.cbtBasicsNegativeFeelMapper = cbtBasicsNegativeFeelMapper;
        this.negativeFeelMapper = negativeFeelMapper;
        this.positiveFeelMapper = positiveFeelMapper;
        this.cbtBasicsViewData = cbtBasicsViewData;
        this.mentalCommonUtils = mentalCommonUtils;
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
     * ユーザーのCBT Basicsデータを取得
     * @param userId ユーザーID
     * @return CbtBasicsデータを含むマップ
     */
    public Map<String, Object> getUserCbtBasicsData(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        // ユーザーのCBT Basicsを取得
        List<CbtBasics> cbtBasicsList = findByUserId(userId);
        result.put("cbtBasics", cbtBasicsList);
        
        return result;
    }
    
    /**
     * 新規作成フォーム表示のためのモデル準備
     * @param model モデル
     */
    public void setViewData(Model model) {
        // ビューデータを作成
        CbtBasicsViewData viewData = createViewData();

        // フォームにセット
        model.addAttribute("viewData", viewData);
        model.addAttribute("cbtBasicsForm", new CbtBasicsForm());
    }

    /**
     * 新規作成処理
     * @param form フォームデータ
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @param cbtBasicsRegistService 登録サービス
     * @return 遷移先のパス
     */
    public String processCreate(CbtBasicsForm form, BindingResult bindingResult, 
                               Model model, CbtBasicsRegistService cbtBasicsRegistService) {
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            // ビューデータを作成して追加
            CbtBasicsViewData viewData = createViewData();
            model.addAttribute("viewData", viewData);
            return CbtBasicsConst.NEW_PATH;
        }
        
        // フォームからエンティティへの変換
        CbtBasics cbtBasics = convertToEntity(form);
        
        // ログインユーザーの取得
        cbtBasics.setUserId(mentalCommonUtils.getUser().getId());
        
        // 保存（ネガティブ感情とポジティブ感情の関連付けも行う）
        cbtBasicsRegistService.save(cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds());
        
        return MemoListConst.REDIRECT_MEMOS;
    }

    /**
     * 詳細表示のためのデータ取得と権限チェック
     * @param id CBT BasicsのID
     * @param model モデル
     * @return 遷移先のパス、またはnull（正常時）
     */
    public String prepareShowDetail(Long id, Model model) {
        CbtBasics cbtBasics = selectByPrimaryKeyWithFeels(id);
        
        if(Objects.isNull(cbtBasics)){
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }

        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        model.addAttribute("cbtBasic", cbtBasics);
        return null; // 正常時はnullを返す
    }

    /**
     * 編集フォーム表示のためのデータ取得と権限チェック
     * @param id CBT BasicsのID
     * @param model モデル
     * @return 遷移先のパス、またはnull（正常時）
     */
    public String prepareEditForm(Long id, Model model) {
        CbtBasics cbtBasics = selectByPrimaryKeyWithFeels(id);
        
        if(Objects.isNull(cbtBasics)){
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // ビューデータを作成
        CbtBasicsViewData viewData = createViewData();
        
        // エンティティからフォームへの変換
        CbtBasicsForm form = convertToForm(cbtBasics);

        // フォームにセット
        model.addAttribute("viewData", viewData);
        model.addAttribute("cbtBasicsForm", form);
        
        return null; // 正常時はnullを返す
    }

    /**
     * 更新処理
     * @param id CBT BasicsのID
     * @param form フォームデータ
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @param cbtBasicsRegistService 登録サービス
     * @return 遷移先のパス
     */
    public String processUpdate(Long id, CbtBasicsForm form, BindingResult bindingResult, 
                               Model model, CbtBasicsRegistService cbtBasicsRegistService) {
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            // ビューデータを作成して追加
            CbtBasicsViewData viewData = createViewData();
            model.addAttribute("viewData", viewData);
            return CbtBasicsConst.EDIT_PATH;
        }

        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(form.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // フォームからエンティティへの更新
        CbtBasics cbtBasics = convertToEntity(form);
        
        // 更新（ネガティブ感情とポジティブ感情の関連付けも行う）
        cbtBasicsRegistService.update(cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds());
        
        return MemoListConst.REDIRECT_MEMOS;
    }

    /**
     * 削除処理と権限チェック
     * @param id CBT BasicsのID
     * @param cbtBasicsRegistService 登録サービス
     * @return 遷移先のパス
     */
    public String processDelete(Long id, CbtBasicsRegistService cbtBasicsRegistService) {
        CbtBasics cbtBasics = findById(id);
        
        if(Objects.isNull(cbtBasics)){
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // 削除
        cbtBasicsRegistService.deleteById(id);
        return MemoListConst.REDIRECT_MEMOS;
    }

    /**
     * 一覧表示のためのデータ取得
     * @param model モデル
     */
    public void prepareListView(Model model) {
        // ログインユーザーの取得
        User currentUser = mentalCommonUtils.getUser();
        
        // ユーザーのCBT Basicsデータを取得
        Map<String, Object> userData = getUserCbtBasicsData(currentUser.getId());
        
        // モデルに追加
        model.addAllAttributes(userData);
    }
}