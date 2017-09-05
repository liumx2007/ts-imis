package com.trasen.imis.service;

import com.trasen.imis.common.MybatisDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangxiahui on 17/5/26.
 */
@Component
public class TestService {

    @Autowired
    MybatisDao mybatisDao;

    Logger logger = Logger.getLogger(TestService.class);

    public String getTestName(String id){
        return null ;//mybatisDao.getSingleRow("Test.getTestName",id);
    }




}
