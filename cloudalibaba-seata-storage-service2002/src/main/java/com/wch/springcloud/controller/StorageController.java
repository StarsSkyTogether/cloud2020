package com.wch.springcloud.controller;

import com.wch.springcloud.domain.CommonResult;
import com.wch.springcloud.service.StorageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class StorageController {

    @Resource
    private StorageService storageService;

    @RequestMapping("/storage/decrease")
    public CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count){

        storageService.decrease(productId,count);
        return new CommonResult(200,"扣减库存成功！");
    }




}
