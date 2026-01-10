package com.wyc.bookkeeping.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.DTO.BillsDTO;
import com.wyc.bookkeeping.entity.VO.BillsVO;
import com.wyc.bookkeeping.mapper.BillsMapper;
import com.wyc.bookkeeping.service.BillsService;
import com.wyc.bookkeeping.util.JwtUtil;
import com.wyc.bookkeeping.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        bills.setUpdatedTime(LocalDateTime.now());
        bills.setTypeid(billsDTO.getTypeid());
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
                return Result.success("账单新增成功" + bills.getId());
            } else {
                return Result.error("账单保存失败，请重试");
            }
        } catch (Exception e) {
            // 记录异常日志（实际项目中使用日志框架如SLF4J）
            System.err.println("新增账单异常：" + e.getMessage());
            return Result.error("系统异常，账单保存失败");
        }
    }

    @Override
    public Object queryBills(BillsVO billsVO) {   //根据状态，种类，时间查询账单

        // 1. 构建查询条件
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();

        //关键字模糊查询
        if (StringUtils.hasText(billsVO.getKeywords())) {
            queryWrapper.like(Bills::getRemark, billsVO.getKeywords());
        }
        // 2. 添加基础条件
        queryWrapper.eq(Bills::getUserId, jwtUtil.getCurrentUserId());
        // 3. 添加动态条件可以多选也可以单选
        if (billsVO.getStatusList() != null && !billsVO.getStatusList().isEmpty()) {
            queryWrapper.in(Bills::getStatus, billsVO.getStatusList());
        }
        if (billsVO.getTypeIdList() != null && !billsVO.getTypeIdList().isEmpty()) {
            queryWrapper.in(Bills::getTypeid, billsVO.getTypeIdList());
        }
        // 4. 时间范围查询
        if (billsVO.getStartTime() != null && billsVO.getEndTime() != null) {
            queryWrapper.between(Bills::getCreateTime, billsVO.getStartTime(), billsVO.getEndTime());//between  方法是用来查询时间范围
        } else if (billsVO.getStartTime() != null) {
            queryWrapper.ge(Bills::getCreateTime, billsVO.getStartTime());//ge  方法是用来查询时间大于等于
        } else if (billsVO.getEndTime() != null) {
            queryWrapper.le(Bills::getCreateTime, billsVO.getEndTime());//le  方法是用来查询时间小于等于
        }
        // 5. 分页处理
        Page<Bills> page = new Page<>(billsVO.getPageNum(), billsVO.getPageSize());

        // 6. 返回结果
        return billsMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Object getRejectBills() {
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bills::getStatus, 2);
        queryWrapper.eq(Bills::getUserId, jwtUtil.getCurrentUserId());
        return billsMapper.selectList(queryWrapper);

    }

    @Override
    public Object index() {
        return billsMapper.index();
    }

    @Override
    public List<Map<String, Object>> monthBills() {
        return billsMapper.monthBills();
    }

    @Override
    public List<Bills> getBills(Long userId) {
        return billsMapper.selectByIds(userId);
    }

    @Override
    public Object adminqueryBills(BillsVO billsVO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();

        //关键字模糊查询
        if (StringUtils.hasText(billsVO.getKeywords())) {
            queryWrapper.like(Bills::getRemark, billsVO.getKeywords());
        }
        // 2. 添加基础条件
        queryWrapper.eq(Bills::getUserId, jwtUtil.getCurrentUserId());
        // 3. 添加动态条件可以多选也可以单选
        if (billsVO.getStatusList() != null && !billsVO.getStatusList().isEmpty()) {
            queryWrapper.in(Bills::getStatus, billsVO.getStatusList());
        }
        if (billsVO.getTypeIdList() != null && !billsVO.getTypeIdList().isEmpty()) {
            queryWrapper.in(Bills::getTypeid, billsVO.getTypeIdList());
        }
        // 4. 时间范围查询
        if (billsVO.getStartTime() != null && billsVO.getEndTime() != null) {
            queryWrapper.between(Bills::getCreateTime, billsVO.getStartTime(), billsVO.getEndTime());//between  方法是用来查询时间范围
        } else if (billsVO.getStartTime() != null) {
            queryWrapper.ge(Bills::getCreateTime, billsVO.getStartTime());//ge  方法是用来查询时间大于等于
        } else if (billsVO.getEndTime() != null) {
            queryWrapper.le(Bills::getCreateTime, billsVO.getEndTime());//le  方法是用来查询时间小于等于
        }
        // 5. 分页处理
        Page<Bills> page = new Page<>(billsVO.getPageNum(), billsVO.getPageSize());
        Page<Bills> billsPage = billsMapper.selectPage(page, queryWrapper);

        // 6. 返回结果
        return Result.success(billsPage);
    }
}
