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

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestTbHoliday {

    @Autowired
    private TbHolidayService tbHolidayService;

    @Test
    @Rollback(false)
    public void saveByList(){
        String result=tbHolidayService.saveByList("2017");
        System.out.println(result);
    }

    @Test
    @Rollback(false)
    public void getDaysforYearMonth(){
        int result=tbHolidayService.getDaysforYearMonth("2017-11","|12|");
        System.out.println("=============="+result);
    }

    @Test
    @Rollback(false)
    public void getWorkDayForRue(){
        List<String> list=tbHolidayService.getWorkDayForRue("周一,周二,周三,周四,周五","2017-11");
        System.out.println(list);
    }
}
