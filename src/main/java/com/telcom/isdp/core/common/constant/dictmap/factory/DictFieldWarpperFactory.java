package com.telcom.isdp.core.common.constant.dictmap.factory;

import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.telcom.isdp.core.common.constant.factory.ConstantFactory;
import com.telcom.isdp.core.common.constant.factory.IConstantFactory;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;

import java.lang.reflect.Method;

public class DictFieldWarpperFactory {

    public static Object createFieldWarpper(Object parameter, String methodName) {
        IConstantFactory constantFactory = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, parameter.getClass());
            return method.invoke(constantFactory, parameter);
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Long.class);
                return method.invoke(constantFactory, Long.parseLong(parameter.toString()));
            } catch (Exception e1) {
                throw new ServiceException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
            }
        }
    }

}
