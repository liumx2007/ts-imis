package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.controller.vo.TestVo;
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
 * Created by zhangxiahui on 17/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestServiceTest {

    @Autowired
    TbTestService tbTestService;

    @Test
    @Rollback(false)
    public void test(){
        Page page = new Page(1,5);
        //page.setSidx("name");
        //page.setSord("desc");
        List<TestVo> list = tbTestService.getList(page);
        for( TestVo vo : list){
            System.out.println(vo.getName());
        }
    }

}
