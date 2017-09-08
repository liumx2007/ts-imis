package com.trasen.imis.task;

import com.trasen.imis.service.JfRecordService;
import com.trasen.imis.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        logger.info("======================生成["+connt_date+"]考勤积分=====================");
        jfRecordService.savejfRecordFor_attence(connt_date);
        logger.info("======================生成["+connt_date+"]考勤积分成功=====================");

    }
}
