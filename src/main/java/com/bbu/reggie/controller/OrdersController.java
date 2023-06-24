package com.bbu.reggie.controller;

import com.bbu.reggie.common.R;
import com.bbu.reggie.entity.Orders;
import com.bbu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
