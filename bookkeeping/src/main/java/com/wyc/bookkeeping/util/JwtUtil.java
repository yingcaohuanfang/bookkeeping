package com.wyc.bookkeeping.util;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wyc.bookkeeping.entity.User;
import com.wyc.bookkeeping.exception.ServiceException;
import com.wyc.bookkeeping.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

/**
 * @author 王亚川
 */
@Component
public class JwtUtil {
    // 固定签名密钥（建议从配置文件读取，此处简化为常量）
    public static final String SECRET_KEY = "aBc123!@#xyz789$%^pqrs456&*()tuvw";
    // Token 过期时间：30分钟（单位：毫秒）
    private static final long EXPIRE_TIME = 360 * 60 * 1000;

    @Resource
    private UserMapper userMapper;

    /**
     * 生成 Token（userId 存入 JWT 的 audience 字段）
     * @param userId 用户ID（数据库中 user_id，bigint 类型）
     * @return 生成的 JWT Token
     */
    public static String generateToken(Long userId) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return JWT.create()
                .withAudience(String.valueOf(userId))
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    /**
     * 获取当前登录用户（从 Token 解析 userId 并查询数据库）
     * @return 当前登录用户信息（User 对象）
     * @throws ServiceException 若 Token 无效、过期或用户不存在，抛出业务异常（401）
     */
    public User getCurrentUser() {
        try {
            // 1. 获取当前请求上下文
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new ServiceException("401", "未获取到请求上下文，请确保请求已进入 Spring 容器");
            }
            HttpServletRequest request = attributes.getRequest();

            // 2. 从请求头获取 Token
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                throw new ServiceException("401", "请登录（Token 为空）");
            }

            // 3. 验证 Token 签名和过期时间（关键：使用固定密钥验证）
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT decodedJWT;
            try {
                decodedJWT = verifier.verify(token); // 验证失败会抛出 JWTVerificationException
            } catch (JWTVerificationException e) {
                throw new ServiceException("401", "Token 无效或已过期，请重新登录");
            }

            // 4. 从 audience 字段解析 userId（与 generateToken 对应）
            List<String> audience = decodedJWT.getAudience();
            if (audience == null || audience.isEmpty()) {
                throw new ServiceException("401", "Token 格式错误（未包含 userId）");
            }
            Long userId;
            try {
                userId = Long.valueOf(audience.get(0)); // 必须用 Long（匹配数据库 bigint 类型）
            } catch (NumberFormatException e) {
                throw new ServiceException("401", "Token 中 userId 格式错误（非数字）");
            }

            // 5. 查询数据库获取用户信息
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException("401", "用户不存在（userId=" + userId + "）");
            }
            return user;

        } catch (ServiceException e) {
            // 抛出已定义的业务异常（如 401）
            throw e;
        } catch (Exception e) {
            // 其他未知异常（如数据库异常）
            throw new ServiceException("500", "获取当前用户失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户的 ID（简化版，用于仅需 userId 的场景）
     * @return 当前用户 userId（Long 类型）
     * @throws ServiceException 若 Token 无效，抛出业务异常
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();

    }
}