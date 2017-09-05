package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbContract;
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
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestContractService {

    @Autowired
    private ContractService contractService;

    @Test
    @Rollback(false)
    public void getTbContractList(){
        Page page=new Page(1,5);
        Map<String,String> param=new HashMap<String,String>();
        List<TbContract> tbContractList=contractService.getTbContractList(param,page);
        if(tbContractList.size()>0){
            System.out.println("-----------------"+tbContractList.get(0).getName());
        }else{
            System.out.println("-----------------"+"数据为空");
        }
    }

    @Test
    @Rollback(true)
    public void insertContract(){
        TbContract tbContract=new TbContract();
        tbContract.setEntryDate("2017-01-01");
        tbContract.setRegularDate("2017-02-02");
        tbContract.setName("luoyun");
        tbContract.setWorkNum("D_15623");
        tbContract.setOperator("xitong");
        contractService.insertContract(tbContract);
    }
}
