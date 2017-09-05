package com.trasen.imis.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.AttenceVo;
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
 * Created by zhangxiahui on 17/6/23.
 */
@Controller
@RequestMapping("/attence")
public class AttenceController {

    private static final Logger logger = Logger.getLogger(AttenceController.class);

    @Autowired
    AttenceService attenceService;


    @ResponseBody
    @RequestMapping(value = "/searchAttList", method = RequestMethod.POST)
    public Map<String,Object> searchAttList(@RequestBody Map<String, Object> params){
        //结果集
        Map<String,Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo",1);
        result.put("pageSize", 0);
        result.put("totalCount",0);
        result.put("totalPages",1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String tagName = MapUtils.getString(params, "tagName");
            String name = MapUtils.getString(params, "name");
            String attenceDate = MapUtils.getString(params, "attenceDate");
            Integer type = MapUtils.getInteger(params,"type");
            Long lateTime = MapUtils.getLong(params,"lateTime");
            Long backTime = MapUtils.getLong(params,"backTime");
            String lackAtt = MapUtils.getString(params, "lackAtt");
            String startDate = MapUtils.getString(params, "startDate");
            String endDate = MapUtils.getString(params, "endDate");
            String signinType = MapUtils.getString(params, "signinType");


            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSord("desc");
            page.setSidx("created");


            AttenceVo attenceVo = new AttenceVo();
            if(!StringUtil.isEmpty(name)){
                attenceVo.setName(name);
            }
            if(!StringUtil.isEmpty(attenceDate)){
                attenceVo.setAttenceDate(attenceDate.substring(0,10));
            }
            if(!StringUtil.isEmpty(tagName)){
                attenceVo.setTagName(tagName);
            }
            if(type!=null){
                attenceVo.setType(type);
            }
            if(lateTime!=null){
                attenceVo.setLateTime(lateTime);
            }
            if(backTime!=null){
                attenceVo.setBackTime(backTime);
            }
            if(!StringUtil.isEmpty(startDate)){
                attenceVo.setStartTime(startDate);
            }
            if(!StringUtil.isEmpty(endDate)){
                attenceVo.setEndTime(endDate);
            }
            if(!StringUtil.isEmpty(signinType)){
                attenceVo.setSigninType(signinType);
            }



            List<AttenceVo> list = new ArrayList<>();
            if(!StringUtil.isEmpty(lackAtt)&&"lack".equals(lackAtt)){
                //list = attenceService.searchLackAttList(attenceVo,page);
                list=attenceService.searchActualLackAttList(attenceVo,page);
            }else if(!StringUtil.isEmpty(lackAtt)&&"leave".equals(lackAtt)){
                list = attenceService.searchLeaveAttList(attenceVo,page);
            }else{
                list = attenceService.searchAttList(attenceVo,page);
            }
            Map<String,Integer> attCount = attenceService.countAttence(attenceVo);

            result.put("list",list);
            result.put("attCount",attCount);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取考勤异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取考勤异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }







}
