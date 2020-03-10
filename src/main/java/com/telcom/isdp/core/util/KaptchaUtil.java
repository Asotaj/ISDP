package com.telcom.isdp.core.util;

import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.telcom.isdp.config.properties.IsdpProperties;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(IsdpProperties.class).getKaptchaOpen();
    }
}