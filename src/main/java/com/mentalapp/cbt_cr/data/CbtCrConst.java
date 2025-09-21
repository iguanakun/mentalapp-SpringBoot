package com.mentalapp.cbt_cr.data;

/** 認知再構成法の定数クラス */
public class CbtCrConst {
  /** 新規作成画面のパス */
  public static final String NEW_PATH = "cbt_cr/new";

  /** ステップ2画面のパス */
  public static final String STEP2_PATH = "cbt_cr/step2";

  /** 詳細画面のパス */
  public static final String SHOW_PATH = "cbt_cr/show";

  /** 編集画面のパス */
  public static final String EDIT_PATH = "cbt_cr/edit";

  /** 編集ステップ2画面のパス */
  public static final String EDIT_STEP2_PATH = "cbt_cr/edit_step2";

  /** ステップ2画面表示へのリダイレクト */
  public static final String REDIRECT_STEP2_VIEW = "redirect:/cbt_cr/step2_view";

  /** 編集ステップ2画面表示へのリダイレクト（IDを含む）のプレフィックス */
  public static final String REDIRECT_EDIT_STEP2_VIEW_PREFIX = "redirect:/cbt_cr/";

  /** 編集ステップ2画面表示へのリダイレクト（IDを含む）のサフィックス */
  public static final String REDIRECT_EDIT_STEP2_VIEW_SUFFIX = "/edit_step2_view";
}
