package com.trasen.imis.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.cache.GlobalCache;
import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.service.*;
import com.trasen.imis.task.WeiXinPersonTask;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/7.
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    private static final Logger logger = Logger.getLogger(TaskController.class);

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    @Autowired
    AttenceService attenceService;

    @Autowired
    AttenceCountService attenceCountService;

    @Autowired
    ContractService contractService;

    @Autowired
    JfRecordService jfRecordService;

    @Autowired
    ProductService productService;


    @ResponseBody
    @RequestMapping(value = "/attRule", method = RequestMethod.GET, produces = "application/json")
    public Result attRule() {
        Result result = new Result();
        Map<String,TbAttenceLocation> attRuleMap = attenceService.getAttenceRuleData();
        globalCache.setAttRuleMap(attRuleMap);
        result.setSuccess(true);
        result.setStatusCode(1);
        result.setMessage("刷新考期规则缓存成功");
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/makeAttenceLack", method = RequestMethod.GET, produces = "application/json")
    public Result makeAttenceLack(@RequestParam String date) {
        Result result = new Result();
        if(StringUtil.isEmpty(date)){
            date = DateUtils.getYestday("yyyy-MM-dd");
        }
        logger.info("======================生成缺勤数据定时任务["+date+"]=====================");
        boolean boo = attenceService.makeAttenceLack(date);
        result.setSuccess(boo);
        result.setStatusCode(1);
        result.setMessage("生成缺勤数据成功");
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/countAttence", method = RequestMethod.GET, produces = "application/json")
    public Result countAttence(@RequestParam String date) {
        Result result = new Result();
        if(StringUtil.isEmpty(date)){
            date = DateUtils.getDate(new Date(),"yyyyMM");
        }
        logger.info("=======执行考勤统计任务,开始统计["+date+"]的考勤数据");
        attenceCountService.countAttence(date);
        result.setSuccess(true);
        result.setStatusCode(1);
        result.setMessage("执行考勤统计任务成功");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/sendContract", method = RequestMethod.GET, produces = "application/json")
    public Result countAttence() {
        Result result=new Result();
        String appid = PropertiesUtils.getProperty("appid");
        String templateId = PropertiesUtils.getProperty("templateId");
        String toUser = PropertiesUtils.getProperty("toUser");
        String openName = PropertiesUtils.getProperty("openName");
        String messageUrl=PropertiesUtils.getProperty("send_template_message");
        System.out.println("===============发送劳动合同");
        if (appid == null || templateId == null || toUser == null || openName == null||messageUrl==null) {
            logger.info("参数错误:" + "参数错误");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String dateStr = sdf.format(date);
            String contractDate = null;
            try {
                Calendar rightNow = Calendar.getInstance();
                String contractMouth = PropertiesUtils.getProperty("contractMouth");
                rightNow.setTime(date);
                rightNow.add(Calendar.MONTH, Integer.valueOf(contractMouth));// 日期加3个月
                Date dt1 = rightNow.getTime();
                contractDate = sdf.format(dt1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, String> dateMap = new HashMap<String, String>();
            dateMap.put("dateStrat", dateStr);
            dateMap.put("dateEnd", contractDate);
            String contractName = contractService.getTbContratListByDate(dateMap);
            if(contractName != null) {
                Map<String, String> wxParam = new HashMap<String, String>();
                wxParam.put("appid", appid);
                wxParam.put("touser", toUser);
                wxParam.put("templateId", templateId);
                wxParam.put("name", "尹中贵");
                wxParam.put("code", contractName);
                String parameterJson = JSONObject.toJSONString(wxParam);
                String message = HttpUtil.connectURL(messageUrl, parameterJson, "POST");
                JSONObject dataJson = (JSONObject) JSONObject.parse(message);
                String resultmeg = dataJson.getString("msg");
                logger.info("劳动合同发送:" + resultmeg);
            }
        }
        result.setSuccess(true);
        result.setStatusCode(1);
        result.setMessage("劳动合同发送任务成功");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/savejfRecordFor_attence", method = RequestMethod.GET, produces = "application/json")
    public Result savejfRecordFor_attence(@RequestParam String date) {
        Result result = new Result();
        if(StringUtil.isEmpty(date)){
            date = DateUtils.getYearMonth();
        }
        logger.info("=======生成["+date+"]考勤积分");
        jfRecordService.savejfRecordFor_attence(date);
        result.setSuccess(true);
        result.setStatusCode(1);
        result.setMessage("生成["+date+"]考勤积分");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateProductList", method = RequestMethod.GET, produces = "application/json")
    public Result saveOrUpdateProductList() {
        Result result = new Result();
        logger.info("======================同步产品数据开始");
        productService.saveOrUpdateProductList();
        result.setSuccess(true);
        result.setStatusCode(1);
        result.setMessage("======================同步产品数据结束");
        return result;
    }
}
