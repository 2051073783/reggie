package com.bbu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbu.reggie.common.CustomException;
import com.bbu.reggie.entity.Setmeal;
import com.bbu.reggie.entity.SetmealDish;
import com.bbu.reggie.mapper.SetmealMapper;
import com.bbu.reggie.service.SetmealDishService;
import com.bbu.reggie.service.SetmealService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealMapper setmealMapper;
    @Transactional
    @Override
    public void deleteWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);

        int count = count(setmealLambdaQueryWrapper);
        if (count > 0){
            throw new CustomException("套餐正在售卖中，停售后再删除");
        }
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(setmealDishLambdaQueryWrapper);

    }

    @Override
    public void updateStatus(Long id, int status) {
        setmealMapper.updateStatus(id,status);
    }
}