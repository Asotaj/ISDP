package com.telcom.isdp.core.metadata;

import cn.stylefeng.roses.core.metadata.CustomMetaObjectHandler;
import com.telcom.isdp.core.shiro.ShiroKit;
import org.springframework.stereotype.Component;

@Component
public class IsdpMpFieldHandler extends CustomMetaObjectHandler {

    @Override
    protected Object getUserUniqueId() {
        try {

            return ShiroKit.getUser().getId();

        } catch (Exception e) {

            //如果获取不到当前用户就存空id
            return "";
        }
    }
}
