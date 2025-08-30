package com.mentalapp.cbt_basic.util;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.entity.CbtBasicsNegativeFeel;
import com.mentalapp.cbt_basic.entity.CbtBasicsPositiveFeel;
import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.util.MentalCommonUtils;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;

/**
 * CBT Basicsの共通ユーティリティクラス
 */
public class CbtBasicCommonUtils {

    private static NegativeFeelMapper negativeFeelMapper;
    private static PositiveFeelMapper positiveFeelMapper;
    private static MentalCommonUtils mentalCommonUtils;

    /**
     * エンティティからフォームへの変換
     * @param cbtBasics 変換元のCBT Basicsエンティティ
     * @return 変換後のフォーム
     */
    public static CbtBasicsInputForm convertToForm(CbtBasics cbtBasics) {
        // ネガティブ感情IDリストの抽出
        List<Long> negativeFeelIds = extractedNegativeFeelsIdList(cbtBasics);

        // ポジティブ感情IDリストの抽出
        List<Long> positiveFeelIds = extractedPositiveFeelsIdList(cbtBasics);

        CbtBasicsInputForm form = new CbtBasicsInputForm();
        form.setCbtBasics(cbtBasics);
        form.setNegativeFeelIds(negativeFeelIds);
        form.setPositiveFeelIds(positiveFeelIds);

        return form;
    }

    /**
     * CBT Basicsからネガティブ感情IDリストを抽出
     * @param cbtBasics 抽出元のCBT Basicsエンティティ
     * @return ネガティブ感情IDのリスト、関連付けがない場合はnull
     */
    private static List<Long> extractedNegativeFeelsIdList(CbtBasics cbtBasics) {
        if (Objects.isNull(cbtBasics.getCbtBasicsNegativeFeels()) && cbtBasics.getCbtBasicsNegativeFeels().isEmpty()) {
            return null;
        }
        
        return cbtBasics.getCbtBasicsNegativeFeels().stream()
                .map(CbtBasicsNegativeFeel::getNegativeFeelingId)
                .toList();
    }
    
    /**
     * CBT Basicsからポジティブ感情IDリストを抽出
     * @param cbtBasics 抽出元のCBT Basicsエンティティ
     * @return ポジティブ感情IDのリスト、関連付けがない場合はnull
     */
    private static List<Long> extractedPositiveFeelsIdList(CbtBasics cbtBasics) {
        if (Objects.isNull(cbtBasics.getCbtBasicsPositiveFeels()) && cbtBasics.getCbtBasicsPositiveFeels().isEmpty()) {
            return null;
        }
        
        return cbtBasics.getCbtBasicsPositiveFeels().stream()
                .map(CbtBasicsPositiveFeel::getPositiveFeelingId)
                .toList();
    }

    /**
     * 感情一覧表示ビューデータを作成
     * @param viewData ビューデータオブジェクト（既存のものがある場合）
     * @return 作成されたビューデータ
     */
    public static CbtBasicsViewData createAllFeelsViewData(CbtBasicsViewData viewData) {
        // 感情データを取得
        List<NegativeFeel> negativeFeels = negativeFeelMapper.selectAll();
        List<PositiveFeel> positiveFeels = positiveFeelMapper.selectAll();

        // ビューデータにセット
        if (viewData == null) {
            viewData = new CbtBasicsViewData();
        }
        viewData.setNegativeFeels(negativeFeels);
        viewData.setPositiveFeels(positiveFeels);
        
        return viewData;
    }

    /**
     * 感情一覧表示ビューデータを作成（オーバーロード）
     * @return 作成されたビューデータ
     */
    public static CbtBasicsViewData createAllFeelsViewData() {
        return createAllFeelsViewData(null);
    }

    /**
     * CBT Basicsのアクセス権チェック
     * @param cbtBasics チェック対象のCBT Basics
     * @return アクセス可能な場合はtrue、それ以外はfalse
     */
    public static Boolean checkAccessPermission(CbtBasics cbtBasics) {
        // NULLチェック
        if(Objects.isNull(cbtBasics)){
            return false;
        }

        // モニタリング情報へのユーザへのアクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return false;
        }

        return true;
    }

    /**
     * バリデーションエラーチェック
     * @param bindingResult バリデーション結果
     * @return エラーがある場合はtrue、それ以外はfalse
     */
    public static Boolean checkValidationError(BindingResult bindingResult) {
        // バリデーションエラーがある場合
        return bindingResult.hasErrors();
    }
}
