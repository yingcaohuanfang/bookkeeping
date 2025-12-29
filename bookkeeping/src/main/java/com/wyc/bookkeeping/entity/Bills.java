package com.wyc.bookkeeping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王亚川
 */
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@Data
@TableName("bills") // 关联数据库表名
public class Bills {
    @TableId(type = IdType.AUTO) // 自增主键
    private Long id; // 账单ID

    private BigDecimal amount; // 金额

    private String incomeexpenditure; // 收入/支出

    @TableField("createTime")
    private String createTime; // 创建时间（日期）
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("updatedTime")
    private LocalDateTime updatedTime; // 更新时间

    private Long typeid; // 种类ID

    private String invoices; // 发票

    @TableField("userid")
    private Long userId;

    private String remark; // 备注

    private Integer status; // 账单状态：0-待审核，1-审核通过，2-已驳回（默认0）
}