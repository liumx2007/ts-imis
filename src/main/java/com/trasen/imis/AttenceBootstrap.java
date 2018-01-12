package com.trasen.imis;

import com.trasen.imis.task.*;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangxiahui on 17/6/26.
 */
@Component
@Scope("singleton")
public class AttenceBootstrap  implements ApplicationListener<ApplicationEvent> {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AttenceRuleTask attenceRuleTask;

    @Autowired
    MakeAttenceLackTask makeAttenceLackTask;

    @Autowired
    AttenceCountTask attenceCountTask;

    @Autowired
    WeiXinPersonTask weiXinPersonTask;

    @Autowired
    JfRecordTask jfRecordTask;


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            boolean isRoot = ((ContextRefreshedEvent) event).getApplicationContext().getParent() == null;
            if (isRoot) {
                try {
                    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(9);
                    String count_time = PropertiesUtils.getProperty("COUNT_TIME");
                    Integer time = 5;
                    if (count_time != null) {
                        time = Integer.parseInt(count_time);
                    }
                    scheduler.scheduleAtFixedRate(attenceRuleTask, 0, time, TimeUnit.MINUTES);
                    logger.info("===========attenceRuleTask 任务启动完成=========间隔=" + (time * 60 * 1000));


                    String endDate = DateUtils.getDate(DateUtils.getBeforeCurrentDate(1), "yyyy-MM-dd");
                    String startTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");


                    //定时计算缺勤数据
                    String lackTime = PropertiesUtils.getProperty("lack_time");
                    if (lackTime == null) {
                        lackTime = "00:05:00";
                    }
                    String endLackTime = endDate + " " + lackTime.trim();
                    long lackDiffCount = DateUtils.dateDiff(startTime, endLackTime, "yyyy-MM-dd HH:mm:ss", "m");
                    logger.info("==系统当前时间是【" + startTime + "】，生成缺勤数据任务的时间是【" + endLackTime + "】，中间间隔【" + lackDiffCount + "】分钟");
                    scheduler.scheduleAtFixedRate(makeAttenceLackTask, lackDiffCount, 24 * 60, TimeUnit.MINUTES);


                    //定时计算考勤统计任务
                    String countTime = PropertiesUtils.getProperty("count_time");
                    if (countTime == null) {
                        countTime = "00:30:00";
                    }
                    String endCountTime = endDate + " " + countTime.trim();
                    long countDiffCount = DateUtils.dateDiff(startTime, endCountTime, "yyyy-MM-dd HH:mm:ss", "m");
                    logger.info("==系统当前时间是【" + startTime + "】，定时统计考勤数据的时间是【" + endCountTime + "】，中间间隔【" + countDiffCount + "】分钟");
                    scheduler.scheduleAtFixedRate(attenceCountTask, countDiffCount, 24 * 60, TimeUnit.MINUTES);


                    //定时发送劳动合同任务
                   /* String contractTime = PropertiesUtils.getProperty("contract_time");
                    if (contractTime == null) {
                        contractTime = "08:00:00";
                    }
                    String endcontractTime = endDate + " " + contractTime.trim();
                    long contractDiffCount = DateUtils.dateDiff(startTime, endcontractTime, "yyyy-MM-dd HH:mm:ss", "m");
                    scheduler.scheduleAtFixedRate(weiXinPersonTask, contractDiffCount, 24 * 60, TimeUnit.MINUTES);
                    logger.info("===========任务启动完成=========");*/

                    //定时增加考勤积分记录
                    String countJf_time = PropertiesUtils.getProperty("countJF_time");
                    if (countJf_time == null) {
                        countJf_time = "02:00:00";
                    }
                    String endcountJf_time = endDate + " " + countJf_time.trim();
                    long countJf_timeDiffCount = DateUtils.dateDiff(startTime, endcountJf_time, "yyyy-MM-dd HH:mm:ss", "m");
                    scheduler.scheduleAtFixedRate(jfRecordTask,countJf_timeDiffCount, 24 * 60, TimeUnit.MINUTES);
                    logger.info("===========任务启动完成=========");


                } catch (Exception e) {
                    logger.info("任务启动异常", e);
                }
            }
        }

    }
}
