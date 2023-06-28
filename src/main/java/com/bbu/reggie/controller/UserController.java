package com.bbu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbu.reggie.common.R;
import com.bbu.reggie.entity.User;
import com.bbu.reggie.service.UserService;
import com.bbu.reggie.utils.SMSUtils;
import com.bbu.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        log.info(user.getPhone());
        String phone = user.getPhone();

        if (phone != null){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code----------->"+code);
//            SMSUtils.sendMessage("瑞吉外卖","SMS_461525158",phone,code);

//            session.setAttribute(phone,code);
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("success");
        }
        return R.error("短信发送失败");
    }

    /**
     * 登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

//        Object sessionCode = session.getAttribute(phone);
        Object Code = redisTemplate.opsForValue().get(phone);

        if (Code != null && Code.equals(code)){

            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(lambdaQueryWrapper);
            if (user == null){
                user =new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("phone");
        return R.success("success");
    }
}
