package com.telcom.isdp.core.util;

import com.telcom.isdp.core.listener.ConfigListener;

public class DefaultImages {

    public static String loginBg() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/login-register.jpg";
    }
    public static String defaultAvatarUrl() {
        return ConfigListener.getConf().get("contextPath") + "/system/previewAvatar";
    }

    public static String error404() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/error-bg.jpg";
    }
}
