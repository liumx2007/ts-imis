package com.trasen.imis.task;

import com.trasen.imis.service.JfRecordService;
import com.trasen.imis.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/8
 */
@Component
public class JfRecordTask implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JfRecordService jfRecordService;

    @Override
    public void run() {
        String connt_date=DateUtils.getYearMonth();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
        Calendar cale =Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        String date_conut=DateUtils.getDate(new Date(),"yyyy-MM-dd");
        if(firstday.equals(date_conut)){
            logger.info("======================生成["+connt_date+"]考勤积分=====================");
            jfRecordService.savejfRecordFor_attence(connt_date);
            logger.info("======================生成["+connt_date+"]考勤积分成功=====================");
        }
    }
}
