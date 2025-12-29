package com.wyc.bookkeeping.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.wyc.bookkeeping.util.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王亚川
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${ip}")
    String ip;

    @Value("${server.port}")
    String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";



    @PostMapping("/upload")
    public Result upload(@RequestParam("files") MultipartFile[] files) throws IOException {  // 修改参数为MultipartFile数组
        List<String> urls = new ArrayList<>();

        if (!FileUtil.exist(ROOT_PATH)) {
            FileUtil.mkdir(ROOT_PATH);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            String originalFilename = file.getOriginalFilename();
            String mainName = FileUtil.mainName(originalFilename);
            String extName = FileUtil.extName(originalFilename);

            if (FileUtil.exist(ROOT_PATH + File.separator + originalFilename)) {
                originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
            }

            File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
            file.transferTo(saveFile);
            String url = "http://" + ip + ":" + port + "/file/download/" + originalFilename;
            urls.add(url);
        }

        return Result.success(urls);  // 返回URL列表
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
//        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 附件下载
        response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 预览
        String filePath = ROOT_PATH + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);  // 数组是一个字节数组，也就是文件的字节流数组
        outputStream.flush();
        outputStream.close();
    }


    @PostMapping("/editor/upload")
    public Dict editorUpload(@RequestParam MultipartFile file, @RequestParam String type) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 文件的原始名称
        // aaa.png
        String mainName = FileUtil.mainName(originalFilename);  // aaa
        String extName = FileUtil.extName(originalFilename);// png
        if (!FileUtil.exist(ROOT_PATH)) {
            FileUtil.mkdir(ROOT_PATH);  // 如果当前文件的父级目录不存在，就创建
        }
        if (FileUtil.exist(ROOT_PATH + File.separator + originalFilename)) {  // 如果当前上传的文件已经存在了，那么这个时候我就要重名一个文件名称
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile);  // 存储文件到本地的磁盘里面去
        String url = "http://" + ip + ":" + port + "/file/download/" + originalFilename;
        if ("img".equals(type)) {
            return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url", url)));
        } else if ("video".equals(type)) {
            return Dict.create().set("errno", 0).set("data", Dict.create().set("url", url));
        }
        return Dict.create().set("errno", 0);
    }

}
