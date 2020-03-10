package com.telcom.isdp.core.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ToolUtil {

    public static T convert(Class<T> reflect, JSONObject jsonObject){
        T ref = new T();
        try{
            Class<?> obj=Class.forName(ref.getClass().getName());
            Object object = obj.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for(int i = 0;i < fields.length; i++){
                System.out.print(fields[i].getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
