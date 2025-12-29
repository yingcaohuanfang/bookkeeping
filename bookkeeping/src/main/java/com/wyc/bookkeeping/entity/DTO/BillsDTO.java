package com.wyc.bookkeeping.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * @author 王亚川
 */
import java.math.BigDecimal;

@Data
public class BillsDTO {
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须为正数")
    private BigDecimal amount; // 金额

    @NotNull(message = "收支类型不能为空")
    @Pattern(regexp = "^(income|expenditure)$", message = "收支类型必须为'income'或'expenditure'")
    private String incomeexpenditure; // 收入/支出


    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createTime; // 日期（创建时间）
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedTime; // 更新时间（可为空，默认当前时间）

    @NotNull(message = "账单种类ID不能为空")
    private Long typeid; // 种类ID

    private String invoices; // 发票（可选，如发票号码或URL）

    @NotNull(message = "人员ID不能为空")
    private Long userId;

    @Size(max = 255, message = "备注不能超过255字符")
    private String remark; // 备注（可选）
}