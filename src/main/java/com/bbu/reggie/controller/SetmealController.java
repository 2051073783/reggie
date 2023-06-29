package com.bbu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbu.reggie.Dto.DishDto;
import com.bbu.reggie.Dto.SetmealDto;
import com.bbu.reggie.common.R;
import com.bbu.reggie.entity.Dish;
import com.bbu.reggie.entity.DishFlavor;
import com.bbu.reggie.entity.Setmeal;
import com.bbu.reggie.entity.SetmealDish;
import com.bbu.reggie.service.CategoryService;
import com.bbu.reggie.service.SetmealDishService;
import com.bbu.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmeal",allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        }
        return R.success("success");
    }

    /**
     * 分页查询，跟DishController里的分页类似，这里使用的是for遍历
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        Page<SetmealDto> dtoPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, lambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(record,setmealDto);//把record里的数据放到new出来的对象

            Long categoryId = record.getCategoryId();//分类id
            //判空操作，防止空指针
            if (categoryId != null){
                String categoryName = categoryService.getById(categoryId).getName();
                setmealDto.setCategoryName(categoryName);
            }
            list.add(setmealDto);
        }
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除操作
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmeal",allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("success");
    }

    /**
     * 停售和起售
     * @param status
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmeal",allEntries = true)
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable int status,Long[]  ids){
        for (Long id : ids) {
            setmealService.updateStatus(id,status);
        }
        return R.success("success");
    }

    @Cacheable(value = "setmeal",key = "#setmeal.categoryId + '_' + #setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
