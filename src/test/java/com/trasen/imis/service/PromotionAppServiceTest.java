package com.trasen.imis.service;

import com.trasen.imis.model.AttenceLeave;
import com.trasen.imis.model.TbTree;
import org.junit.*;
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
 * @date 2017/9/4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class PromotionAppServiceTest {

    @Autowired
    private PromotionAppService promotionAppService;

    @Test
    @Rollback(false)
    public void test(){
       List<TbTree> list= promotionAppService.getDeptList("C_1");
       for(TbTree tbTree:list){
           System.out.println("========="+tbTree.getName());
       }
    }
}
