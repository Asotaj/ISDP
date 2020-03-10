package com.telcom.isdp.modular.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Wrapper;

/**
 * @author jiayq
 * @date 2019/11/7 10:32
 */
@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    private String PREFIX = "/modular/business/analysis/";

    /**
     * 跳转到分析首页
     *
     * @author jiayq
     * @date 2019/11/7 10:32
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "analysis.html";
    }

    /**
     * 跳转到分析首页
     *
     * @author jiayq
     * @date 2019/11/7 10:32
     */
    @RequestMapping("/content")
    public String content() {
        return PREFIX + "content.html";
    }

}
