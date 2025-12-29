package com.wyc.bookkeeping.entity.DTO;



import lombok.Data;

/**
 * @author 王亚川
 */
@Data
public class LoginResponseDTO {
    private String token;         // JWT令牌
    private String username;      // 用户名
    private int role;          // 角色

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
