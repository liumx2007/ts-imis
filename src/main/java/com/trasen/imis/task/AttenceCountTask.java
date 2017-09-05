package com.trasen.imis.task;

import com.trasen.imis.service.AttenceCountService;
import com.trasen.imis.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/7/6.
 */
@Component
public class AttenceCountTask implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AttenceCountService attenceCountService;

    @Override
    public void run() {
        String date = DateUtils.getDate(new Date(),"yyyyMM");
        logger.info("=======执行考勤统计任务,开始统计["+date+"]的考勤数据");
        attenceCountService.countAttence(date);
        logger.info("=======完成考勤["+date+"]的考勤统计");


    }
}
