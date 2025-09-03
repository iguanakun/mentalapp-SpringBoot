package com.mentalapp.cbt_cr.service;

import com.mentalapp.cbt_cr.dao.CbtCrMapper;
import com.mentalapp.cbt_cr.data.CbtCrConst;
import com.mentalapp.cbt_cr.entity.CbtCr;
import com.mentalapp.cbt_cr.form.CbtCrInputForm;
import com.mentalapp.cbt_cr.util.CbtCrCommonUtils;
import com.mentalapp.cbt_cr.viewdata.CbtCrViewData;
import com.mentalapp.common.dao.DistortionListMapper;
import com.mentalapp.common.dao.NegativeFeelMapper;
import com.mentalapp.common.dao.PositiveFeelMapper;
import com.mentalapp.common.entity.DistortionList;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.util.MentalCommonUtils;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/** 認知再構成法の表示処理サービス */
@Service
@RequiredArgsConstructor
public class CbtCrIndexService {

  private final CbtCrMapper cbtCrMapper;
  private final NegativeFeelMapper negativeFeelMapper;
  private final PositiveFeelMapper positiveFeelMapper;
  private final DistortionListMapper distortionListMapper;
  private final CbtCrCommonUtils cbtCrCommonUtils;
  private final MentalCommonUtils mentalCommonUtils;
  private final HttpSession session;
  
  /**
   * CbtCrオブジェクトの存在確認とアクセス権限チェックを行う
   *
   * @param id 認知再構成法のID
   * @return 権限チェック結果（成功時はCbtCrオブジェクト、失敗時はnull）
   */
  private CbtCr validateAccessPermission(Long id) {
    // 認知再構成法を取得
    CbtCr cbtCr = cbtCrMapper.selectByPrimaryKeyWithFeels(id);
    
    // 存在チェック
    if (Objects.isNull(cbtCr)) {
      return null;
    }
    
    // アクセス権限チェック
    if (!cbtCrCommonUtils.checkAccessPermission(cbtCr)) {
      return null;
    }
    
    return cbtCr;
  }

  /**
   * 新規作成画面の処理
   *
   * @param model モデル
   * @return ビュー名
   */
  public String processNew(Model model) {
    // ビューデータを作成
    CbtCrViewData viewData = createCbtCrViewDataWithFeel();

    // モデルに追加
    model.addAttribute("viewData", viewData);
    model.addAttribute("cbtCrForm", new CbtCrInputForm());

    return CbtCrConst.NEW_PATH;
  }

  private CbtCrViewData createCbtCrViewDataWithFeel() {
    // 全ての感情を取得
    List<NegativeFeel> negativeFeels = negativeFeelMapper.selectAll();
    List<PositiveFeel> positiveFeels = positiveFeelMapper.selectAll();

    // ビューデータを作成
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setNegativeFeels(negativeFeels);
    viewData.setPositiveFeels(positiveFeels);

    return viewData;
  }

  /**
   * ステップ2画面の処理
   *
   * @param form 入力フォーム
   * @param model モデル
   * @return ビュー名
   */
  public String processStep2(CbtCrInputForm form, Model model) {
    // セッションに一時保存
    saveStep1FormToSession(form);

    // リダイレクト
    return "redirect:/cbt_cr/step2_view";
  }

  /**
   * フォームデータをセッションに保存
   *
   * @param form 入力フォーム
   */
  private void saveStep1FormToSession(CbtCrInputForm form) {
    // step1の入力値を保存
    session.setAttribute("negativeFeelIds", form.getNegativeFeelIds());
    session.setAttribute("positiveFeelIds", form.getPositiveFeelIds());
    session.setAttribute("fact", form.getFact());
    session.setAttribute("mind", form.getMind());
  }

  /**
   * セッションからデータを取得してステップ2画面を表示
   *
   * @param model モデル
   * @return ビュー名
   */
  public String processStep2FromSession(Model model) {
    // 思考の歪みを取得
    List<DistortionList> distortionLists = distortionListMapper.findAll();

    // ビューデータを作成
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setDistortionLists(distortionLists);

    // モデルに追加
    model.addAttribute("viewData", viewData);
    model.addAttribute("cbtCrForm", new CbtCrInputForm());

    return CbtCrConst.STEP2_PATH;
  }

