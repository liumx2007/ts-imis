package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.common.SecurityCheck;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangxiahui on 17/8/21.
 * 接口调试用,测试
 */
@Controller
@RequestMapping("ts-authorize")
public class AuthorizeController {

    Logger logger = Logger.getLogger(AuthorizeController.class);


    @ResponseBody
    @RequestMapping(value = "/{appId}/menus", method = RequestMethod.GET)
    public JSONObject getMenus(@PathVariable String appId,HttpServletRequest request) {
        logger.info("===权限系统:获取菜单权限====["+ VisitInfoHolder.getUserId()+"]====");
        String xToken = SecurityCheck.getHeaderValue(request,"X-TOKEN");
        String menusUrl = PropertiesUtils.getProperty("authorize_menus").replace("{appId}",appId);
        String result = HttpUtil.connectURLGET(menusUrl,xToken);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/{appId}/operList/{state}", method = RequestMethod.GET)
    public JSONObject getOperList(@PathVariable String appId,@PathVariable String state,HttpServletRequest request) {
        logger.info("===权限系统:获取页面操作权限====["+ VisitInfoHolder.getUserId()+"]====["+state+"]===");
        String xToken = SecurityCheck.getHeaderValue(request,"X-TOKEN");
        String menusUrl = PropertiesUtils.getProperty("authorize_operList")
                .replace("{appId}",appId).replace("{state}",state);
        String result = HttpUtil.connectURLGET(menusUrl,xToken);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value="/{appId}/getSubordinateList", method = RequestMethod.GET)
    public JSONObject getSubordinateList(@PathVariable String appId,HttpServletRequest request){
        logger.info("===权限系统:获取页面操作权限====["+ VisitInfoHolder.getUserId()+"]====["+appId+"]===");
        String xToken = SecurityCheck.getHeaderValue(request,"X-TOKEN");
        String menusUrl = PropertiesUtils.getProperty("authorize_subPerson").replace("{appId}",appId);
        String result = HttpUtil.connectURLGET(menusUrl,xToken);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject;
    }




}
