package com.bbu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbu.reggie.entity.Setmeal;
import com.bbu.reggie.mapper.SetmealMapper;
import com.bbu.reggie.service.SetmealService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}