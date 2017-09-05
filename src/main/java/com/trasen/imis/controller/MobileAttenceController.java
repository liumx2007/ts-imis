package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.trasen.imis.model.AttenceLogVo;
import com.trasen.imis.model.AttenceVo;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.service.MobileAttenceService;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by zhangxiahui on 17/6/21.
 */
@Controller
@RequestMapping("/mobileAttence")
public class MobileAttenceController {

    private static final Logger logger = Logger.getLogger(MobileAttenceController.class);

    @Autowired
    MobileAttenceService mobileAttenceService;


    @ResponseBody
    @RequestMapping(value = "/getAttenceToday", method = RequestMethod.POST)
    public Map<String,Object> getAttenceToday(@RequestBody Map<String, Object> params){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String openId = MapUtils.getString(params, "openId");
            AttenceVo attenceVo = mobileAttenceService.getAttenceToday(openId);

            if(attenceVo!=null){
                result.put("attenceVo",attenceVo);
            }else{
                result.put("status", 0);
                result.put("msg", "openId没找到");
            }
        } catch (IllegalArgumentException e) {
            logger.error("获取当天考勤数据异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("获取当天考勤数据异常" + e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/signInOrOut", method = RequestMethod.POST)
    public Map<String,Object> signInOrOut(@RequestBody AttenceLogVo attenceLogVo){
        //结果集
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", 1);
        result.put("msg", "success");
        try {
            logger.info("==今日签到系统======");
            logger.info("==今日签到系统======"+attenceLogVo.getOpenId());
            logger.info("==今日签到系统======"+attenceLogVo.getType());
            logger.info("==今日签到系统======"+attenceLogVo.getAttenceDate());
            logger.info("==今日签到系统======"+attenceLogVo.getAttenceWeek());
            logger.info("==今日签到系统======"+attenceLogVo.getAccuracy());
            logger.info("==今日签到系统======"+attenceLogVo.getAttenceTime());
            logger.info("==今日签到系统======"+attenceLogVo.getLatitude());
            logger.info("==今日签到系统======"+attenceLogVo.getLongitude());
            logger.info("==今日签到系统======"+attenceLogVo.getName());
            if(attenceLogVo!=null&&attenceLogVo.getType()!=null){
                AttenceVo attenceVo = mobileAttenceService.signIn(attenceLogVo);
                if("signIn".equals(attenceLogVo.getType())&&(attenceVo==null||attenceVo.getSigninTime()==null)){
                    result.put("status", 0);
                    result.put("msg", "您不在考勤范围内!");
                }else if("signOut".equals(attenceLogVo.getType())&&(attenceVo==null||attenceVo.getSignoutTime()==null)){
                    result.put("status", 0);
                    result.put("msg", "签退失败!");
                }else if("sign".equals(attenceLogVo.getType())&&(attenceVo==null||attenceVo.getSigninTime()==null)){
                    result.put("status", 0);
                    result.put("msg", "签到失败!");
                }else{
                    result.put("attenceVo",attenceVo);
                }
            }

        } catch (Exception e) {
            logger.error("签到异常" + e.getMessage(), e);
            result.put("status", -1);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryAttLogList", method = RequestMethod.POST)
    public Result queryAttLogList(@RequestBody Map<String, Object> params){
        //结果集
        Result result = new Result();
        result.setStatusCode(1);
        result.setSuccess(true);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String openId = MapUtils.getString(params, "openId");
            String attDate = MapUtils.getString(params, "attDate");
            AttenceLogVo attLog = new AttenceLogVo();
            attLog.setOpenId(openId);
            attLog.setAttenceDate(attDate);
            List<AttenceLogVo> list = mobileAttenceService.queryAttLogList(attLog);
            result.setObject(list);
        } catch (IllegalArgumentException e) {
            logger.error("获取考勤记录异常" + e.getMessage(), e);
            result.setStatusCode(0);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取考勤记录异常" + e.getMessage(), e);
            result.setStatusCode(0);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getSubPerson", method = RequestMethod.POST)
    public Object getSubPerson(@RequestBody Map<String, Object> params){
        //结果集
        Result result = new Result();
        result.setStatusCode(1);
        result.setSuccess(true);
        List<TbPersonnel> list = new ArrayList<>();
        result.setObject(list);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String openId = MapUtils.getString(params, "openId");
            String name = MapUtils.getString(params, "name");
            Integer userId = mobileAttenceService.getUserIdToOPenId(openId);
            if(userId!=null){
                String menusUrl = PropertiesUtils.getProperty("nu_authorize_subPerson")
                        .replace("{appId}","ts-imis").replace("{userId}",userId.toString());
                String resultStr = HttpUtil.connectURLGET(menusUrl,"");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(resultStr);
                return jsonObject;
            }
        } catch (IllegalArgumentException e) {
            logger.error("获取下属异常" + e.getMessage(), e);
            result.setStatusCode(0);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取下属异常" + e.getMessage(), e);
            result.setStatusCode(0);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
