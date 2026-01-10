package com.wyc.bookkeeping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.DTO.BillRejectDTO;
import com.wyc.bookkeeping.entity.DTO.BillsDTO;
import com.wyc.bookkeeping.entity.VO.BillsVO;
import com.wyc.bookkeeping.service.BillsService;
import com.wyc.bookkeeping.util.JwtUtil;
import com.wyc.bookkeeping.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author 王亚川
 */
@RestController
@CrossOrigin
public class BillsController {

    @Resource
    private BillsService billsService;

    @Resource
    private JwtUtil jwtUtil;


    //添加账单
    @PostMapping("/addBills")
    public Result addBills(@RequestBody BillsDTO billsDTO) {
        return Result.success(billsService.saveBills(billsDTO));
    }

    //多条件查询账单
    @PostMapping("/queryBills")
    public Result queryBills(@RequestBody BillsVO billsVO) {
        return Result.success(billsService.queryBills(billsVO));
    }

    //账单审核通过
    @PutMapping("/approveBills/{id}")
    public Result approveBills(@PathVariable Long id) {
        Bills bills = new Bills();
        bills.setId(id);
        bills.setStatus(1);
        billsService.updateById(bills);
        return Result.success("审核通过");
    }

    //获取当前登录用户的所有订单
    @GetMapping("/getBills")
    public Result getBills() {
        Long userId = jwtUtil.getCurrentUserId();
        return Result.success(billsService.getBills(userId));
    }


    //账单审核不通过
    @PutMapping("/rejectBills/{id}")
    public Result rejectBills(@PathVariable Long id, @RequestBody BillRejectDTO billRejectDTO) {
        try {
            // 1. 基础参数校验：ID不能为空、驳回原因不能为空/空串
            if (id == null || id <= 0) {
                return Result.error("单据ID不能为空，且必须为正整数");
            }
            String dismissReason = billRejectDTO.getDismissed();
            if (dismissReason == null || dismissReason.trim().isEmpty()) {
                return Result.error("驳回原因不能为空，请填写驳回理由");
            }

            // 2. 业务校验：查询该单据是否存在
            Bills bills = billsService.getById(id);
            if (bills == null) {
                return Result.error("驳回失败，该单据不存在或已被删除");
            }

            // 3. 业务校验：单据状态是否允许驳回（状态2=已驳回，状态1=审核通过，这两种状态禁止重复驳回）
            Integer billStatus = bills.getStatus();
            if (2 == billStatus) {
                return Result.error("该单据已被驳回，请勿重复操作");
            }
            if (1 == billStatus) {
                return Result.error("该单据已审核通过，无法驳回");
            }

            // 4. Mybatis-Plus 正确的更新写法（无空指针风险，官方推荐）
            LambdaUpdateWrapper<Bills> updateWrapper = Wrappers.<Bills>lambdaUpdate()
                    .eq(Bills::getId, id)
                    .set(Bills::getDismissed, dismissReason.trim())
                    // 2=审核不通过/驳回状态
                    .set(Bills::getStatus, 2);

            // 执行更新并判断结果：update方法返回 boolean
            boolean updateSuccess = billsService.update(updateWrapper);
            if (updateSuccess) {
                return Result.success("单据审核驳回成功");
            } else {
                return Result.error("单据驳回失败，更新数据异常");
            }

        } catch (Exception e) {
            // 全局异常捕获：打印异常日志+返回友好提示
            e.printStackTrace(); // 生产环境建议替换为logback/log4j2的日志打印
            return Result.error("单据驳回失败，服务端异常，请联系管理员");
        }
    }


    //获取驳回账单状态为2的账单
    @GetMapping("/getRejectBills")
    public Result getRejectBills() {
        return Result.success(billsService.getRejectBills());
    }

    //首页
    @GetMapping("/index")
    public Result index() {
        return Result.success(billsService.index());
    }

    //月账单折线图
    @GetMapping("/monthBills")
    public Result monthBills() {
        return Result.success(billsService.monthBills());
    }



    /**
     * 账单多条件分页查询（全部账单+筛选）
     *
     * @param pageNum   页码，默认1
     * @param pageSize  每页条数，默认10
     * @param status    状态：0=待审核 1=已通过 2=已驳回 【不传=查全部】
     * @param type      账单种类：比如差旅费/办公费 【不传=查全部】
     * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss 【不传=不限制】
     * @param endTime   结束时间 yyyy-MM-dd HH:mm:ss 【不传=不限制】
     * @return 分页结果（总数+当前页数据）
     */
    @GetMapping("/bills/history")
    public Result getBillHistory(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDate startTime,
            @RequestParam(required = false) LocalDate endTime
    ) {
        // 1. 构建分页对象
        Page<Bills> page = new Page<>(pageNum, pageSize);
        // 2. 构建多条件查询器，动态拼接筛选条件
        LambdaQueryWrapper<Bills> queryWrapper = Wrappers.<Bills>lambdaQuery()
                // 状态筛选：传了状态就筛选，不传查全部
                .eq(status != null, Bills::getStatus, status)
                // 种类筛选：传了种类就筛选，不传查全部
                .eq(type != null && !type.trim().isEmpty(), Bills::getTypeid, type.trim())
                // 时间筛选：大于等于开始时间
                .ge(startTime != null, Bills::getCreateTime, startTime)
                // 时间筛选：小于等于结束时间
                .le(endTime != null, Bills::getCreateTime, endTime)
                // 排序：最新提交的账单排在前面
                .orderByDesc(Bills::getCreateTime);
        // 3. 执行分页查询
        Page<Bills> billPage = billsService.page(page, queryWrapper);
        // 4. 返回分页结果
        return Result.success(billPage);
    }
    @PostMapping("/adminqueryBills")
    public Result adminqueryBills(@RequestBody BillsVO billsVO) {
        return Result.success(billsService.adminqueryBills(billsVO));
    }

}
