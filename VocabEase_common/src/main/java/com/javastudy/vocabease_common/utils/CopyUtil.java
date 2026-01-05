package com.javastudy.vocabease_common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyUtil {
    /**
     * 复制集合
     */
    public static <T, S>List<T> copyList(List<S> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList<T>();
        for (S s : sourceList) {
            T t = null;
            try {
                t = targetClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            BeanUtils.copyProperties(s, t);
            targetList.add(t);
        }
        return targetList;
    }
    /**
     * 复制单个对象
     */
    public static <T, S> T copy(S s, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(s, t);
        return t;
    }
}
