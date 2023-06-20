package com.bbu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbu.reggie.entity.Dish;
import com.bbu.reggie.mapper.DishMapper;
import com.bbu.reggie.service.DishService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
