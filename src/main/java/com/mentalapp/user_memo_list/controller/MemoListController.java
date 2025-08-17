package com.mentalapp.user_memo_list.controller;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.common.entity.User;
import com.mentalapp.cbt_basic.service.CbtBasicsIndexService;
import com.mentalapp.common.util.MentalCommonObject;
import com.mentalapp.user_memo_list.service.MemoListIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * メモ一覧のコントローラークラス
 */
@Controller
@RequestMapping("/memos")
public class MemoListController {

    @Autowired
    private MentalCommonObject mentalCommonObject;
    private final CbtBasicsIndexService cbtBasicsIndexService;
    private final MemoListIndexService memoListIndexService;

    @Autowired
    public MemoListController(CbtBasicsIndexService cbtBasicsIndexService, MemoListIndexService memoListIndexService) {
        this.cbtBasicsIndexService = cbtBasicsIndexService;
        this.memoListIndexService = memoListIndexService;
    }

    /**
     * メモ一覧表示
     */
    @GetMapping
    public String index(Model model) {
        // ログインユーザーの取得
        User currentUser = mentalCommonObject.getUser();
        
        // ユーザーのCBT Basicsを取得
        List<CbtBasics> cbtBasicsList = memoListIndexService.createCbtBasicsObjectList(currentUser.getId());
        model.addAttribute("cbtBasics", cbtBasicsList);
        
        // ネガティブ感情の上位3つを取得
        List<Map<String, Object>> topNegativeFeels = cbtBasicsIndexService.findTopNegativeFeelings(currentUser.getId());
        model.addAttribute("negativeFeels", topNegativeFeels);
        
        return "user_memo_list/index";
    }
}