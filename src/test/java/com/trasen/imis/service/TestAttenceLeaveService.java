package com.trasen.imis.service;

import com.trasen.imis.model.AttenceLeave;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestAttenceLeaveService {

    @Autowired
    private AttenceLeaveService attenceLeaveService;

    @Test
    @Rollback(false)
    public void testAttenceLeaveSave(){
        AttenceLeave attenceLeave=new AttenceLeave();
        attenceLeave.setAttenceDate("2017-07-04");
        attenceLeave.setCreateUser("系统");
        attenceLeave.setEndTime("10:00");
        attenceLeave.setStartTime("8:00");
        attenceLeave.setName("luoyun");
        attenceLeave.setLeaveHours(2);
        attenceLeave.setPosition("S");
        attenceLeave.setWorkNum("1002");
        attenceLeave.setRemark("备注");
        attenceLeave.setType(1);
        attenceLeave.setTagId("2");
        attenceLeave.setTagName("luoyun");
        attenceLeave.setCreated(new Date());
        List<AttenceLeave> attenceLeaveList=new ArrayList<AttenceLeave>();
        attenceLeaveList.add(attenceLeave);
        attenceLeaveService.insertAttenceLeaveList(attenceLeaveList);
        // locationService.locationSave(tbAttenceLocation);
    }


}
