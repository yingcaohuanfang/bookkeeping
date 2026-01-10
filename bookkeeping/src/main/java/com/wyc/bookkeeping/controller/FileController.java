package com.wyc.bookkeeping.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.wyc.bookkeeping.entity.User;
import com.wyc.bookkeeping.mapper.UserMapper;
import com.wyc.bookkeeping.util.JwtUtil;
import com.wyc.bookkeeping.util.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";

    private static final String AVATAR_PATH = System.getProperty("user.dir") + File.separator + "avatars";


    //上传头像图片
    @PostMapping("/upload/avatar")
    public Result uploadAvatar(@RequestParam("avatar") MultipartFile file) throws IOException {
        // 1. 校验文件
        if (file.isEmpty()) {
            return Result.error("请选择头像文件");
        }

        // 2. 校验文件类型
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {
            return Result.error("只允许上传图片文件");
        }

        // 3. 创建目录
        if (!FileUtil.exist(AVATAR_PATH)) {
            FileUtil.mkdir(AVATAR_PATH);
        }

        // 4. 生成唯一文件名
        String extName = FileUtil.extName(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + "." + extName;

        // 5. 保存文件
        File saveFile = new File(AVATAR_PATH + File.separator + newFileName);
        file.transferTo(saveFile);

        // 6. 返回访问URL
        String url = "http://" + ip + ":" + port + "/file/avatar/" + newFileName;
        // 7.存储到数据库
        User user = jwtUtil.getCurrentUser();
        user.setAvatar(url);
        userMapper.updateById(user);
        return Result.success(url);
    }


    @GetMapping("/avatar/{filename}")
    public void getAvatar(@PathVariable String filename, HttpServletResponse response) throws IOException {
        File file = new File(AVATAR_PATH + File.separator + filename);
        if (!file.exists()) {
            response.sendError(404, "头像文件不存在");
            return;
        }
        response.setContentType("image/jpeg");
        FileUtil.writeToStream(file, response.getOutputStream());
    }


    @PostMapping("/upload")
    public Result upload(@RequestParam("files") MultipartFile[] files) throws IOException {
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
