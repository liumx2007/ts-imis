package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbAttenceLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class TestLocationService {
    @Autowired
    private LocationService locationService;

    @Test
    @Rollback(false)
    public void test(){
        Page page = new Page(1,5);
        List<TbAttenceLocation> tbAttenceLocationList = locationService.getLocationList(page);
        for( TbAttenceLocation tbAttenceLocation : tbAttenceLocationList){
            System.out.println(tbAttenceLocation.getAddress());
        }
    }
    @Test
    @Rollback(false)
    public void testlocationSave(){
        TbAttenceLocation tbAttenceLocation=new TbAttenceLocation();
        tbAttenceLocation.setAddress("jar");
        tbAttenceLocation.setCreateUser("xitong");
        tbAttenceLocation.setLongitude(23.500f);
        tbAttenceLocation.setLatitude(25.00f);
        tbAttenceLocation.setRange(3);
        tbAttenceLocation.setName("3232");
         locationService.locationSave(tbAttenceLocation);

    }
    @Test
    @Rollback(false)
    public void testlocationViewForid(){
        int pkid=1;
        TbAttenceLocation tbAttenceLocation=locationService.locationViewForid(pkid);
        if(tbAttenceLocation==null){
            System.out.println("查询失败");
        }else{
            System.out.println(tbAttenceLocation.getAddress());
        }
    }
    @Test
    @Rollback(false)
    public void testlocationUpdateForid(){
        TbAttenceLocation tbAttenceLocation=new TbAttenceLocation();
        tbAttenceLocation.setPkid(1);
        tbAttenceLocation.setName("我的");
        int locationInt=locationService.locationUpdateForid(tbAttenceLocation);
        if(locationInt==0){
            System.out.println("数据无更新");
        }else{
            System.out.println("数据更新成功");
        }
    }
    @Test
    @Rollback(false)
    public void locationDeleteForid(){

        int pkid=1;
        int msg=locationService.locationDeleteForid(pkid);
        if(msg==0){
            System.out.println("数据删除失败");
        }else{
            System.out.println("数据删除成功");
        }
    }

}
