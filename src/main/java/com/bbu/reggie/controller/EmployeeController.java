package com.bbu.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbu.reggie.common.R;
import com.bbu.reggie.entity.Employee;
import com.bbu.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        1.加密密码
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        2.根据name查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);


        if (one == null){
            return R.error("用户不存在，请先注册");
        }


        if (!one.getPassword().equals(password)){
            return R.error("密码错误");
        }

        if (one.getStatus().equals(0)){
            return R.error("账号被禁用");
        }

//        6.登录成功把用户id存到sesson中
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success(null);

    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success(null);
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> updateStatus(HttpServletRequest request,@RequestBody Employee employee){
        Long empid = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empid);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        if (emp != null){
            return R.success(emp);
        }
        return R.error("");
    }


}
