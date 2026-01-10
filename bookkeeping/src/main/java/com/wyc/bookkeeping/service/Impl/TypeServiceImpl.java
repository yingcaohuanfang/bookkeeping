package com.wyc.bookkeeping.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyc.bookkeeping.entity.Type;
import com.wyc.bookkeeping.mapper.TypeMapper;
import com.wyc.bookkeeping.service.TypeService;
import org.springframework.stereotype.Service;

/**
 * @author 王亚川
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
}
