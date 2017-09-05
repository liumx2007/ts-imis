package com.trasen.imis.task;

import cn.trasen.commons.util.DateUtil;
import com.trasen.imis.cache.GlobalCache;
import com.trasen.imis.service.AttenceService;
import com.trasen.imis.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangxiahui on 17/6/27.
 */
@Component
public class MakeAttenceLackTask  implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    @Autowired
    AttenceService attenceService;


    @Override
    public void run() {
        String date = DateUtils.getDate(DateUtils.getBeforeCurrentDate(-1),"yyyy-MM-dd");
        logger.info("======================生成缺勤数据定时任务["+date+"]=====================");
        boolean boo = attenceService.makeAttenceLack(date);
        logger.info("======================生成缺勤数据定时任务["+boo+"]=====================");
    }
}
