package com.wyc.bookkeeping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyc.bookkeeping.entity.DTO.LoginDTO;
import com.wyc.bookkeeping.entity.DTO.LoginResponseDTO;
import com.wyc.bookkeeping.entity.DTO.RegisterDTO;
import com.wyc.bookkeeping.entity.User;

/**
 * @author 王亚川
 */
public interface UserService extends IService<User> {
    Object register(RegisterDTO registerDTO);

    LoginResponseDTO login(LoginDTO loginDTO);

    void updatePassword(String oldPassword, String newPassword);


}
