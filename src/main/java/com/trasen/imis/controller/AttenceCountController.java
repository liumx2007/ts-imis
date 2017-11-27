package com.trasen.imis.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.AttenceVo;
import com.trasen.imis.model.TbAttenceCount;
import com.trasen.imis.service.AttenceCountService;
import com.trasen.imis.service.AttenceService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by zhangxiahui on 17/7/7.
 */
@Controller
@RequestMapping("/count")
public class AttenceCountController {

    private static final Logger logger = Logger.getLogger(AttenceCountController.class);

    @Autowired
    AttenceCountService attenceCountService;

    @Autowired
    AttenceService attenceService;

    @ResponseBody
    @RequestMapping(value = "/attCountData", method = RequestMethod.POST)
    public Map<String,Object> attCountData(@RequestBody Map<String, Object> params){
        //结果集
        Map<String,Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo",1);
        result.put("pageSize", 0);
        result.put("totalCount",0);
        result.put("totalPages",1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String countDate = MapUtils.getString(params, "countDate");
            String name = MapUtils.getString(params, "name");
            String tagName = MapUtils.getString(params, "tagName");
            List selectCx= (List) params.get("selctCx");

            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);


            TbAttenceCount count = new TbAttenceCount();
            if(!StringUtil.isEmpty(name)){
                count.setName(name);
            }
            if(!StringUtil.isEmpty(tagName)){
                count.setTagName(tagName);
                String tagId = attenceService.getDeptCode(tagName);
                if(!StringUtil.isEmpty(tagId)){
                    count.setTagId(tagId);
                }
            }
            if(!StringUtil.isEmpty(countDate)){
                count.setCountDate(countDate);
            }
            if(selectCx!=null) {
                for (int i = 0; i < selectCx.size(); i++) {
                    if (selectCx.get(i).equals("lateNum")) count.setLateNum(1);
                    if (selectCx.get(i).equals("backNum")) count.setBackNum(1);
                    if (selectCx.get(i).equals("lackNum")) count.setLackNum(1);
                    if (selectCx.get(i).equals("maternityLeave")) count.setMaternityLeave(1);
                    if (selectCx.get(i).equals("annualLeave")) count.setAnnualLeave(1);
                    if (selectCx.get(i).equals("peternituLeave")) count.setPeternituLeave(1);
                    if (selectCx.get(i).equals("maritalLeave")) count.setMaritalLeave(1);
                    if (selectCx.get(i).equals("funeralLeave")) count.setFuneralLeave(1);
                    if (selectCx.get(i).equals("affairLeave")) count.setAffairLeave(1);
                    if (selectCx.get(i).equals("sickLeave")) count.setSickLeave(1);
                    if (selectCx.get(i).equals("otherLeave")) count.setOtherLeave(1);
                }
            }
            List<TbAttenceCount> list = attenceCountService.queryAttenceCountList(count,page);
            result.put("list",list);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取考勤统计数据异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取考勤统计数据异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }
}
