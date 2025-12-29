package com.wyc.bookkeeping.controller;

import cn.hutool.core.util.StrUtil;
import com.wyc.bookkeeping.entity.DTO.LoginDTO;
import com.wyc.bookkeeping.entity.DTO.LoginResponseDTO;
import com.wyc.bookkeeping.entity.DTO.PasswordDTO;
import com.wyc.bookkeeping.entity.DTO.RegisterDTO;
import com.wyc.bookkeeping.service.UserService;
import com.wyc.bookkeeping.util.JwtUtil;
import com.wyc.bookkeeping.util.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王亚川
 */
@RestController
@CrossOrigin
public class userController {

    @Resource
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO user) {
        // 参数校验
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()
                || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("用户名或密码不能为空");
        }

        try {
            userService.register(user);
            return Result.success("注册成功");
        } catch (Exception e) {
            // 可以根据具体业务异常类型返回不同的错误信息
            return Result.error("注册失败：" + e.getMessage());
        }
    }


    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO token = userService.login(loginDTO);
        return Result.success(token);
    }

    //获取个人中心
    @GetMapping("/user/info")
    public Result getUserInfo() {
        Long userId = jwtUtil.getCurrentUserId();
        return Result.success(userService.getById(userId));
    }




    //修改密码
    @PostMapping("/password")
    public Result updatePassword(@RequestBody PasswordDTO passwordDTO) {
        // 1. 校验参数（非空）
        if (StrUtil.isBlank(passwordDTO.getOldPassword()) || StrUtil.isBlank(passwordDTO.getNewPassword())) {
            return Result.error("旧密码和新密码不能为空");
        }
        // 2. 调用 Service 层处理业务逻辑（修改密码）
        userService.updatePassword(passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return Result.success("密码修改成功");
    }


}
