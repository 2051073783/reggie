package com.bbu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbu.reggie.common.BaseContext;
import com.bbu.reggie.common.R;
import com.bbu.reggie.entity.Orders;
import com.bbu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * 订单功能
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 提交订单功能
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
//log.info("AddressBookId={}",orders.getAddressBookId());
        ordersService.submit(orders);
        return R.success("success");
    }

    /**
     * 客户端查看订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        Page<Orders> PageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);

        Page<Orders> ordersPage = ordersService.page(PageInfo, queryWrapper);


        return R.success(ordersPage);
    }

    /**
     * 管理员查看订单
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, Timestamp beginTime, Timestamp endTime){
//        log.info("number={},begin={},end={}",number,beginTime,endTime);
        Page<Orders> PageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null,Orders::getNumber,number);
        queryWrapper.between(beginTime != null && endTime != null,Orders::getOrderTime,beginTime,endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        Page<Orders> ordersPage = ordersService.page(PageInfo, queryWrapper);


        return R.success(ordersPage);
    }

    /**
     * 管理员修改状态 （订单状态 1待付款，2待派送，3已派送，4已完成，5已取消）
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> put(@RequestBody Orders orders){
        LambdaUpdateWrapper<Orders> qw = new LambdaUpdateWrapper<>();
        qw.eq(Orders::getId,orders.getId());
        qw.set(Orders::getStatus,orders.getStatus());

        ordersService.update(qw);
        return R.success("success");

    }
}
