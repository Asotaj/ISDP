package com.telcom.isdp.core.beetl;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.telcom.isdp.core.common.constant.Const;
import com.telcom.isdp.core.util.KaptchaUtil;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import java.util.HashMap;
import java.util.Map;

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    @Override
    public void initOther() {

        //全局共享变量
        Map<String, Object> shared = new HashMap<>();
        shared.put("systemName", Const.DEFAULT_SYSTEM_NAME);
        shared.put("welcomeTip", Const.DEFAULT_WELCOME_TIP);
        groupTemplate.setSharedVars(shared);

        //全局共享方法
        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
    }
}
