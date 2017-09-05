package com.trasen.imis.service;

import com.trasen.imis.model.JsapiSignature;
import com.trasen.imis.model.UserToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangxiahui on 17/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class WeixinServiceTest {
    @Autowired
    WeixinService weixinService;

    @Test
    @Rollback(false)
    public void fetchAccessToken() {
        // 1).获取access_token
        String accessToken = weixinService.fetchAccessToken();
        System.out.println("========="+accessToken);
    }

    @Test
    @Rollback(false)
    public void fetchJsapiSignature() {
        // 1).获取access_token
        String url = "http://plattesting09.trasen.cn/src/index.html";
        JsapiSignature signature = weixinService.fetchJsapiSignature(url);
        System.out.println("========="+signature.getSignature());
    }

    @Test
    public void getUserToken(){
        UserToken userToken = weixinService.getUserToken("051QTzZ41s2TAN1OaI151xkyZ41QTzZ0");
        //System.out.println("====="+userToken.getOpenid());
    }
}
