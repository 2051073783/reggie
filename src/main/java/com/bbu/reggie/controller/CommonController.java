package com.bbu.reggie.controller;

import com.bbu.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        //已.为分隔符，取点后面所以字符串（包括.）
        //就是取原文件的后缀名
        String after = originalFilename.substring(originalFilename.lastIndexOf("."));

        //用UUID生成随机字符串并拼接原来文件的后缀名
        String fileName = UUID.randomUUID().toString() + after;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断目录是否存在,如果不存在创建目录
        if (!dir.exists()){
            dir.mkdirs();
        }

//        log.info(file.toString());
//将零时文件转存
        file.transferTo(new File(basePath+fileName));

        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            ServletOutputStream OutputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes =new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                OutputStream.write(bytes,0,len);
                OutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
