package com.mentalapp.work_care.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** 仕事の対処の静的ページのコントローラ */
@Controller
@RequestMapping("/work_care")
public class workCareIndexController {

  /** トップページを表示 */
  @GetMapping("/content1")
  public String showContent1() {
    return "work_care/content1";
  }

  /** ページ1を表示 */
  @GetMapping("/page1")
  public String showPage1() {
    return "work_care/page1";
  }

  /** ページ2を表示 */
  @GetMapping("/page2")
  public String showPage2() {
    return "work_care/page2";
  }

  /** ページ3を表示 */
  @GetMapping("/page3")
  public String showPage3() {
    return "work_care/page3";
  }

  /** ページ4を表示 */
  @GetMapping("/page4")
  public String showPage4() {
    return "work_care/page4";
  }
}
