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
import com.mentalapp.common.exception.DatabaseException;
import com.mentalapp.common.util.MentalCommonUtils;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

/** 認知再構成法の表示処理サービス */
@Service
@RequiredArgsConstructor
@Slf4j
public class CbtCrIndexService {

  private final CbtCrMapper cbtCrMapper;
  private final NegativeFeelMapper negativeFeelMapper;
  private final PositiveFeelMapper positiveFeelMapper;
  private final DistortionListMapper distortionListMapper;
  private final CbtCrCommonUtils cbtCrCommonUtils;
  private final MentalCommonUtils mentalCommonUtils;
  private final HttpSession session;

  /**
   * 新規作成画面の処理
   *
   * @param model モデル
   * @return ビュー名
   */
  public String processNew(Model model) {
    // ビューデータを作成（共通ユーティリティクラスのメソッドを使用）
    CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsViewData();

    // モデルに追加
    model.addAttribute("viewData", viewData);
    model.addAttribute("cbtCrForm", new CbtCrInputForm());

    return CbtCrConst.NEW_PATH;
  }

  /**
   * ステップ2画面の処理
   *
   * @param form 入力フォーム
   * @param model モデル
   * @return ビュー名
   */
  public String nextStep2(CbtCrInputForm form, Model model) {
    // セッションに一時保存
    // URLに入力値が出るのを防ぐため、step2へ遷移する前にセッション保存し、リダイレクトする
    saveStep1FormToSession(form);

    // リダイレクト
    return CbtCrConst.REDIRECT_STEP2_VIEW;
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
  @Transactional
  public String processStep2(Model model) throws DatabaseException {
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
  @Transactional
  public String processShow(Long id, Model model) {
    try {
      // 認知再構成法を取得
      CbtCr cbtCr = cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(id);

      // 存在チェックとアクセス権限チェック
      if (Objects.isNull(cbtCr) || !cbtCrCommonUtils.checkAccessPermission(cbtCr)) {
        return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
      }

      // タグ情報を取得
      String tagNames = cbtCrCommonUtils.extractTagNamesToString(cbtCr);

      // モデルに追加
      model.addAttribute("cbtCr", cbtCr);
      model.addAttribute("tagNames", tagNames);

      return CbtCrConst.SHOW_PATH;
    } catch (Exception e) {
      log.error("詳細画面表示中にデータベースエラーが発生しました: {}", e.getMessage(), e);
      throw new DatabaseException("詳細画面表示中にデータベースエラーが発生しました", e);
    }
  }

  /**
   * 編集画面の処理
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  @Transactional
  public String processEdit(Long id, Model model) throws DatabaseException {
    // 認知再構成法を取得
    CbtCr cbtCr = cbtCrMapper.selectByPrimaryKeyWithFeelsAndTags(id);

    // 存在チェックとアクセス権限チェック
    if (Objects.isNull(cbtCr) || !cbtCrCommonUtils.checkAccessPermission(cbtCr)) {
      return MentalCommonUtils.REDIRECT_MEMOS_PAGE;
    }

    // step1の入力値を設定
    Long cbtCrId = cbtCr.getId();
    String fact = null;
    String mind = null;
    List<Long> negativeFeelIds = null;
    List<Long> positiveFeelIds = null;
    // もどる時の復元処理
    if (Objects.nonNull(session.getAttribute("fact"))
        || Objects.nonNull(session.getAttribute("mind"))
        || Objects.nonNull(session.getAttribute("negativeFeelIds"))
        || Objects.nonNull(session.getAttribute("positiveFeelIds"))) {
      fact = (String) session.getAttribute("fact");
      mind = (String) session.getAttribute("mind");
      negativeFeelIds = (List<Long>) session.getAttribute("negativeFeelIds");
      positiveFeelIds = (List<Long>) session.getAttribute("positiveFeelIds");
    } else {
      // もどるではない場合、DB取得値を設定
      fact = cbtCr.getFact();
      mind = cbtCr.getMind();
      negativeFeelIds = mentalCommonUtils.extractedNegativeFeelsIdList(cbtCr.getNegativeFeels());
      positiveFeelIds = mentalCommonUtils.extractedPositiveFeelsIdList(cbtCr.getPositiveFeels());
    }

    // フォームに設定
    CbtCrInputForm form =
        createCbtCrInputForm(cbtCrId, fact, mind, negativeFeelIds, positiveFeelIds);

    // step2の入力値をセッションに設定
    session.setAttribute("distortionIds", cbtCrCommonUtils.extractDistortionIds(cbtCr));
    session.setAttribute("whyCorrect", cbtCr.getWhyCorrect());
    session.setAttribute("whyDoubt", cbtCr.getWhyDoubt());
    session.setAttribute("newThought", cbtCr.getNewThought());
    session.setAttribute("tagNames", cbtCrCommonUtils.extractTagNamesToString(cbtCr));

    // ビューデータを作成
    CbtCrViewData viewData = cbtCrCommonUtils.createAllFeelsViewData();

    // モデルに追加
    model.addAttribute("cbtCrForm", form);
    model.addAttribute("viewData", viewData);

    return CbtCrConst.EDIT_PATH;
  }

  /**
   * CbtCrエンティティからCbtCrInputFormを作成する
   *
   * @param cbtCr 認知再構成法エンティティ
   * @return 作成されたフォーム
   */
  private CbtCrInputForm createCbtCrInputForm(
      Long cbtCrId,
      String fact,
      String mind,
      List<Long> negativeFeelIds,
      List<Long> positiveFeelIds) {
    CbtCrInputForm form = new CbtCrInputForm();
    // ID
    form.setId(cbtCrId);

    // 事実
    form.setFact(fact);
    // 思考
    form.setMind(mind);

    // ネガティブ感情
    form.setNegativeFeelIds(negativeFeelIds);
    // ポジティブ感情
    form.setPositiveFeelIds(positiveFeelIds);

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
    // セッションに一時保存
    // URLに入力値が出るのを防ぐため、step2へ遷移する前にセッション保存し、リダイレクトする
    saveStep1FormToSession(form);

    // リダイレクト
    return CbtCrConst.REDIRECT_EDIT_STEP2_VIEW_PREFIX
        + id
        + CbtCrConst.REDIRECT_EDIT_STEP2_VIEW_SUFFIX;
  }

  /**
   * セッションからデータを取得して編集ステップ2画面を表示
   *
   * @param id 認知再構成法のID
   * @param model モデル
   * @return ビュー名
   */
  @Transactional
  public String processEditStep2FromSession(Long id, Model model) throws DatabaseException {
    // 思考の歪みを取得
    List<DistortionList> distortionLists = distortionListMapper.findAll();

    // ビューデータを作成
    CbtCrViewData viewData = new CbtCrViewData();
    viewData.setDistortionLists(distortionLists);

    // フォームに既存の値をセット
    CbtCrInputForm form = new CbtCrInputForm();
    form.setId(id);
    form.setWhyCorrect((String) session.getAttribute("whyCorrect"));
    form.setWhyDoubt((String) session.getAttribute("whyDoubt"));
    form.setNewThought((String) session.getAttribute("newThought"));
    form.setDistortionIds((List<Long>) session.getAttribute("distortionIds"));
    form.setTagNames((String) session.getAttribute("tagNames"));

    // モデルに追加
    model.addAttribute("viewData", viewData);
    model.addAttribute("cbtCrForm", form);

    return CbtCrConst.EDIT_STEP2_PATH;
  }
}
