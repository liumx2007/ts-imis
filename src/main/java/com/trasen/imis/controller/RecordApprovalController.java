package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.model.TbRankCheck;
import com.trasen.imis.service.RecordApprovalService;
import org.apache.commons.collections.MapUtils;
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
 * @date 2017/9/5
 */
@Controller
@RequestMapping(value="/recordApp")
public class RecordApprovalController {

    @Autowired
    private RecordApprovalService recordApprovalService;

    @ResponseBody
    @RequestMapping(value="/getRecordApprovalList",method = RequestMethod.POST)
    public String getRecordApprovalList(@RequestBody Map<String,String> param) throws JsonProcessingException {
        Integer pageNo = MapUtils.getInteger(param, "pageNo");
        Integer pageSize = MapUtils.getInteger(param, "pageSize");
        Page page = new Page(pageNo,pageSize);
        page.setSord("asc");
        page.setSidx("rjr.created");
        List<TbJfRecord> tbJfRecordList= recordApprovalService.getRecordApprovalList(param,page);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(tbJfRecordList);
        String json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+ ",\"code\": "+1+"}";
        return json;

    }

    @ResponseBody
    @RequestMapping(value="/agreeUpdateJfRecrod",method = RequestMethod.POST)
    public Result agreeUpdateJfRecrod(@RequestBody List<TbJfRecord> tbJfRecordList){
        Result result=new Result();
        recordApprovalService.updateJfRecrod(tbJfRecordList, AppCons.RECORDAPP_AGREE);
        result.setMessage("更新成功");
        result.setSuccess(true);
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/disareeUpdateJfRecord",method = RequestMethod.POST)
    public Result disareeUpdateJfRecord(@RequestBody List<TbJfRecord> tbJfRecordList){
        Result result=new Result();
        recordApprovalService.updateJfRecrod(tbJfRecordList, AppCons.PROMOTION_DISAGREE);
        result.setMessage("更新成功");
        result.setSuccess(true);
        return result;
    }
}
