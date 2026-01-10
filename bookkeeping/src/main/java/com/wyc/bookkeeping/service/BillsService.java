package com.wyc.bookkeeping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.DTO.BillsDTO;
import com.wyc.bookkeeping.entity.VO.BillsVO;

import java.util.List;

/**
 * @author 王亚川
 */
public interface BillsService extends IService<Bills> {
    Object saveBills(BillsDTO billsDTO);

    Object queryBills(BillsVO billsVO);

    Object getRejectBills();

    Object index();

    Object monthBills();

    List<Bills> getBills(Long userId);

    Object adminqueryBills(BillsVO billsVO);
}
