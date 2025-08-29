package com.mentalapp.user_memo_list.controller;

import com.mentalapp.cbt_basic.entity.CbtBasics;
import com.mentalapp.common.entity.User;
import com.mentalapp.cbt_basic.service.CbtBasicsIndexService;
import com.mentalapp.common.util.MentalCommonUtils;
import com.mentalapp.user_memo_list.service.MemoListIndexService;
import com.mentalapp.user_memo_list.data.MemoListConst;
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
    private MentalCommonUtils mentalCommonUtils;
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
        User currentUser = mentalCommonUtils.getUser();
        
        // ユーザーのメモデータを取得
        Map<String, Object> userData = memoListIndexService.getUserMemoData(currentUser.getId());
        
        // モデルに追加
        model.addAllAttributes(userData);
        
        return MemoListConst.INDEX_PATH;
    }
}