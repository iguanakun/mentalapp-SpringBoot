package com.mentalapp.cbt_basic.controller;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.cbt_basic.viewdata.CbtBasicsViewData;
import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import com.mentalapp.cbt_basic.service.CbtBasicsIndexService;
import com.mentalapp.cbt_basic.service.CbtBasicsRegistService;
import com.mentalapp.cbt_basic.form.CbtBasicsForm;
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

/**
 * CBT Basicsのコントローラークラス
 */
@Controller
@RequestMapping("/cbt_basics")
public class CbtBasicsController {

    private final CbtBasicsIndexService cbtBasicsIndexService;
    private final CbtBasicsRegistService cbtBasicsRegistService;

    @Autowired
    public CbtBasicsController(CbtBasicsIndexService cbtBasicsIndexService, 
                              CbtBasicsRegistService cbtBasicsRegistService) {
        this.cbtBasicsIndexService = cbtBasicsIndexService;
        this.cbtBasicsRegistService = cbtBasicsRegistService;
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
                         BindingResult bindingResult) {
        
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            return "cbt_basics/new";
        }
        
        // フォームからエンティティへの変換
        CbtBasics cbtBasics = new CbtBasics();
        cbtBasics.setFact(form.getFact());
        cbtBasics.setMind(form.getMind());
        cbtBasics.setBody(form.getBody());
        cbtBasics.setBehavior(form.getBehavior());
        
        // ログインユーザーの取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        cbtBasics.setUser(currentUser);
        
        // 保存（ネガティブ感情とポジティブ感情の関連付けも行う）
        cbtBasicsRegistService.save(cbtBasics, form.getNegativeFeelIds(), form.getPositiveFeelIds());
        
        // TODO: タグの保存処理
        
        return "redirect:/cbt_basics/lists";
    }

    /**
     * 詳細表示
     */
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        CbtBasics cbtBasics = cbtBasicsIndexService.findById(id);
        
        // アクセス権チェック
        if (!isAuthorized(cbtBasics)) {
            return "redirect:/";
        }
        
        model.addAttribute("cbtBasic", cbtBasics);
        return "cbt_basics/show";
    }

    /**
     * 編集フォーム表示
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        CbtBasics cbtBasics = cbtBasicsIndexService.findById(id);
        
        // アクセス権チェック
        if (!isAuthorized(cbtBasics)) {
            return "redirect:/";
        }
        
        // エンティティからフォームへの変換
        CbtBasicsForm form = new CbtBasicsForm();
        form.setId(cbtBasics.getId());
        form.setFact(cbtBasics.getFact());
        form.setMind(cbtBasics.getMind());
        form.setBody(cbtBasics.getBody());
        form.setBehavior(cbtBasics.getBehavior());
        form.setUserId(cbtBasics.getUser().getId());
        
        // ネガティブ感情とポジティブ感情のIDを設定
        List<NegativeFeel> negativeFeels = cbtBasics.getNegativeFeels();
        if (negativeFeels != null && !negativeFeels.isEmpty()) {
            List<Long> negativeFeelingIds = negativeFeels.stream()
                    .map(NegativeFeel::getId)
                    .toList();
            form.setNegativeFeelIds(negativeFeelingIds);
        }
        
        List<PositiveFeel> positiveFeels = cbtBasics.getPositiveFeels();
        if (positiveFeels != null && !positiveFeels.isEmpty()) {
            List<Long> positiveFeelingIds = positiveFeels.stream()
                    .map(PositiveFeel::getId)
                    .toList();
            form.setPositiveFeelIds(positiveFeelingIds);
        }
        
        // TODO: タグの取得処理
        
        model.addAttribute("cbtBasicsForm", form);
        return "cbt_basics/edit";
    }

    /**
     * 更新処理
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("cbtBasicsForm") CbtBasicsForm form,
                         BindingResult bindingResult) {
        
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            return "cbt_basics/edit";
        }
        
        CbtBasics cbtBasics = cbtBasicsIndexService.findById(id);
        
        // アクセス権チェック
        if (!isAuthorized(cbtBasics)) {
            return "redirect:/";
        }
        
        // フォームからエンティティへの更新
        cbtBasics.setFact(form.getFact());
        cbtBasics.setMind(form.getMind());
        cbtBasics.setBody(form.getBody());
        cbtBasics.setBehavior(form.getBehavior());
        
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
        if (!isAuthorized(cbtBasics)) {
            return "redirect:/";
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        // ユーザーのCBT Basicsを取得
        List<CbtBasics> cbtBasicsList = cbtBasicsIndexService.findByUserId(currentUser.getId());
        model.addAttribute("cbtBasics", cbtBasicsList);
        
        // ネガティブ感情の上位3つを取得
        List<Map<String, Object>> topNegativeFeels = cbtBasicsIndexService.findTopNegativeFeelings(currentUser.getId());
        model.addAttribute("negativeFeels", topNegativeFeels);
        
        return "cbt_basics/lists";
    }

    /**
     * アクセス権チェック
     * @param cbtBasics チェック対象のCBT Basics
     * @return アクセス権がある場合はtrue
     */
    private boolean isAuthorized(CbtBasics cbtBasics) {
        if (cbtBasics == null) {
            return false;
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        return cbtBasics.getUser().getId().equals(currentUser.getId());
    }
}