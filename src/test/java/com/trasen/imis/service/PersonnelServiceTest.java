package com.trasen.imis.service;

import com.trasen.imis.model.TbPersonnel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangxiahui on 17/7/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class PersonnelServiceTest {

    @Autowired
    PersonnelService personnelService;


    @Test
    @Rollback(false)
    public void getPkid(){
        Integer pkid = personnelService.getPkid("test");
        System.out.println("========pkid:"+pkid);
    }

    @Test
    @Rollback(false)
    public void insertPer(){
        TbPersonnel tbPersonnel = new TbPersonnel();
        tbPersonnel.setName("张夏晖");
        tbPersonnel.setPosition("开发工程师");
        tbPersonnel.setDepName("研发部");
        //personnelService.savePersonnel(tbPersonnel);

    }

    @Test
    @Rollback(false)
    public void updatePer(){
        TbPersonnel tbPersonnel = new TbPersonnel();
        tbPersonnel.setPerId("16");
        tbPersonnel.setName("张夏晖02");
        tbPersonnel.setPosition("开发工程师");
        tbPersonnel.setDepName("研发部02");
        //personnelService.savePersonnel(tbPersonnel);

    }
    @Test
    @Rollback(false)
    public void delePer(){
        //personnelService.deletePersonnel("16");
    }

    @Test
    public void getTagCode(){
        String str = personnelService.getTagCode("12",null);
        System.out.println("========="+str);
    }


}
