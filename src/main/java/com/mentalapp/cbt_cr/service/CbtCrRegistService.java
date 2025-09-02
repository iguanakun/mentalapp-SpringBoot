package com.mentalapp.cbt_cr.service;

import com.mentalapp.cbt_cr.dao.CbtCrDistortionRelationMapper;
import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.dao.CbtCrNegativeFeelMapper;
import com.mentalapp.cbt_cr.dao.CbtCrPositiveFeelMapper;
import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.entity.CbtCrDistortionRelation;
import com.mentalapp.cbt_cr.entity.CbtCrNegativeFeel;
import com.mentalapp.cbt_cr.entity.CbtCrPositiveFeel;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.user_memo_list.data.MemoListConst;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * 認知再構成法の登録・更新・削除処理を行うサービスクラス
 */
@Service
@RequiredArgsConstructor
public class CbtCrRegistService {

    private final CbtCrMapper cbtCrMapper;
    private final CbtCrNegativeFeelMapper cbtCrNegativeFeelMapper;
    private final CbtCrPositiveFeelMapper cbtCrPositiveFeelMapper;
    private final CbtCrDistortionRelationMapper cbtCrDistortionRelationMapper;
    private final CbtCrCommonUtils cbtCrCommonUtils;
    private final MentalCommonUtils mentalCommonUtils;
    private final HttpSession session;

    /**
     * 新規登録処理
     * @param form 入力フォーム
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @return 遷移先のパス
     */
    public String processRegist(CbtCrInputForm form, BindingResult bindingResult, Model model) {
    // セッションから一時保存データを取得
    setSessionDataToForm(form);

        // バリデーションエラーチェック
        if (hasValidationError(form, bindingResult, model)) {
            return CbtCrConst.STEP2_PATH;
        }

        // ユーザーIDの設定
        form.setUserId(mentalCommonUtils.getUser().getId());

        // 登録
        CbtCr cbtCr = createCbtCr(form);
        
        return MemoListConst.REDIRECT_MEMOS;
    }

    /**
     * 更新処理
     * @param form 入力フォーム
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @param id 認知再構成法のID
     * @return 遷移先のパス
     */
    public String processUpdate(CbtCrInputForm form, BindingResult bindingResult, Model model, Long id) {
    // セッションから一時保存データを取得
    setSessionDataToForm(form);

        // バリデーションエラーチェック
        if (hasValidationError(form, bindingResult, model)) {
            return CbtCrConst.EDIT_STEP2_PATH;
        }

        // 更新対象の取得
        CbtCr cbtCr = cbtCrMapper.selectByPrimaryKey(id);
        
        // 存在チェック
        if (Objects.isNull(cbtCr)) {
            return "redirect:/memos";
        }
        
        // アクセス権チェック
        if (!cbtCrCommonUtils.checkAccessPermission(cbtCr)) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }

        // フォームからエンティティへの変換
        updateCbtCrFromForm(cbtCr, form);

        // 更新
        update(cbtCr, form.getNegativeFeelIds(), form.getPositiveFeelIds(), form.getDistortionIds());

