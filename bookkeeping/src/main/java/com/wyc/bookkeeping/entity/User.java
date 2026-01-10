package com.wyc.bookkeeping.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 王亚川
 */
@Setter
@Getter
@Data
public class User {
    @TableId(value = "userid", type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private int role;

    @TableField(value = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
