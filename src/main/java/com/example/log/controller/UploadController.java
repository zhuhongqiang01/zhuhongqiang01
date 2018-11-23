package com.example.log.controller;

import com.example.log.dto.BmiTestDTO;
import com.example.log.dto.DataDto;
import com.example.log.dto.FileDto;
import com.example.log.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 文件上传
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadController {

    // ftp服务器ip地址
    @Value("${ftpAddress}")
    private String ftpAddress;
    // 端口号
    @Value("${ftpPort}")
    private int ftpPort;
    // 用户名
    @Value("${ftpName}")
    private String ftpName;
    // 密码
    @Value("${ftpPassWord}")
    private String ftpPassWord;
    // 图片路径
    @Value("${ftpBasePath}")
    private String ftpBasePath;

    @ResponseBody
    @PostMapping(value = "/file", produces = {"application/json;charset=UTF-8"})
    public BmiTestDTO upload(MultipartFile file){

        BmiTestDTO dto = new BmiTestDTO();
        try {

            byte[] bytes = file.getBytes();
            dto.setIcon(bytes);
            dto.setUserSex("F");
            return dto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Autowired
    private CacheService cacheService;


    @ResponseBody
    @GetMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public DataDto save(String id,int page){
        DataDto dto = new DataDto();
        dto.setId(id+"-"+page);
        dto.setData("success"+"page"+page);
        return cacheService.put(dto);
    }

    @ResponseBody
    @GetMapping(value = "/remove", produces = {"application/json;charset=UTF-8"})
    public void remove(String id){
        cacheService.remove(id);
    }

    @ResponseBody
    @GetMapping(value = "/get", produces = {"application/json;charset=UTF-8"})
    public DataDto get(String id){
        return cacheService.get(id,DataDto.class);
    }

    @ResponseBody
    @GetMapping(value = "/merge", produces = {"application/json;charset=UTF-8"})
    public String merge(String id){
        String re = "";
        for (int i = 1; i < 4; i++) {
            DataDto dto = cacheService.get(id + "-" + i, DataDto.class);
            re += dto.getData();
        }
        return re;
    }

    @Autowired
    private ConcurrentMapCache concurrentMapCache;

    private ModelMap modelMap = new ModelMap();

    @ResponseBody
    @GetMapping(value = "/cache", produces = {"application/json;charset=UTF-8"})
    public String cache(String id){
        for (int i = 0; i < 100; i++) {
            concurrentMapCache.put(id+i,"1111111");
        }
        modelMap.clear();
        modelMap.put("1111",id);

        String s = concurrentMapCache.get("111111", String.class);
        return s;
    }
}
