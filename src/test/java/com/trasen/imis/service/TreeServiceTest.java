package com.trasen.imis.service;

import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.model.TbTree;
import com.trasen.imis.model.TreeVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TreeServiceTest {

    @Autowired
    TreeService treeService;

    @Test
    @Rollback(false)
    public void makeAttenceLack(){

        TbTree tree = treeService.getParentTree();
        TreeVo treeVo = treeService.getTree(tree);


        String menuJsonString = JSONObject.toJSONString(treeVo);

        System.out.println(menuJsonString);
    }

    @Test
    @Rollback(false)
    public void insertTreeDept(){

        Map<String,Object> mapdept=new HashMap<String,Object>();
        mapdept.put("pkid","d_32");
        mapdept.put("deptname","内控2");
        mapdept.put("per_deptid","8");
        mapdept.put("per_deptname","长沙微康");
        mapdept.put("per_level","1");
        mapdept.put("level","2");
        mapdept.put("type","dept");
        mapdept.put("created",new Date());
        mapdept.put("remark","备注");

        //treeService.insertTreeDept(mapdept);

    }




}
