package com.mentalapp.cbt_basic.controller;

import com.mentalapp.cbt_basic.form.CbtBasicsInputForm;
import com.mentalapp.cbt_basic.service.CbtBasicsIndexService;
import com.mentalapp.cbt_basic.service.CbtBasicsRegistService;
import com.mentalapp.cbt_basic.util.CbtBasicCommonUtils;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * CBT Basicsのコントローラークラス
 */
@Controller
@RequestMapping("/cbt_basics")
@RequiredArgsConstructor
public class CbtBasicsController {

    private final CbtBasicsIndexService cbtBasicsIndexService;
    private final CbtBasicsRegistService cbtBasicsRegistService;

    /**
     * 新規作成フォーム表示
     * @param model モデル
     * @return 新規作成フォームのビュー名
     */
    @GetMapping("/new")
    public String newCbtBasic(Model model) {
        CbtBasicsViewData viewData = cbtBasicsIndexService.processNew();

        model.addAttribute("viewData", viewData);
        model.addAttribute("cbtBasicsForm", new CbtBasicsInputForm());

        return CbtBasicsConst.NEW_PATH;
    }

    /**
     * 新規作成処理
     * @param form 入力フォーム
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @return 遷移先のビュー名
     */
    @PostMapping("/regist")
    public String create(@Valid @ModelAttribute("cbtBasicsForm") CbtBasicsInputForm form,
                         BindingResult bindingResult,
                         Model model) {
        return cbtBasicsRegistService.processRegist(form, bindingResult, model);
    }

    /**
     * 詳細表示
     * @param id CBT BasicsのID
     * @param model モデル
     * @return 詳細ページのビュー名
     */
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        return cbtBasicsIndexService.processShow(id, model);
    }

    /**
     * 編集フォーム表示
     * @param id CBT BasicsのID
     * @param model モデル
     * @return 編集フォームのビュー名
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        return cbtBasicsIndexService.processEdit(id, model);
    }

    /**
     * 更新処理
     * @param id CBT BasicsのID
     * @param form 入力フォーム
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @return 遷移先のビュー名
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("cbtBasicsForm") CbtBasicsInputForm form,
                         BindingResult bindingResult,
                         Model model) {
        // バリデーションエラーがある場合
        if (CbtBasicCommonUtils.checkValidationError(bindingResult)
        || !form.hasAnyContent()) {
            // ビューデータを作成して追加
            CbtBasicsViewData viewData = CbtBasicCommonUtils.createAllFeelsViewData();
            model.addAttribute("viewData", viewData);
            return CbtBasicsConst.EDIT_PATH;
        }

        return cbtBasicsRegistService.processUpdate(form);
    }

    /**
     * 削除処理
     * @param id 削除するCBT BasicsのID
     * @return 遷移先のビュー名
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return cbtBasicsRegistService.processDelete(id);
    }
}