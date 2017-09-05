package com.trasen.imis.service;

import com.trasen.imis.model.WinXinPerson;
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
 * @date 2017/7/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestWeiXinPersonService {

    @Autowired
    private WinXinPersonService winXinPersonService;

    @Test
    @Rollback(false)
    public void getBlackList(){
        int wxblack=winXinPersonService.getBlackList("文建");
        System.out.println(wxblack);

    }

    @Test
    @Rollback(false)
    public void getWeiXinPersonByName(){
        List<WinXinPerson> winXinPersonList= winXinPersonService.getPersonByName("张夏晖");
        if(winXinPersonList.size()==0){
            System.out.println("数据为空");
        }else{
           String content="";
            for(WinXinPerson winXinPerson:winXinPersonList){
                content = content+winXinPerson.getName() + " 个人信息,请注意保密\n"
                        +"手机:"+winXinPerson.getPhone()+"\n"
                        +"部门:"+winXinPerson.getDepName()+"\n"
                        +"家庭住址:"+winXinPerson.getHouseAddress()+"\n"
                        +"所在项目:"+winXinPerson.getTagName()+"\n";
            }
            System.out.println(content);
        }
    }
}
