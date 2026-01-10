package com.wyc.bookkeeping.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 王亚川
 */
@Data
public class BillsVO {

    private String Keywords;
    private Integer pageNum;
    private Integer pageSize;
    private List<Integer> statusList;
    private List<Long> typeIdList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}