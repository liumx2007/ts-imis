package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbJfRank;
import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.model.TbRankCheck;
import com.trasen.imis.service.WeiXinPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/29
 */
@Controller
@RequestMapping(value="/promotion")
public class WeiXinPromotionController {

    @Autowired
    private WeiXinPromotionService weiXinPromotionService;

    @RequestMapping(value="/getJfPersonByopenId",method = RequestMethod.POST)
    @ResponseBody
    public Result getJfPersonByopenId(@RequestBody Map<String,String> param){
        Result result=new Result();
        if(param.isEmpty()||param.get("openId")==null){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }
       TbJfPerson tbJfPerson= weiXinPromotionService.getJfPersonByopenId(param.get("openId"));
        if(tbJfPerson==null){
            result.setSuccess(false);
            result.setMessage("查询查询失败");
        }else{
            result.setSuccess(true);
            result.setMessage("查询数据成功");
            result.setObject(tbJfPerson);
        }

        return result;
    }

    @RequestMapping(value="/savaCheck",method = RequestMethod.POST)
    @ResponseBody
    public Result savaCheck(@RequestBody TbRankCheck tbRankCheck){
        Result result=new Result();
        if(tbRankCheck==null){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }
        weiXinPromotionService.savaCheck(tbRankCheck);
        result.setMessage("保存成功");
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value="/getJfRecordByOpendId",method = RequestMethod.POST)
    @ResponseBody
    public Result getJfRecordByOpendId(@RequestBody Map<String,String> param){
        Result result=new Result();
        if(param.isEmpty()||param.get("openId")==null){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }
        List<TbJfRecord> tbJfRecordList=weiXinPromotionService.getJfRecordByOpendId(param.get("openId"));
        if(tbJfRecordList==null){
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }else{

            result.setMessage("数据查询成功");
            result.setSuccess(true);
            result.setObject(tbJfRecordList);

        }
        return result;
    }

    @RequestMapping(value="/getPersonByopenId",method = RequestMethod.POST)
    @ResponseBody
    public Result getPersonByopenId(@RequestBody Map<String,String> param){
        Result result=new Result();
        if(param.isEmpty()||param.get("openId")==null){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }
        TbJfPerson tbJfPerson=weiXinPromotionService.getPersonByopenId(param.get("openId"));
        if(tbJfPerson==null){
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }else{
            result.setObject(tbJfPerson);
            result.setSuccess(true);
        }
        return result;
    }
}
