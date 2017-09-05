package com.trasen.imis.service;

import com.trasen.imis.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/7/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class AttenceCountServiceTest {

    @Autowired
    AttenceCountService attenceCountService;




    @Test
    @Rollback(false)
    public void countAttence(){
        /*String date = DateUtils.getDate(new Date(),"yyyyMM");
        System.out.println("=========日期:"+date);*/
        attenceCountService.countAttence("201706");
    }

}
