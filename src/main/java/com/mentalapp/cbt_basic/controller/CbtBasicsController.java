package com.mentalapp.cbt_basic.controller;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import com.mentalapp.cbt_basic.service.CbtBasicsIndexService;
import com.mentalapp.cbt_basic.service.CbtBasicsRegistService;
import com.mentalapp.cbt_basic.form.CbtBasicsForm;
import com.mentalapp.common.service.UserService;
import com.mentalapp.common.util.MentalCommonUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CBT Basicsのコントローラークラス
 */
@Controller
@RequestMapping("/cbt_basics")
public class CbtBasicsController {

    private final CbtBasicsIndexService cbtBasicsIndexService;
    private final CbtBasicsRegistService cbtBasicsRegistService;
    private final UserService userService;
    @Autowired
    private MentalCommonUtils mentalCommonUtils;

    @Autowired
    public CbtBasicsController(CbtBasicsIndexService cbtBasicsIndexService,
                               CbtBasicsRegistService cbtBasicsRegistService, UserService userService) {
        this.cbtBasicsIndexService = cbtBasicsIndexService;
        this.cbtBasicsRegistService = cbtBasicsRegistService;
        this.userService = userService;
    }

    /**
     * 新規作成フォーム表示
     */
    @GetMapping("/new")
    public String newCbtBasic(Model model) {
        // ビューデータを作成
        CbtBasicsViewData viewData = cbtBasicsIndexService.createViewData();

        // フォームにセット
        model.addAttribute("viewData", viewData);
        model.addAttribute("cbtBasicsForm", new CbtBasicsForm());

        return "cbt_basics/new";
    }

    /**
     * 新規作成処理
     */
    @PostMapping("/regist")
    public String create(@Valid @ModelAttribute("cbtBasicsForm") CbtBasicsForm form,
                         BindingResult bindingResult,
                         Model model) {
        
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            // ビューデータを作成して追加
            CbtBasicsViewData viewData = cbtBasicsIndexService.createViewData();
            model.addAttribute("viewData", viewData);
            return "cbt_basics/new";
        }
        
        // フォームからエンティティへの変換
        CbtBasics cbtBasics = cbtBasicsIndexService.convertToEntity(form);
        
        // ログインユーザーの取得
        cbtBasics.setUserId(mentalCommonUtils.getUser().getId());
        
        // 保存（ネガティブ感情とポジティブ感情の関連付けも行う）
        cbtBasicsRegistService.save(cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds());
        
        // TODO: タグの保存処理

        return "redirect:/memos";
    }

    /**
     * 詳細表示
     */
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        CbtBasics cbtBasics = cbtBasicsIndexService.selectByPrimaryKeyWithFeels(id);
        
        if(Objects.isNull(cbtBasics)){
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }

        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        model.addAttribute("cbtBasic", cbtBasics);
        return "cbt_basics/show";
    }

    /**
     * 編集フォーム表示
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        CbtBasics cbtBasics = cbtBasicsIndexService.selectByPrimaryKeyWithFeels(id);
        
        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // ビューデータを作成
        CbtBasicsViewData viewData = cbtBasicsIndexService.createViewData();
        
        // エンティティからフォームへの変換
        CbtBasicsForm form = cbtBasicsIndexService.convertToForm(cbtBasics);

        // TODO: タグの取得処理

        // フォームにセット
        model.addAttribute("viewData", viewData);
        model.addAttribute("cbtBasicsForm", form);

        return "cbt_basics/edit";
    }

    /**
     * 更新処理
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("cbtBasicsForm") CbtBasicsForm form,
                         BindingResult bindingResult,
                         Model model) {
        
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            // ビューデータを作成して追加
            CbtBasicsViewData viewData = cbtBasicsIndexService.createViewData();
            model.addAttribute("viewData", viewData);
            return "cbt_basics/edit";
        }
        
        CbtBasics cbtBasics = cbtBasicsIndexService.findById(id);
        
        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // フォームからエンティティへの更新
        cbtBasics = cbtBasicsIndexService.convertToEntity(form);
        
        // 更新（ネガティブ感情とポジティブ感情の関連付けも行う）
        cbtBasicsRegistService.update(cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds());
        
        // TODO: タグの更新処理
        
        return "redirect:/cbt_basics/lists";
    }

    /**
     * 削除処理
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        CbtBasics cbtBasics = cbtBasicsIndexService.findById(id);
        
        // アクセス権チェック
        if (!mentalCommonUtils.isAuthorized(cbtBasics.getUserId())) {
            return MentalCommonUtils.REDIRECT_TOP_PAGE;
        }
        
        // 削除
        cbtBasicsRegistService.deleteById(id);
        return "redirect:/cbt_basics/lists";
    }

    /**
     * 一覧表示
     */
    @GetMapping("/lists")
    public String lists(Model model) {
        // ログインユーザーの取得
        User currentUser = mentalCommonUtils.getUser();
        
        // ユーザーのCBT Basicsデータを取得
        Map<String, Object> userData = cbtBasicsIndexService.getUserCbtBasicsData(currentUser.getId());
        
        // モデルに追加
        model.addAllAttributes(userData);
        
        return "cbt_basics/lists";
    }

}