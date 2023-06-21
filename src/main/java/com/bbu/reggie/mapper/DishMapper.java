package com.bbu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbu.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    @Update("update dish set status = #{status} where id = #{ids};")
    public int updateStatus(@Param("ids") Long ids,@Param("status") int status);
}
