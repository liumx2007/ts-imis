package com.trasen.imis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.model.*;
import com.trasen.imis.utils.WeixinUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class MenuTest {
    @Autowired
    WeixinService weixinService;

    @Test
    @Rollback(false)
    public void createMenu(){
        // 1).获取access_token
        String accessToken = weixinService.fetchAccessToken();
        // 2).创建菜单
        Menu menu = new Menu();




        //2017-12-20
        /*ComplexButton cb0 = new ComplexButton();
        cb0.setName("考勤");

        ViewButton cb01 = new ViewButton();
        cb01.setName("公司考勤");
        cb01.setType("view");
        cb01.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2Fmobile&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb04 = new ViewButton();
        cb04.setName("公司考勤2");
        cb04.setType("view");
        cb04.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2fapp%2fattence%2findex.html&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");


        ViewButton cb02 = new ViewButton();
        cb02.setName("外出考勤");
        cb02.setType("view");
        cb02.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%3FattType%3D1%23%2Fmobile&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb05 = new ViewButton();
        cb05.setName("外出考勤2");
        cb05.setType("view");
        cb05.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2fapp%2fattence%2findex.html%3FattType%3D1&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");



        ViewButton cb03 = new ViewButton();
        cb03.setName("考勤纪录");
        cb03.setType("view");
        cb03.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2FattList&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        List<Button> cb0List = new ArrayList<>();
        cb0List.add(cb01);
        cb0List.add(cb04);
        cb0List.add(cb02);
        cb0List.add(cb05);
        cb0List.add(cb03);
        cb0.setSub_button(cb0List);

        // 菜单2
        ComplexButton cb1 = new ComplexButton();
        cb1.setName("积分");


        ViewButton cb12 = new ViewButton();
        cb12.setName("晋级申请");
        cb12.setType("view");
        cb12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2Fpromotion&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb13 = new ViewButton();
        cb13.setName("积分记录");
        cb13.setType("view");
        cb13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2FjfRecord&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb14 = new ViewButton();
        cb14.setName("个人信息");
        cb14.setType("view");
        cb14.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2Fperson&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        List<Button> cb1List = new ArrayList<>();
        cb1List.add(cb12);
        cb1List.add(cb13);
        cb1List.add(cb14);

        cb1.setSub_button(cb1List);


        ViewButton cb2 = new ViewButton();
        cb2.setName("信息平台");
        cb2.setType("view");
        cb2.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3a%2f%2Fbpmtest01.trasen.cn%2fsrc%2fmobile%2fimitationLogin%2flogin.html&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");





        List<Button> cbList = new ArrayList<>();
        cbList.add(cb0);
        cbList.add(cb1);
        cbList.add(cb2);
        menu.setButton(cbList);
        String menuJsonString = JSONObject.toJSONString(menu);*/







        ComplexButton cb0 = new ComplexButton();
        cb0.setName("考勤");

        ViewButton cb01 = new ViewButton();
        cb01.setName("公司考勤");
        cb01.setType("view");
        cb01.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%23%2Fmobile&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb04 = new ViewButton();
        cb04.setName("新公司考勤");
        cb04.setType("view");
        cb04.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3a%2f%2fplattesting09.trasen.cn%2fsrc%2fapp%2fattence%2findex.html&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");


        ViewButton cb02 = new ViewButton();
        cb02.setName("外出考勤");
        cb02.setType("view");
        cb02.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%3FattType%3D1%23%2Fmobile&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb05 = new ViewButton();
        cb05.setName("新外出考勤");
        cb05.setType("view");
        cb05.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3a%2f%2fplattesting09.trasen.cn%2fsrc%2fapp%2fattence%2findex.html%3FattType%3D1&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb03 = new ViewButton();
        cb03.setName("考勤纪录");
        cb03.setType("view");
        cb03.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%23%2FattList&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        List<Button> cb0List = new ArrayList<>();
        cb0List.add(cb01);
        cb0List.add(cb04);
        cb0List.add(cb02);
        cb0List.add(cb05);
        cb0List.add(cb03);
        cb0.setSub_button(cb0List);

        // 菜单2
        ComplexButton cb1 = new ComplexButton();
        cb1.setName("积分");


        ViewButton cb12 = new ViewButton();
        cb12.setName("晋级申请");
        cb12.setType("view");
        cb12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%23%2Fpromotion&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb13 = new ViewButton();
        cb13.setName("积分记录");
        cb13.setType("view");
        cb13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%23%2FjfRecord&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb14 = new ViewButton();
        cb14.setName("个人信息");
        cb14.setType("view");
        cb14.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%23%2Fperson&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        List<Button> cb1List = new ArrayList<>();
        cb1List.add(cb12);
        cb1List.add(cb13);
        cb1List.add(cb14);

        cb1.setSub_button(cb1List);


        ViewButton cb2 = new ViewButton();
        cb2.setName("信息平台");
        cb2.setType("view");
        cb2.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3a%2f%2fplattesting09.trasen.cn%2fsrc%2fmobile%2fimitationLogin%2flogin.html&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");





        List<Button> cbList = new ArrayList<>();
        cbList.add(cb0);
        cbList.add(cb1);
        cbList.add(cb2);
        menu.setButton(cbList);
        String menuJsonString = JSONObject.toJSONString(menu);

        System.out.println(menuJsonString);
        //WeixinUtil.createMenu(menu, accessToken);

    }


}
