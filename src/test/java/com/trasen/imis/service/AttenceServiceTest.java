package com.trasen.imis.service;

import com.trasen.imis.model.AttenceVo;
import com.trasen.imis.model.TbAttenceLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * Created by zhangxiahui on 17/6/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class AttenceServiceTest {

    @Autowired
    AttenceService attenceService;


    @Test
    @Rollback(false)
    public void getAttenceRuleData(){
        Map<String,TbAttenceLocation> map = attenceService.getAttenceRuleData();
        Set<String> set = map.keySet();
        for(String key : set){
            System.out.println("===="+key);
        }
        String tagId = "|HIS|";
        TbAttenceLocation tbAttenceLocation = attenceService.getAttenceRule(tagId,map);
        /*System.out.println("=="+tagId);
        System.out.println("=="+tbAttenceLocation.getTagId()+"=="+tbAttenceLocation.getDepId());*/
    }

    @Test
    @Rollback(false)
    public void makeAttenceLack(){
        boolean boo = attenceService.makeAttenceLack("2017-06-27");
        System.out.println(boo);
    }
}
