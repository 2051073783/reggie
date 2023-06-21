package com.bbu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbu.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void deleteWithDish(List<Long> ids);

    void updateStatus(Long id, int status);
}
