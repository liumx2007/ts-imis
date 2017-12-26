package com.trasen.imis.task;

import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.model.TbContract;
import com.trasen.imis.service.ContractService;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/20
 */
@Component
public class WeiXinPersonTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(WeiXinPersonTask.class);

    @Autowired
    private ContractService contractService;


    @Override
    public void run() {
        logger.info("======================发送["+"劳动合同提醒"+"]开始=====================");
        contractService.sendTbContract();
        logger.info("======================发送["+"劳动合同提醒"+"]结束=====================");
    }



    public static void main(String[] args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date =new Date();
        String mytime = dateFormat.format(date);
        System.out.println(mytime);
    }


}
