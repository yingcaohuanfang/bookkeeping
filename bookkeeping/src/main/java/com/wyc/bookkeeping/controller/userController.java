package com.wyc.bookkeeping.controller;

import cn.hutool.core.util.StrUtil;
import com.wyc.bookkeeping.entity.DTO.*;
import com.wyc.bookkeeping.entity.User;
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
public class UserController {

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

    //获取所有用户信息
    @GetMapping("/user/list")
    public Result getUserInfoAll() {
        return Result.success(userService.getUserInfoAll());
    }

    //根据id获取用户全部账单
    @GetMapping("/user/bills/{id}")
    public Result getBills(@PathVariable Long id) {
        return Result.success(userService.getBills(id));
    }

    //根据ID获取前三个账单
    @GetMapping("/user/bills/three/{id}")
    public Result getBillsThree(@PathVariable Long id) {
        return Result.success(userService.getBillsThree(id));
    }

    //管理员停用普通账户账号
    @PostMapping("/disable")
    public Result disable(@RequestParam Long userId) {
        User loginAdmin = jwtUtil.getCurrentUser();
        // 校验：当前操作人必须是管理员(role=1)，非管理员禁止调用
        if (loginAdmin.getRole() != 1) {
            return Result.error("权限不足，仅管理员可执行账号停用操作");
        }
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不能为空");
        }
        User disabledUser = userService.getById(userId);
        if (disabledUser == null) {
            return Result.error("用户不存在");
        }
        if (disabledUser.getRole() == 1) {
            return Result.error("管理员账号不能被停用");
        }
        if (disabledUser.getRole() == 0) {
            return Result.error("该用户账号已处于停用状态，无需重复操作");
        }
        disabledUser.setRole(0);
        userService.updateById(disabledUser);
        return Result.success("用户账号已停用");

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

    //修改昵称
    @PostMapping("/nickname")
    public Result updateNickname(@RequestParam String nicknameDTO) {
        // 1. 校验参数（非空）
        if (StrUtil.isBlank(nicknameDTO)) {
            return Result.error("昵称不能为空");
        }
        userService.updateNickname(nicknameDTO);
        return Result.success("昵称修改成功");

    }


}
