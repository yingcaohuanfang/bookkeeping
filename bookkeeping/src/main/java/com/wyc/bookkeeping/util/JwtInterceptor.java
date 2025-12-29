package com.wyc.bookkeeping.util;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wyc.bookkeeping.entity.User;
import com.wyc.bookkeeping.exception.ServiceException;
import com.wyc.bookkeeping.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * @author 王亚川
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String requestURI = request.getRequestURI();
        // 判断是否为图片下载路径
        if (requestURI.startsWith("/file/download/")) {
            return true;
        }


        // 1. 从请求头或参数获取 Token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }

        // 2. Token 为空直接拒绝
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "请登录（Token 为空）");
        }

        // 3. 解析 Token 中的 userId（从 audience 字段获取）
        String userIdStr;
        try {
            List<String> audience = JWT.decode(token).getAudience();
            if (audience == null || audience.isEmpty()) {
                throw new ServiceException("401", "Token 格式错误（未包含 userId）");
            }
            userIdStr = audience.get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException("401", "Token 解析失败，请重新登录");
        }

        // 4. 验证 userId 有效性（查询数据库）
        Long userId;
        try {
            userId = Long.valueOf(userIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("401", "userId 格式错误（非数字）");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("401", "用户不存在，请重新登录");
        }

        // 5. 验证 Token 签名（使用固定密钥）
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JwtUtil.SECRET_KEY)).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException("401", "Token 无效或已过期，请重新登录");
        }

        // 6. 认证通过，放行请求
        return true;
    }
}