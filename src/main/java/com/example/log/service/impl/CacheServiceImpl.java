package com.example.log.service.impl;

import com.example.log.dto.DataDto;
import com.example.log.service.CacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author: ex-zhuhongqiang001
 * @Description:
 * @Date: 2018/11/12
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService{

    @Override
    @CachePut(value = "people", key = "#bean.id")
    public <T> T put(T bean) {
        return bean;
    }

    @Override
    @CacheEvict(value = "people")
    public void remove(String id) {

    }

    @Override
    @Cacheable(value = "people", key = "#id")
    public <T> T get(String id,Class<T> clazz) {
        System.out.println("============");
        DataDto dto = new DataDto();
        dto.setId("37478");
        dto.setData("success");
        return (T)dto;
    }
}
