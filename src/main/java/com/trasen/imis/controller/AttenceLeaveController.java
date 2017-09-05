package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.model.AttenceLeave;
import com.trasen.imis.service.AttenceLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/4
 */
@Controller
@RequestMapping(value="/attenceLeave")
public class AttenceLeaveController {

    @Autowired
    private AttenceLeaveService attenceLeaveService;

    @RequestMapping(value="/insertAttenceLeaveList",method = RequestMethod.POST)
    @ResponseBody
    public Result insertAttenceLeaveList(@RequestBody List<AttenceLeave> attenceLeaveList){
        Result result=new Result();
        int attenInt=attenceLeaveService.insertAttenceLeaveList(attenceLeaveList);
        if(attenInt>0){
            result.setSuccess(true);
            result.setMessage("维护成功");
        }else{
            result.setSuccess(true);
            result.setMessage("更新数据失败");
        }
        return result;
    }
}