  /**
   * 詳細画面の処理
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  public String processShow(Long id, Model model) {
    // 認知再構成法を取得し、アクセス権限をチェック
    CbtCr cbtCr = validateAccessPermission(id);
    if (cbtCr == null) {
      return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
    }

    // モデルに追加
    model.addAttribute("cbtCr", cbtCr);

    return CbtCrConst.SHOW_PATH;
  }

  /**
   * 編集画面の処理
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  public String processEdit(Long id, Model model) {
    // 認知再構成法を取得し、アクセス権限をチェック
    CbtCr cbtCr = validateAccessPermission(id);
    if (cbtCr == null) {
      return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
    }

    // フォームに値をセット
    CbtCrInputForm form = createCbtCrInputForm(cbtCr);

    // ビューデータを作成
    CbtCrViewData viewData = createCbtCrViewDataWithFeel();

    // モデルに追加
    model.addAttribute("viewData", viewData);
    model.addAttribute("cbtCrForm", form);

    return CbtCrConst.EDIT_PATH;
  }

  private static CbtCrInputForm createCbtCrInputForm(CbtCr cbtCr) {
    CbtCrInputForm form = new CbtCrInputForm();
    form.setId(cbtCr.getId());
    form.setFact(cbtCr.getFact());
    form.setMind(cbtCr.getMind());

    // ネガティブ感情とポジティブ感情のIDをセット
    if (Objects.nonNull(cbtCr.getNegativeFeels())) {
      form.setNegativeFeelIds(cbtCr.getNegativeFeels().stream().map(NegativeFeel::getId).toList());
    }

    if (Objects.nonNull(cbtCr.getPositiveFeels())) {
      form.setPositiveFeelIds(cbtCr.getPositiveFeels().stream().map(PositiveFeel::getId).toList());
    }

    form.setWhyCorrect(cbtCr.getWhyCorrect());
    form.setWhyDoubt(cbtCr.getWhyDoubt());
    form.setNewThought(cbtCr.getNewThought());

    // 思考の歪みのIDをセット
    if (Objects.nonNull(cbtCr.getDistortionLists())) {
      form.setDistortionIds(
          cbtCr.getDistortionLists().stream().map(DistortionList::getId).toList());
    }

    return form;
  }

  /**
   * 編集ステップ2画面の処理
   *
   * @param id 認知再構成法のID
   * @param form 入力フォーム
   * @param model モデル
   * @return ビュー名
   */
  public String processEditStep2(Long id, CbtCrInputForm form, Model model) {
    // 認知再構成法を取得し、アクセス権限をチェック
    CbtCr cbtCr = validateAccessPermission(id);
    if (cbtCr == null) {
      return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
    }

    // セッションに一時保存
    saveStep1FormToSession(form);

    // リダイレクト
    // セッションに値を格納するため、step2へ遷移する前にセッション保存し、step2へリダイレクトする
    return "redirect:/cbt_cr/" + id + "/edit_step2_view";
  }

  /**
   * セッションからデータを取得して編集ステップ2画面を表示
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  public String processEditStep2FromSession(Long id, Model model) {
    // 認知再構成法を取得し、アクセス権限をチェック
    CbtCr cbtCr = validateAccessPermission(id);
    if (cbtCr == null) {
      return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
    }

    // 思考の歪みを取得
    List<DistortionList> distortionLists = distortionListMapper.findAll();

    // ビューデータを作成
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setDistortionLists(distortionLists);

    // フォームに既存の値をセット
    CbtCrInputForm form = new CbtCrInputForm();
    form.setId(id);
    form.setWhyCorrect(cbtCr.getWhyCorrect());
    form.setWhyDoubt(cbtCr.getWhyDoubt());
    form.setNewThought(cbtCr.getNewThought());

    // 思考の歪みのIDをセット
    if (Objects.nonNull(cbtCr.getDistortionLists())) {
      form.setDistortionIds(
          cbtCr.getDistortionLists().stream().map(DistortionList::getId).toList());
    }

    // モデルに追加
    model.addAttribute("viewData", viewData);
    model.addAttribute("cbtCrForm", form);

    return CbtCrConst.EDIT_STEP2_PATH;
  }
}
