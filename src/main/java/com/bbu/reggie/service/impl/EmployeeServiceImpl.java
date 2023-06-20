package com.bbu.reggie.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbu.reggie.entity.Employee;
import com.bbu.reggie.mapper.EmployeeMapper;
import com.bbu.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
