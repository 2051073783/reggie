package com.bbu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbu.reggie.common.BaseContext;
import com.bbu.reggie.common.R;
import com.bbu.reggie.entity.Orders;
import com.bbu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
//log.info("AddressBookId={}",orders.getAddressBookId());
        ordersService.submit(orders);
        return R.success("success");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        Page<Orders> PageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);

        Page<Orders> ordersPage = ordersService.page(PageInfo, queryWrapper);


        return R.success(ordersPage);
    }
}
