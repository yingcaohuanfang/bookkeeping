package com.wyc.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyc.bookkeeping.entity.Bills;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 王亚川
 */
@Mapper
public interface BillsMapper extends BaseMapper<Bills> {
    void saveBills(Bills bills);

    Map index();


    List<Map<String, Object>> monthBills();


    List<Bills> selectByIds(@Param("userId") Long userId);

}
