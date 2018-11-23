package com.example.log.service;

/**
 * @Author: ex-zhuhongqiang001
 * @Description:
 * @Date: 2018/11/12
 */
public interface CacheService {


    <T> T  put(T bean);

    void remove(String id);

    <T> T  get(String id,Class<T> clazz);
}
