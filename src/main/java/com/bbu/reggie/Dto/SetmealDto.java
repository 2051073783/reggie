package com.bbu.reggie.Dto;

import com.bbu.reggie.entity.Setmeal;
import com.bbu.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
