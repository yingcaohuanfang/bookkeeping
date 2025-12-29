package com.wyc.bookkeeping.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.DTO.BillsDTO;
import com.wyc.bookkeeping.mapper.BillsMapper;
import com.wyc.bookkeeping.service.BillsService;
import com.wyc.bookkeeping.util.JwtUtil;
import com.wyc.bookkeeping.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author 王亚川
 */
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements BillsService {

    @Autowired
    private BillsMapper billsMapper;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Object saveBills(BillsDTO billsDTO) {
        //新增账单
        Bills bills = new Bills();
        bills.setAmount(billsDTO.getAmount());
        bills.setIncomeexpenditure(billsDTO.getIncomeexpenditure());
        bills.setCreateTime(billsDTO.getCreateTime());
        // 补充默认更新时间
        bills.setUpdatedTime(billsDTO.getUpdatedTime() != null ? billsDTO.getUpdatedTime() : LocalDateTime.now());
        bills.setTypeid(billsDTO.getTypeid());
        bills.setInvoices(billsDTO.getInvoices());
        bills.setRemark(billsDTO.getRemark());
        bills.setUserId(jwtUtil.getCurrentUserId());
        //拍照上传发票
        try {
            // 1. 获取原始URL列表
            List<String> imageUrls = Collections.singletonList(billsDTO.getInvoices());
            // 3. 用逗号拼接URL列表，生成纯文本
            String imageUrlsText = String.join(",", imageUrls);
            bills.setInvoices(imageUrlsText);
        } catch (Exception e) {
            throw new RuntimeException("图片URL处理失败：" + e.getMessage());
        }
        bills.setStatus(0);
        try {
            boolean saved = billsMapper.insert(bills) > 0;
            if (saved) {
                return Result.success("账单新增成功"+bills.getId());
            } else {
                return Result.error("账单保存失败，请重试");
            }
        } catch (Exception e) {
            // 记录异常日志（实际项目中使用日志框架如SLF4J）
            System.err.println("新增账单异常：" + e.getMessage());
            return Result.error("系统异常，账单保存失败");
        }
    }
}
