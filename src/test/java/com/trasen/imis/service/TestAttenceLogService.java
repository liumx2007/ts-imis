package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.AttenceLogVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: TestAttenceLogService
 * @Description: 操作类型
 * @date 2017/6/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestAttenceLogService {

    @Autowired
    private AttenceLogService attenceLogService;

    @Test
    @Rollback(false)
    public void testGetAttenceLogList(){
        Map<String,Object> attenceLogMap=new HashMap<String,Object>();
        Page page = new Page(1,5);
        attenceLogMap.put("workNum","1002");
        List<AttenceLogVo> attenceLogVoList= attenceLogService.getAttenceLogList(attenceLogMap,page);
        for(AttenceLogVo attenceLogVo:attenceLogVoList){
            System.out.println(attenceLogVo.getAddress());
        }


    }
}
