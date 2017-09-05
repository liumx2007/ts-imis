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

        /*ViewButton cb01 = new ViewButton();
        cb01.setName("加入创星");
        cb01.setType("view");
        cb01.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_1_c_1#wechat_redirect");

        ViewButton cb11 = new ViewButton();
        cb11.setName("晋级申请");
        cb11.setType("view");
        cb11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_2_c_1#wechat_redirect");

        // 菜单3
        ComplexButton cb2 = new ComplexButton();
        cb2.setName("个人中心");
        ViewButton cb21 = new ViewButton();
        cb21.setName("积分记录");
        cb21.setType("view");
        cb21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_3_c_3#wechat_redirect");

        ViewButton cb22 = new ViewButton();
        cb22.setName("个人信息");
        cb22.setType("view");
        cb22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_3_c_2#wechat_redirect");
        //cb2.setSub_button(new ViewButton[]{cb21,cb22});
        List<Button> cb22List = new ArrayList<>();
        cb22List.add(cb21);
        cb22List.add(cb22);
        cb2.setSub_button(cb22List);


        //menu.setButton(new Button[]{cb01,cb11,cb2});
        List<Button> list = new ArrayList<>();
        list.add(cb01);
        list.add(cb11);
        list.add(cb2);

        menu.setButton(list);*/




        // 菜单1
        /*ComplexButton cb0 = new ComplexButton();
        cb0.setName("考勤");

        ViewButton cb01 = new ViewButton();
        cb01.setName("公司考勤");
        cb01.setType("view");
        cb01.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2Fmobile&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb02 = new ViewButton();
        cb02.setName("外出考勤");
        cb02.setType("view");
        cb02.setUrl("http://bpmtest01.trasen.cn/src/index.html?attType=1#/mobile");

        ViewButton cb03 = new ViewButton();
        cb03.setName("考勤纪录");
        cb03.setType("view");
        cb03.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2FattList&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        List<Button> cb0List = new ArrayList<>();
        cb0List.add(cb01);
        cb0List.add(cb02);
        cb0List.add(cb03);
        cb0.setSub_button(cb0List);

        // 菜单2
        ComplexButton cb1 = new ComplexButton();
        cb1.setName("积分");

        ViewButton cb11 = new ViewButton();
        cb11.setName("加入创星");
        cb11.setType("view");
        cb11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_1_c_1#wechat_redirect");

        ViewButton cb12 = new ViewButton();
        cb12.setName("晋级申请");
        cb12.setType("view");
        cb12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_2_c_1#wechat_redirect");

        ViewButton cb13 = new ViewButton();
        cb13.setName("积分记录");
        cb13.setType("view");
        cb13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_3_c_3#wechat_redirect");

        List<Button> cb1List = new ArrayList<>();
        cb1List.add(cb11);
        cb1List.add(cb12);
        cb1List.add(cb13);

        cb1.setSub_button(cb1List);

        // 菜单3
        ComplexButton cb2 = new ComplexButton();
        cb2.setName("个人中心");

        ViewButton cb21 = new ViewButton();
        cb21.setName("个人信息");
        cb21.setType("view");
        cb21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2Fsrc%2Findex.html%23%2Fperson&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb22 = new ViewButton();
        cb22.setName("积分信息");
        cb22.setType("view");
        cb22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5788f1ce93ff3255&redirect_uri=http%3A%2F%2Fbpmtest01.trasen.cn%2FTrasenJFApi%2FBaseWebForm.aspx&response_type=code&scope=snsapi_userinfo&state=m_3_c_2#wechat_redirect");


        List<Button> cb2List = new ArrayList<>();
        cb2List.add(cb21);
        cb2List.add(cb22);


        cb2.setSub_button(cb2List);

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

        ViewButton cb02 = new ViewButton();
        cb02.setName("外出考勤");
        cb02.setType("view");
        cb02.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%3FattType%3D1%23%2Fmobile&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        ViewButton cb03 = new ViewButton();
        cb03.setName("考勤纪录");
        cb03.setType("view");
        cb03.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx883815fb0da06f3d&redirect_uri=http%3A%2F%2Fplattesting09.trasen.cn%2Fsrc%2Findex.html%23%2FattList&response_type=code&scope=snsapi_base&state=TS-IMIS#wechat_redirect");

        List<Button> cb0List = new ArrayList<>();
        cb0List.add(cb01);
        cb0List.add(cb02);
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

        List<Button> cb1List = new ArrayList<>();
        cb1List.add(cb12);
        cb1List.add(cb13);

        cb1.setSub_button(cb1List);





        List<Button> cbList = new ArrayList<>();
        cbList.add(cb0);
        cbList.add(cb1);
        menu.setButton(cbList);
        String menuJsonString = JSONObject.toJSONString(menu);

        System.out.println(menuJsonString);
        //WeixinUtil.createMenu(menu, accessToken);

    }


}
