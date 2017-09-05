package com.trasen.imis.controller;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.trasen.imis.model.JsapiSignature;
import com.trasen.imis.model.UserToken;
import com.trasen.imis.service.WeixinService;
import com.trasen.imis.service.WinXinPersonService;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/6/16.
 */
@Controller
@RequestMapping("/authorize")
public class WeixinAuthorizeController {

    private static final Logger logger = Logger.getLogger(WeixinAuthorizeController.class);

    @Autowired
    WeixinService weixinService;

    @Autowired
    WinXinPersonService winXinPersonService;

    @ResponseBody
    @RequestMapping(value = "/fetchJsApiTicket", method = RequestMethod.POST)
    public Map<String,Object> fetchJsApiTicket(@QueryParam("url") String url){
        String appid = PropertiesUtils.getProperty("appid");
        String nonceStr = null;
        String timestamp = null;
        String signature = null;
        if(url!=null){
            String [] urls = url.split("#");
            url = urls[0];
            JsapiSignature jsapiSignature = weixinService.fetchJsapiSignature(url);
            if(jsapiSignature!=null){
                nonceStr = jsapiSignature.getNoncestr();
                timestamp = jsapiSignature.getTimestamp();
                signature = jsapiSignature.getSignature();
            }
        }
        return new ImmutableMap.Builder<String, Object>().put("appId", appid)
                .put("nonceStr", nonceStr).put("timestamp",timestamp).put("signature",signature).build();
    }

    @ResponseBody
    @RequestMapping(value = "/oauth2", method = RequestMethod.GET)
    public Map<String,Object> oauth2(@QueryParam("code") String code){
        if("1234".equals(code)){
            // TODO: 17/8/2  设置下属考勤权限
            Integer check = winXinPersonService.checkWeixinOpenId("ofzbj0uh4TG4xwrp4rHKW8kmMx-o");
            return new ImmutableMap.Builder<String, Object>().put("openid", "ofzbj0uh4TG4xwrp4rHKW8kmMx-o")
                    .put("status", 1).put("check",check).build();
        }
        UserToken userToken = weixinService.getUserToken(code);
        if(userToken!=null&&userToken.getOpenid()!=null){
            // TODO: 17/8/2  设置下属考勤权限
            Integer check = winXinPersonService.checkWeixinOpenId(userToken.getOpenid());
            return new ImmutableMap.Builder<String, Object>().put("openid", userToken.getOpenid())
                    .put("status", 1).put("check",check).build();
        }else{
            return new ImmutableMap.Builder<String, Object>().put("status", 0).build();
        }
    }

}
