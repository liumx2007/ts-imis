package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.source.tree.Tree;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.model.TbRankCheck;
import com.trasen.imis.model.TbTree;
import com.trasen.imis.service.PromotionAppService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/4
 */
@Controller
@RequestMapping(value="/promotionApp")
public class PromotionAppController {

    @Autowired
    private PromotionAppService promotionAppService;

    @RequestMapping(value="/getCompanyList",method = RequestMethod.POST)
    @ResponseBody
    public Result getCompanyList(){
        Result result=new Result();
        List<TbTree> companyList=promotionAppService.getCompanyList();
        if(companyList==null){
            result.setSuccess(false);
            result.setMessage("数据为空");
        }else{
            result.setSuccess(true);
            result.setObject(companyList);
        }
        return  result;
    }

    @RequestMapping(value="/{pkid}/getDeptList",method = RequestMethod.POST)
    @ResponseBody
    public Result getDeptList(@PathVariable String pkid){
        Result result=new Result();
        List<TbTree> tbTreeList=promotionAppService.getDeptList(pkid);
        if(tbTreeList!=null){
            result.setObject(tbTreeList);
            result.setSuccess(true);
        }else{
            result.setMessage("查询数据为空");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/getRankCheckList",method = RequestMethod.POST)
    @ResponseBody
    public String getRankCheckList(@RequestBody Map<String,String> param) throws JsonProcessingException {
        Integer pageNo = MapUtils.getInteger(param, "pageNo");
        Integer pageSize = MapUtils.getInteger(param, "pageSize");
        Page page = new Page(pageNo,pageSize);
        page.setSord("asc");
        page.setSidx("rch.created");
        List<TbRankCheck> tbRankCheckList= promotionAppService.getRankCheckList(param,page);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(tbRankCheckList);
        String json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+ ",\"code\": "+1+"}";
        return json;

    }

    @RequestMapping(value="/agreeUpdateRank",method = RequestMethod.POST)
    @ResponseBody
    public Result agreeUpdateRank(@RequestBody List<TbRankCheck> tbRankCheckList){
        Result result=new Result();
        int updateCount=promotionAppService.updateRankCkeck(tbRankCheckList, AppCons.PROMOTION_AGREE);
        if(updateCount>0){
            result.setMessage("晋级成功");
            result.setSuccess(true);
        }else{
            result.setMessage("晋级失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/disagreeUpdateRank",method = RequestMethod.POST)
    @ResponseBody
    public Result disagreeUpdateRank(@RequestBody List<TbRankCheck> tbRankCheckList){
        Result result=new Result();
        int updateCount=promotionAppService.updateRankCkeck(tbRankCheckList, AppCons.PROMOTION_DISAGREE);
        if(updateCount>0){
            result.setSuccess(true);
            result.setMessage("修改成功");
        }else{
            result.setMessage("修改失败");
            result.setSuccess(false);
        }
        return result;
    }


}
