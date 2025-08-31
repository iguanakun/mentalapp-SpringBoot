package com.mentalapp.cbt_basic.service;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.dao.CbtBasicsMapper;
import com.mentalapp.cbt_basic.dao.CbtBasicsNegativeFeelMapper;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/**
 * CBT Basicsの表示・検索機能を提供するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class CbtBasicsIndexService {

    private final CbtBasicsMapper cbtBasicsMapper;
    private final CbtBasicCommonUtils cbtBasicCommonUtils;

    /**
     * 新規作成処理のためのビューデータを準備
     * @return 感情一覧を含むビューデータ
     */
    public CbtBasicsViewData processNew(){
        return cbtBasicCommonUtils.createAllFeelsViewData();
    }


    /**
     * ユーザーIDによる取得
     * @param userId 取得するユーザーのID
     * @return 指定されたユーザーIDに関連するCBT Basicsのリスト
     */
    public List<CbtBasics> findByUserId(Long userId) {
        return cbtBasicsMapper.selectByUserId(userId);
    }

    /**
     * 主キーによるCBT Basicsの取得（感情情報を含む）
     * @param cbtBasicId 取得するCBT BasicsのID
     * @return 感情情報を含むCBT Basicsエンティティ
     */
    public CbtBasics selectByPrimaryKeyWithFeels(Long cbtBasicId){
        return cbtBasicsMapper.selectByPrimaryKeyWithFeels(cbtBasicId);
    }

    /**
     * 詳細表示のためのデータ取得と権限チェック
     * @param id CBT BasicsのID
     * @param model モデル
     * @return 遷移先のパス、またはnull（正常時）
     */
    public String processShow(Long id, Model model) {
        // 表示するモニタリングデータを取得
        CbtBasics cbtBasics = selectByPrimaryKeyWithFeels(id);

        // アクセス権をチェック
        if (!cbtBasicCommonUtils.checkAccessPermission(cbtBasics)) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }

        model.addAttribute("cbtBasic", cbtBasics);

        return CbtBasicsConst.SHOW_PATH;
    }

    /**
     * 編集フォーム表示のためのデータ取得と権限チェック
     * @param id CBT BasicsのID
     * @param model モデル
     * @return 遷移先のパス、またはnull（正常時）
     */
    public String processEdit(Long id, Model model) {
        // 編集対象のモニタリング情報を取得
        CbtBasics cbtBasics = cbtBasicsMapper.selectByPrimaryKeyWithFeels(id);

        //　アクセス権チェック
        if (!cbtBasicCommonUtils.checkAccessPermission(cbtBasics))
            return MentalCommonUtils.REDIRECT_TOP_PAGE;

        // ビューデータを作成
        CbtBasicsViewData viewData = cbtBasicCommonUtils.createAllFeelsViewData();

        // エンティティからフォームへの変換
        CbtBasicsInputForm form = cbtBasicCommonUtils.convertToForm(cbtBasics);

        // フォームにセット
        model.addAttribute("viewData", viewData);
        model.addAttribute("cbtBasicsForm", form);

        return CbtBasicsConst.EDIT_PATH;
    }
}