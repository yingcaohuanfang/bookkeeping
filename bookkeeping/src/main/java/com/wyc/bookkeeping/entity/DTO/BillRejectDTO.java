package com.wyc.bookkeeping.entity.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 账单驳回入参DTO
 */
@Data
public class BillRejectDTO {
    // @NotBlank：校验字符串 非null、非空、非空白字符，刚好适配驳回理由
    @NotBlank(message = "驳回理由不能为空")
    private String dismissed;
}
