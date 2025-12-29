package com.wyc.bookkeeping.controller;

import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.DTO.BillsDTO;
import com.wyc.bookkeeping.service.BillsService;
import com.wyc.bookkeeping.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王亚川
 */
@RestController
@CrossOrigin
public class BillsController {

    @Resource
    private BillsService billsService;


    //添加账单
    @PostMapping("/addBills")
    public Result addBills(@RequestBody BillsDTO billsDTO) {
        return Result.success(billsService.saveBills(billsDTO));
    }
}
