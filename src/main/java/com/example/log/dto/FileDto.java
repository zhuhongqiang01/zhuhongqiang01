package com.example.log.dto;

import com.example.log.model.DataInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @Author: ex-zhuhongqiang001
 * @Description:
 * @Date: 2018/10/16
 */
public class FileDto {

    private MultipartFile[] files;

    private DataInfo data;

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public DataInfo getData() {
        return data;
    }

    public void setData(DataInfo data) {
        this.data = data;
    }
}
