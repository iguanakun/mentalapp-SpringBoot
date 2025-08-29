package com.mentalapp.cbt_basic.controller;

import com.mentalapp.cbt_basic.service.CbtBasicsIndexService;
import com.mentalapp.cbt_basic.service.CbtBasicsRegistService;
import com.mentalapp.cbt_basic.form.CbtBasicsForm;
import com.mentalapp.common.service.UserService;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.cbt_basic.data.CbtBasicsConst;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * CBT Basicsのコントローラークラス
 */
@Controller
@RequestMapping("/cbt_basics")
public class CbtBasicsController {

    private final CbtBasicsIndexService cbtBasicsIndexService;
    private final CbtBasicsRegistService cbtBasicsRegistService;
    private final UserService userService;
    private final MentalCommonUtils mentalCommonUtils;

    @Autowired
    public CbtBasicsController(CbtBasicsIndexService cbtBasicsIndexService,
                               CbtBasicsRegistService cbtBasicsRegistService, 
                               UserService userService,
                               MentalCommonUtils mentalCommonUtils) {
        this.cbtBasicsIndexService = cbtBasicsIndexService;
        this.cbtBasicsRegistService = cbtBasicsRegistService;
        this.userService = userService;
        this.mentalCommonUtils = mentalCommonUtils;
    }

    /**
     * 新規作成フォーム表示
     */
    @GetMapping("/new")
    public String newCbtBasic(Model model) {
        cbtBasicsIndexService.setViewData(model);
        model.addAttribute("cbtBasicsForm", new CbtBasicsForm());

        return CbtBasicsConst.NEW_PATH;
    }

    /**
     * 新規作成処理
     */
    @PostMapping("/regist")
    public String create(@Valid @ModelAttribute("cbtBasicsForm") CbtBasicsForm form,
                         BindingResult bindingResult,
                         Model model) {
        return cbtBasicsIndexService.processCreate(form, bindingResult, model, cbtBasicsRegistService);
    }

    /**
     * 詳細表示
     */
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        String redirectPath = cbtBasicsIndexService.prepareShowDetail(id, model);
        return redirectPath != null ? redirectPath : CbtBasicsConst.SHOW_PATH;
    }

    /**
     * 編集フォーム表示
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        String redirectPath = cbtBasicsIndexService.prepareEditForm(id, model);
        return redirectPath != null ? redirectPath : CbtBasicsConst.EDIT_PATH;
    }

    /**
     * 更新処理
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("cbtBasicsForm") CbtBasicsForm form,
                         BindingResult bindingResult,
                         Model model) {
        return cbtBasicsIndexService.processUpdate(id, form, bindingResult, model, cbtBasicsRegistService);
    }

    /**
     * 削除処理
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return cbtBasicsIndexService.processDelete(id, cbtBasicsRegistService);
    }

    /**
     * 一覧表示
     */
    @GetMapping("/lists")
    public String lists(Model model) {
        cbtBasicsIndexService.prepareListView(model);
        return CbtBasicsConst.LISTS_PATH;
    }

}