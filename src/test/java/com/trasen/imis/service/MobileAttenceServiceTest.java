package com.trasen.imis.service;

import com.trasen.imis.model.AttenceVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangxiahui on 17/6/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class MobileAttenceServiceTest {

    @Autowired
    MobileAttenceService mobileAttenceService;

    @Test
    @Rollback(false)
    public void getAttence(){
        AttenceVo attenceVo = mobileAttenceService.getAttenceToday("ofzbj0uh4TG4xwrp4rHKW8kmMx-o");
        if(attenceVo!=null){
            System.out.println("==="+attenceVo.getName()+"="+attenceVo.getWorkNum());

        }
    }

    @Test
    public void isHoliday(){
        System.out.println("============"+mobileAttenceService.isHoliday("2017-10-01"));;
        System.out.println("============"+mobileAttenceService.isWorkDay("2017-10-01"));;
        System.out.println("============"+mobileAttenceService.isHoliday("2017-12-12"));;
        System.out.println("============"+mobileAttenceService.isWorkDay("2017-12-12"));;
        System.out.println("============"+mobileAttenceService.isHoliday("2017-12-11"));;
        System.out.println("============"+mobileAttenceService.isWorkDay("2017-12-11"));;
        System.out.println("============"+mobileAttenceService.isHoliday("2017-12-10"));;
        System.out.println("============"+mobileAttenceService.isWorkDay("2017-12-10"));;


    }

}
