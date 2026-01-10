package com.wyc.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyc.bookkeeping.entity.Bills;
import com.wyc.bookkeeping.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 王亚川
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> selectLists();

    List<Bills> selectByIds(Long id);
}
