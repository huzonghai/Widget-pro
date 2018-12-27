package com.ck.newssdk.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heihei on 2017/7/13.
 */

public class JsonUtil {

    public static <T> ArrayList<T> parseArray(String s, Class<T> categoryBeanClass) {
        JSONArray jsonArray = null;
        ArrayList<T> list = new ArrayList<T>();
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                T bean = JsonUtil.parseObject(jsonObject.toString(), categoryBeanClass);
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T parseObject(String json, Class<T> aClass) {
        Object obj = constructorNewInstance(aClass.getName(), null, null);
        setObj(obj, json);
        return (T) obj;
    }

    public static JSONObject toJson(Object object) {
        Field[] fileds = object.getClass().getDeclaredFields();
        JSONObject jsonObj = new JSONObject();
        for (Field filed : fileds) {
            Class<?> aClass = filed.getType();
            try {
                filed.setAccessible(true);
                if (checkType(aClass))
                    jsonObj.put(filed.getName(), filed.get(object) != null ? filed.get(object) : ""); //如果是null 至为空字符串。否则json会忽略这个对象。
                else if (aClass.getName().equals("java.util.List")) {
                    List list = (List) filed.get(object);
                    JSONArray jsonArray = new JSONArray();
                    for (Object obj : list) {
                        if (obj != null)
                            jsonArray.put(checkType(obj.getClass()) ? obj : toJson(obj));
                    }
                    jsonObj.put(filed.getName(), jsonArray);
                } else {
                    if (filed.getName().equals("$change") || filed.getName().equals("serialVersionUID"))
                        continue;
                    jsonObj.put(filed.getName(), filed.get(object) != null ? toJson(filed.get(object)) : "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            filed.getName();
        }

        return jsonObj;
    }

    private static boolean checkType(Class<?> aClass) {
        if (aClass.getName().equals("java.lang.String") || aClass.getName().equals("int") || aClass.getName().equals("long") || aClass.getName().equals("double") || aClass.getName().equals("boolean") || aClass.getName().equals("float"))
            return true;
        return false;
    }


    public static void setObj(Object object, String json) {
        StringBuilder compares = new StringBuilder();
        Field[] fileds = object.getClass().getDeclaredFields();
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        for (Field filed : fileds) {
            String propertyName = filed.getName();
            if (propertyName.equals("serialVersionUID")||propertyName.equals("itemType")||propertyName.equals("imgs"))
                continue;
            Class<?> aClass = filed.getType();
            Object propertyValue = getValue(jsonObj, aClass, propertyName);
            setFieldValue(filed, object, propertyValue);
        }
    }

    private static Object getValue(JSONObject jsonObj, Class<?> aClass, String propertyName) {
        Object obj = null;
        try {
            if (aClass.getName().equals("java.lang.String"))
                obj = jsonObj.getString(propertyName);
            else if (aClass.getName().equals("int"))
                obj = jsonObj.getInt(propertyName);
            else if (aClass.getName().equals("long"))
                obj = jsonObj.getLong(propertyName);
            else if (aClass.getName().equals("double"))
                obj = jsonObj.getDouble(propertyName);
            else if (aClass.getName().equals("boolean"))
                obj = jsonObj.getBoolean(propertyName);
      /*      else if (aClass.getName().equals("float"))
                obj = Float.valueOf(jsonObj.getString(value));*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public static void setFieldValue(Field field, Object obj, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static Object constructorNewInstance(String className, Class[] parameterTypes, Object[] initargs) {
        try {
            Constructor<?> constructor = (Constructor<?>) Class
                    .forName(className).getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(initargs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