        return MemoListConst.REDIRECT_MEMOS;
    }

    /**
     * バリデーションエラーチェック
     * @param form 入力フォーム
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @return エラーがある場合はtrue
     */
    private Boolean hasValidationError(CbtCrInputForm form, BindingResult bindingResult, Model model) {
        // バリデーションエラーがある場合
        if (cbtCrCommonUtils.checkValidationError(bindingResult)
                || !form.hasAnyContent()) {
            // ビューデータを作成して追加
            CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsAndDistortionsViewData();
            model.addAttribute("viewData", viewData);
            return true;
        }
        return false;
    }

    /**
     * セッションから一時保存データを取得してフォームに設定
     * @param form 入力フォーム
     */
    private void setSessionDataToForm(CbtCrInputForm form) {
        // セッションから一時保存データを取得
        List<Long> negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
        List<Long> positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");
        String fact = (String) session.getAttribute("fact");
        String mind = (String) session.getAttribute("mind");
        
        // セッションデータをフォームに設定
        form.setNegativeFeelIds(negativeFeelIds);
        form.setPositiveFeelIds(positiveFeelIds);
        form.setFact(fact);
        form.setMind(mind);
        
        // セッションから削除
        session.removeAttribute("negativeFeelIds");
        session.removeAttribute("positiveFeelIds");
        session.removeAttribute("fact");
        session.removeAttribute("mind");
    }

    /**
     * フォームからエンティティへの変換
     * @param cbtCr 更新対象のエンティティ
     * @param form 入力フォーム
     */
    private void updateCbtCrFromForm(CbtCr cbtCr, CbtCrInputForm form) {
        cbtCr.setFact(form.getFact());
        cbtCr.setMind(form.getMind());
        cbtCr.setWhyCorrect(form.getWhyCorrect());
        cbtCr.setWhyDoubt(form.getWhyDoubt());
        cbtCr.setNewThought(form.getNewThought());
        cbtCr.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * 新規登録
     * @param form 入力フォーム
     * @return 登録されたCbtCr
     */
    @Transactional
    public CbtCr createCbtCr(CbtCrInputForm form) {
        // エンティティの作成
        CbtCr cbtCr = new CbtCr();
        cbtCr.setFact(form.getFact());
        cbtCr.setMind(form.getMind());
        cbtCr.setWhyCorrect(form.getWhyCorrect());
        cbtCr.setWhyDoubt(form.getWhyDoubt());
        cbtCr.setNewThought(form.getNewThought());
        cbtCr.setUserId(form.getUserId());
        cbtCr.setCreatedAt(LocalDateTime.now());
        cbtCr.setUpdatedAt(LocalDateTime.now());
        
        // 登録
        cbtCrMapper.insert(cbtCr);
        
        // ネガティブ感情の関連付け
        insertNegativeFeelJoinTable(cbtCr, form.getNegativeFeelIds());
        
        // ポジティブ感情の関連付け
        insertPositiveFeelsJoinTable(cbtCr, form.getPositiveFeelIds());
        
        // 思考の歪みの関連付け
        insertDistortionRelationsJoinTable(cbtCr, form.getDistortionIds());
        
        
        return cbtCr;
    }

    /**
     * ネガティブ感情の関連付け
     * @param cbtCr 認知再構成法
     * @param negativeFeelIds ネガティブ感情のIDリスト
     */
    private void insertNegativeFeelJoinTable(CbtCr cbtCr, List<Long> negativeFeelIds) {
        if (Objects.nonNull(negativeFeelIds) && !negativeFeelIds.isEmpty()) {
            for (Long negativeFeelId : negativeFeelIds) {
                CbtCrNegativeFeel cbtCrNegativeFeel = new CbtCrNegativeFeel();
                cbtCrNegativeFeel.setCbtCrId(cbtCr.getId());
        cbtCrNegativeFeel.setNegativeFeelId(negativeFeelId.intValue());
                cbtCrNegativeFeelMapper.insert(cbtCrNegativeFeel);
            }
        }
    }

    /**
     * ポジティブ感情の関連付け
     * @param cbtCr 認知再構成法
     * @param positiveFeelIds ポジティブ感情のIDリスト
     */
    private void insertPositiveFeelsJoinTable(CbtCr cbtCr, List<Long> positiveFeelIds) {
        if (Objects.nonNull(positiveFeelIds) && !positiveFeelIds.isEmpty()) {
            for (Long positiveFeelId : positiveFeelIds) {
                CbtCrPositiveFeel cbtCrPositiveFeel = new CbtCrPositiveFeel();
                cbtCrPositiveFeel.setCbtCrId(cbtCr.getId());
        cbtCrPositiveFeel.setPositiveFeelId(positiveFeelId.intValue());
                cbtCrPositiveFeelMapper.insert(cbtCrPositiveFeel);
            }
        }
    }

    /**
     * 思考の歪みの関連付け
     * @param cbtCr 認知再構成法
     * @param distortionIds 思考の歪みのIDリスト
     */
    private void insertDistortionRelationsJoinTable(CbtCr cbtCr, List<Long> distortionIds) {
        if (Objects.nonNull(distortionIds) && !distortionIds.isEmpty()) {
            for (Long distortionId : distortionIds) {
                CbtCrDistortionRelation cbtCrDistortionRelation = new CbtCrDistortionRelation();
                cbtCrDistortionRelation.setCbtCrId(cbtCr.getId());
                cbtCrDistortionRelation.setDistortionListId(distortionId);
                cbtCrDistortionRelationMapper.insert(cbtCrDistortionRelation);
            }
        }
    }

    /**
     * 既存のCbtCrを更新
     * @param cbtCr 更新するCbtCr
     * @param negativeFeelIds 関連付けるネガティブ感情のIDリスト
     * @param positiveFeelIds 関連付けるポジティブ感情のIDリスト
     * @param distortionIds 関連付ける思考の歪みのIDリスト
     * @return 更新されたCbtCr
     */
    @Transactional
    public CbtCr update(CbtCr cbtCr, List<Long> negativeFeelIds, List<Long> positiveFeelIds, List<Long> distortionIds) {
        // 既存の感情テーブルと思考の歪みテーブルの関連を削除
        cbtCrNegativeFeelMapper.deleteByCbtCrId(cbtCr.getId());
        cbtCrPositiveFeelMapper.deleteByCbtCrId(cbtCr.getId());
        cbtCrDistortionRelationMapper.deleteByCbtCrId(cbtCr.getId());

        // 更新
        cbtCrMapper.updateByPrimaryKey(cbtCr);

        // ネガティブ感情の関連付け
        insertNegativeFeelJoinTable(cbtCr, negativeFeelIds);
        
        // ポジティブ感情の関連付け
        insertPositiveFeelsJoinTable(cbtCr, positiveFeelIds);
        
        // 思考の歪みの関連付け
        insertDistortionRelationsJoinTable(cbtCr, distortionIds);
        
        return cbtCr;
    }

    /**
     * 削除処理と権限チェック
     * @param id 認知再構成法のID
     * @return 遷移先のパス
     */
    public String processDelete(Long id) {
        // 削除対象の取得
        CbtCr cbtCr = cbtCrMapper.selectByPrimaryKey(id);
        
        // 存在チェック
        if (Objects.isNull(cbtCr)) {
            return "redirect:/memos";
        }
        
        // アクセス権チェック
        if (!cbtCrCommonUtils.checkAccessPermission(cbtCr)) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }

        // 削除
        deleteCbtCr(id);
        return MemoListConst.REDIRECT_MEMOS;
    }

    /**
     * 指定されたIDの認知再構成法を削除
     * @param id 削除する認知再構成法のID
     */
    @Transactional
    public void deleteCbtCr(Long id) {
        // 関連するネガティブ感情、ポジティブ感情、思考の歪みの関連を削除
        cbtCrNegativeFeelMapper.deleteByCbtCrId(id);
        cbtCrPositiveFeelMapper.deleteByCbtCrId(id);
        cbtCrDistortionRelationMapper.deleteByCbtCrId(id);
        
        // 認知再構成法を削除
        cbtCrMapper.deleteByPrimaryKey(id);
    }
}