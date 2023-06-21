package com.bbu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbu.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    @Update("update setmeal set status = #{status} where id = #{id};")
    public int updateStatus(@Param("id") Long id, @Param("status") int status);
}
