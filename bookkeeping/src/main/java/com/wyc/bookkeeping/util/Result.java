package com.wyc.bookkeeping.util;

import lombok.Data;


/**
 * @author 王亚川
 */
@Data
public class Result {
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_AUTH_ERROR = "401";
    public static final String CODE_SYS_ERROR = "500";

    // Getter（按需添加）
    private final String code;
    private final String Message;
    private final Object data;

    // 全参构造
    public Result(String code, String msg, Object data) {
        this.code = code;
        this.Message = msg;
        this.data = data;
    }

    // 快速成功方法
    public static Result success() {
        return new Result(CODE_SUCCESS, "请求成功", null);
    }

    public static Result success(Object data) {
        return new Result(CODE_SUCCESS, "请求成功", data);
    }

    // 快速错误方法
    public static Result error(String msg) {
        return new Result(CODE_SYS_ERROR, msg, null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

}