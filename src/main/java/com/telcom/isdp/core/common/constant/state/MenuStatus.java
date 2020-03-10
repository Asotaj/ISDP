package com.telcom.isdp.core.common.constant.state;

import lombok.Getter;

@Getter
public enum MenuStatus {

    ENABLE("ENABLE", "启用"),
    DISABLE("DISABLE", "禁用");

    String code;
    String message;

    MenuStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getDescription(String status) {
        if (status == null) {
            return "";
        } else {
            for (MenuStatus s : MenuStatus.values()) {
                if (s.getCode().equals(status)) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
