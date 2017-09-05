package com.trasen.imis.service;

import com.trasen.imis.model.TwfDict;
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
 * @date 2017/6/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestwfDictService {

    @Autowired
    private TwfDictService twfDictService;

    @Test
    @Rollback(false)
    public void testTwfDictforType(){
        List<TwfDict> twfDictList=twfDictService.getTwfDictForType(1);
        for(int i=0;i<twfDictList.size();i++){
            System.out.println(twfDictList.get(i).getName());
        }

    }

}
