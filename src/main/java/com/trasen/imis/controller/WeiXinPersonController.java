package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.model.WinXinPerson;
import com.trasen.imis.service.WinXinPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/19
 */
@Controller
@RequestMapping(value="weiXinPerson")
public class WeiXinPersonController {

    @Autowired
    private WinXinPersonService winXinPersonService;

    @RequestMapping(value="/getPersonByOpenId",method = RequestMethod.POST)
    @ResponseBody
    public Result getPersonByOpenId(@RequestParam String openId){
        Result result=new Result();
        if(openId==null){
            result.setSuccess(false);
            result.setMessage("参数失败");
            return result;
        }
        WinXinPerson winXinPerson=winXinPersonService.getPersonByOpenId(openId);
        if(winXinPerson==null){
            result.setSuccess(false);
            result.setMessage("数据查询失败");
            return result;
        }
        result.setSuccess(true);
        result.setObject(winXinPerson);
        return result;
    }

    @RequestMapping(value="/updatePersonAndBasic",method = RequestMethod.POST)
    @ResponseBody
    public Result updatePersonAndBasic(@RequestBody WinXinPerson winXinPerson){
        Result result=new Result();
        if(winXinPerson==null){
            result.setSuccess(false);
            result.setMessage("参数失败");
            return result;
        }
        int wxUpdate=winXinPersonService.updatePersonAndBasic(winXinPerson);
        if(wxUpdate==0){
            result.setSuccess(true);
            result.setMessage("无数据更新");
            return result;
        }
        result.setSuccess(true);
        result.setMessage("数据更新成功");
        return result;
    }

}
