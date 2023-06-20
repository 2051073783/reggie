package com.bbu.reggie.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbu.reggie.common.CustomException;
import com.bbu.reggie.entity.Category;
import com.bbu.reggie.entity.Dish;
import com.bbu.reggie.entity.Setmeal;
import com.bbu.reggie.mapper.CategoryMapper;
import com.bbu.reggie.service.CategoryService;
import com.bbu.reggie.service.DishService;
import com.bbu.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> queryWrapperDish = new LambdaQueryWrapper<>();
        queryWrapperDish.eq(Dish::getCategoryId,ids);
        int count1 = dishService.count(queryWrapperDish);
        if (count1 > 0){
            throw new CustomException("当前分类里还有菜品，不能删除");

        }
        LambdaQueryWrapper<Setmeal> queryWrapperSetmeal = new LambdaQueryWrapper<>();
        queryWrapperSetmeal.eq(Setmeal::getCategoryId,ids);
        int count2 = setmealService.count(queryWrapperSetmeal);
        if (count2 > 0){
            throw new CustomException("当前套餐里还有菜品，不能删除");
        }

        super.removeById(ids);
    }
}
