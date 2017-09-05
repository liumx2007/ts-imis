package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.model.TbUser;
import com.trasen.imis.service.TbUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangxiahui on 17/7/26.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class);


    @Autowired
    TbUserService tbUserService;




    //Sign in
    @RequestMapping(value="/signIn",method = RequestMethod.POST)
    @ResponseBody
    public Result signIn(@RequestBody TbUser tbuser){
        Result result=new Result();
        if(tbuser==null||tbuser.getName()==null||tbuser.getPassword()==null){
            result.setMessage("用户名或密码为空");
            result.setSuccess(false);
            return result;
        }
        TbUser user = tbUserService.isLogin(tbuser.getName(),tbuser.getPassword());

        if(user==null){
            result.setMessage("用户名或密码有误");
            result.setSuccess(false);
            return result;
        }else{
            result.setMessage("登录成功");
            result.setSuccess(true);
            result.setObject(user);
            return result;
        }
    }

    @RequestMapping(value="/updatePassword",method = RequestMethod.POST)
    @ResponseBody
    public Result updatePassword(@RequestBody TbUser tbuser){
        Result result=new Result();
        if(tbuser==null||tbuser.getName()==null||tbuser.getPassword()==null||tbuser.getNewPassword()==null){
            result.setMessage("用户名或密码为空");
            result.setSuccess(false);
            return result;
        }
        TbUser user = tbUserService.getUser(tbuser.getName(),tbuser.getPassword());

        if(user==null){
            result.setMessage("用户名或密码有误");
            result.setSuccess(false);
            return result;
        }else{

            int num = tbUserService.updatePassword(tbuser);
            if(num>0){
                result.setMessage("修改密码成功");
                result.setSuccess(true);
            }else{
                result.setMessage("密码未修改");
                result.setSuccess(true);
            }
            result.setObject(user);
            return result;
        }
    }
}
