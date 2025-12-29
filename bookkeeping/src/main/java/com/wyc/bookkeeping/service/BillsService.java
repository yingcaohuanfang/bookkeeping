package com.wyc.bookkeeping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.DTO.BillsDTO;

/**
 * @author 王亚川
 */
public interface BillsService extends IService<Bills> {
    Object saveBills(BillsDTO billsDTO);
}
