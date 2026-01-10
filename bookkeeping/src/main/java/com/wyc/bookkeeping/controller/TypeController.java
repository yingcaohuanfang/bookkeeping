package com.wyc.bookkeeping.controller;

import com.wyc.bookkeeping.entity.Type;
import com.wyc.bookkeeping.service.TypeService;
import com.wyc.bookkeeping.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王亚川
 */
@RestController
@CrossOrigin
public class TypeController {

    @Autowired
    private TypeService typeService;

    //新增种类
    @PostMapping("/addType")
    public Result addType(@RequestBody Type bype) {
        typeService.save(bype);
        return Result.success("新增成功");
    }
    //获取所有种类
    @GetMapping("/getAllType")
    public Result getAllType() {
        return Result.success(typeService.list());
    }

    //根据ID获取种类
    @GetMapping("/getTypeById/{id}")
    public Result getTypeById(@PathVariable Long id) {
        return Result.success(typeService.getById(id));
    }
}
