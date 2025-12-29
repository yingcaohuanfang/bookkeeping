package com.wyc.bookkeeping.entity.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author 王亚川
 */
@Data
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    public @NotBlank(message = "用户名不能为空") @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "用户名不能为空") @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间") String username) {
        this.username = username;
    }

    public @NotBlank(message = "密码不能为空") @Size(min = 6, max = 20, message = "密码长度必须在6-20之间") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "密码不能为空") @Size(min = 6, max = 20, message = "密码长度必须在6-20之间") String password) {
        this.password = password;
    }
}