package com.trasen.imis.task;

import com.trasen.imis.cache.GlobalCache;
import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.service.AttenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zhangxiahui on 17/6/26.
 */
@Component
public class AttenceRuleTask implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    @Autowired
    AttenceService attenceService;


    @Override
    public void run() {
        logger.info("======================进入规则缓存定时任务=====================");
        Map<String,TbAttenceLocation> attRuleMap = attenceService.getAttenceRuleData();
        globalCache.setAttRuleMap(attRuleMap);
        logger.info("======================["+attRuleMap.size()+"]条规则加入缓存=====================");
    }
}
