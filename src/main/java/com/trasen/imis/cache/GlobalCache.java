package com.trasen.imis.cache;

import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.model.TbHoliday;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GlobalCache {

    protected Logger logger = Logger.getLogger(getClass());


    private static GlobalCache globalCache = new GlobalCache();

    private Map<String, TbAttenceLocation> attRuleMap;

    private  Map<String,TbHoliday> tbHolidayMap;

    private  Map<String,String> holiday;


    private GlobalCache() {
        attRuleMap = new HashMap<>();
        holiday = new HashMap<>();
    }


    public static GlobalCache getGlobalCache() {
        return globalCache;
    }


    public Map<String, TbAttenceLocation> getAttRuleMap() {
        return attRuleMap;
    }

    public void setAttRuleMap(Map<String, TbAttenceLocation> attRuleMap) {
        this.attRuleMap = attRuleMap;
    }

    public void setTbHolidayMap(Map<String, TbHoliday> tbHolidayMap) {
        this.tbHolidayMap = tbHolidayMap;
    }

    public Map<String, TbHoliday> getTbHolidayMap() {
        return tbHolidayMap;
    }

    public Map<String, String> getHoliday() {
        return holiday;
    }

    public void setHoliday(Map<String, String> holiday) {
        this.holiday = holiday;
    }
}
