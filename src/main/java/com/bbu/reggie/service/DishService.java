package com.bbu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbu.reggie.Dto.DishDto;
import com.bbu.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
}
