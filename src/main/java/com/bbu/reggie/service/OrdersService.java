package com.bbu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbu.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
