package com.trasen.imis.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ContractProductService contractProductService;

    @Test
    @Rollback(false)
    public void saveOrUpdateProductList(){
        System.out.println("========productService:"+productService.saveOrUpdateProductList());
    }

    /*@Test
    public void getcontractTransenList(){
        Map<String,String> param=new HashMap<String,String>();
        param.put("rows","10");
        param.put("page","1");
        Map<String,Object> params=contractProductService.getcontractTransenList(param);
        List<ContractInfo> contractInfoList= (List<ContractInfo>) params.get("list");
        for(ContractInfo contractInfo:contractInfoList){
            System.out.println(contractInfo.getContractNo());
        }*/

    }


}
