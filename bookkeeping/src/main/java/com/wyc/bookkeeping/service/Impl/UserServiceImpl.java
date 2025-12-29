package com.wyc.bookkeeping.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyc.bookkeeping.entity.DTO.LoginDTO;
import com.wyc.bookkeeping.entity.DTO.LoginResponseDTO;
import com.wyc.bookkeeping.entity.DTO.RegisterDTO;
import com.wyc.bookkeeping.entity.User;
import com.wyc.bookkeeping.exception.ServiceException;
import com.wyc.bookkeeping.mapper.UserMapper;
import com.wyc.bookkeeping.service.UserService;
import com.wyc.bookkeeping.util.JwtUtil;
import com.wyc.bookkeeping.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 王亚川
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtil jwtUtil;

    // 注册
//    @Override
//    public Object register(RegisterDTO registerDTO) {
//        String username = registerDTO.getUsername();
//        String password = registerDTO.getPassword();
//        if (username == null || username.trim().isEmpty()
//                || password == null || password.trim().isEmpty()) {
//            throw new ServiceException("用户名或密码不能为空");
//        }
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        User user = this.getOne(queryWrapper);
//        if (user != null) {
//            throw new ServiceException("用户名已存在");
//        }
//        user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setRole(1);
//        user.setCreateTime(LocalDateTime.now());
//        this.save(user);
//        return Result.success("注册成功");
//    }

    @Override
    public Object register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return Result.error("用户名或密码不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);
        if (user != null) {
            return Result.error("用户名已存在");
        }
        user = new User();
        user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(1);
        user.setNickname(username);
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
        return Result.success("注册成功");

    }

    // 登录
    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new ServiceException("用户名或密码错误");
        }
        boolean passwordMatch = loginDTO.getPassword().equals(user.getPassword());
        if (!passwordMatch) {
            throw new ServiceException("用户名或密码错误");
        }
        String token = JwtUtil.generateToken(user.getUserId());

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(token);
        responseDTO.setUsername(user.getUsername());
        responseDTO.setRole(1);
        return responseDTO;

    }

    // 修改密码
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        //获取当前用户信息
        User user = jwtUtil.getCurrentUser();
        //验证旧密码
        if (!user.getPassword().equals(oldPassword)) {
            throw new ServiceException("旧密码错误");
        }
        //更新密码
        user.setPassword(newPassword);
        //保存
        int rows = userMapper.updateById(user);
        if (rows == 0) {
            throw new ServiceException("密码更新失败，请重试");
        }

    }

}
