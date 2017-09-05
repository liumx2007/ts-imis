package com.trasen.imis.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.cache.GlobalCache;
import com.trasen.imis.dao.TbHolidayMapper;
import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.model.TbHoliday;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.WorkDateutil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/27
 */
@Component
public class TbHolidayService {

    Logger logger = Logger.getLogger(TbHolidayService.class);
    @Autowired
    private TbHolidayMapper tbHolidayMapper;

    @Autowired
    private AttenceService attenceService;

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    public String saveByList(String yearMonth){
        String workDay="";
        String holiday="";
        if(yearMonth==null){
            return "参数错误";
        }else{
            int count=tbHolidayMapper.deleteByYearMonth(yearMonth);
            if(count<0) return "删除失败";
            List<TbHoliday> tbHolidayList=new ArrayList<TbHoliday>();
            for(int i=1;i<=12;i++){
                TbHoliday tbHoliday=new TbHoliday();
                String year_Month=yearMonth+"-"+i;
                JSONObject jsonObject=WorkDateutil.getRequest2(year_Month);
                if(jsonObject==null) return "接口错误";
                JSONObject jsonObject1=jsonObject.getJSONObject("result");
                if (jsonObject1==null){
                    tbHoliday.setYearMonth(year_Month);
                }else{
                    JSONObject jsonObject2=jsonObject1.getJSONObject("data");
                    JSONArray jsonArray=JSONObject.parseArray(jsonObject2.getString("holiday"));
                    StringBuffer buffer=new StringBuffer();
                    StringBuffer bufferwork=new StringBuffer();
                    for(int j=0;j<jsonArray.size();j++){
                        JSONObject jsonObject3= (JSONObject) jsonArray.get(j);
                        JSONArray jsonArray1=jsonObject3.getJSONArray("list");
                        for(int k=0;k<jsonArray1.size();k++){
                            JSONObject jsonObject4= (JSONObject) jsonArray1.get(k);
                            if(jsonObject4.getString("status").equals("1")){
                                if(buffer.indexOf(jsonObject4.getString("date"))<0){
                                    if(jsonObject4.getString("date").indexOf(year_Month)>=0) buffer.append(jsonObject4.getString("date"));
                                }
                                if(k+1!=jsonArray1.size()){
                                    buffer.append(",");
                                }
                            }else{
                                if(bufferwork.indexOf(jsonObject4.getString("date"))<0){
                                    if(jsonObject4.getString("date").indexOf(year_Month)>=0) bufferwork.append(jsonObject4.getString("date"));
                                }
                                if(k+1!=jsonArray1.size()){
                                    bufferwork.append(",");
                                }
                            }
                        }
                    }
                    workDay=bufferwork.toString();
                    holiday=buffer.toString();
                    tbHoliday.setYearMonth(year_Month);
                    tbHoliday.setHoliday(holiday);
                    tbHoliday.setWorkDay(workDay);
                }
                tbHolidayList.add(tbHoliday);
            }
            int insertCount=tbHolidayMapper.saveByList(tbHolidayList);
            if(insertCount>0){
                return "保存成功";
            }else{
                return "保存失败";
            }
        }
    }

    public int getDaysforYearMonth(String yearMonth,String tagId) {
        TbHoliday tbHoliday=null;
        if(globalCache.getTbHolidayMap()==null){
            tbHoliday= tbHolidayMapper.selectCountByYearMonth(yearMonth);
            if(tbHoliday==null){
                return 0;
            }
            Map<String,TbHoliday> tbHolidayMap=new HashMap<String,TbHoliday>();
            tbHolidayMap.put("tbHoliday",tbHoliday);
            globalCache.setTbHolidayMap(tbHolidayMap);
        }
        tbHoliday=globalCache.getTbHolidayMap().get("tbHoliday");
        logger.info("==================节假日信息"+tbHoliday.getYearMonth());
        List<String> listHoliday=new ArrayList<String>();
        List<String> listworDay=new ArrayList<String>();
        List<String> listoutDay=new ArrayList<String>();
        List<String> listworkingDay=new ArrayList<String>();
        TbAttenceLocation rule=null;
        if(tbHoliday==null){
            logger.info("==================节假日查询失败");
            return 0;
        }
        if (tbHoliday.getHoliday() != null&&tbHoliday.getHoliday()!="") {
            String[]  arrayHoliday = tbHoliday.getHoliday().split(",");
            Collections.addAll(listHoliday, arrayHoliday);
        }
        if (tbHoliday.getWorkDay() != null&&tbHoliday.getWorkDay()!="") {
            String[]  arrayworkDay = tbHoliday.getWorkDay().split(",");
            Collections.addAll(listworDay, arrayworkDay);
        }
        if (tagId != null && !StringUtil.isEmpty(tagId)) {
             rule = attenceService.getAttenceRule(tagId, globalCache.getAttRuleMap());
            if (rule == null) {
                logger.info("==================规则数据为空");
                return 0;
            }
            if(rule.getOutDate()!=null&&rule.getOutDate()!=""){
                String[] outDate=rule.getOutDate().split(",");
                Collections.addAll(listoutDay, outDate);
            }
            if(rule.getWorkingDay()!=null&&rule.getWorkingDay()!=""){
                listworkingDay=getWorkDayForRue(rule.getWorkingDay(),yearMonth);
            }

        }

        listHoliday.addAll(listoutDay);
        logger.info("==================listHoliday"+listHoliday.size());
        for(int i=0;i<listHoliday.size();i++){
            for(int j=0;j<listworkingDay.size();j++){
                if(listworkingDay.get(j).equals(listHoliday.get(i))){
                    listworkingDay.remove(j);
                    j--;
                }
            }
        }
        logger.info("==================listHoliday"+listHoliday.size());
        for(int i=0;i<listworDay.size();i++){
            if(!listworkingDay.contains(listworDay.get(i))){
                listworkingDay.add(listworDay.get(i));
            }
        }
        logger.info("==================listworkingDay"+listworkingDay.size());
        return listworkingDay.size();
    }

    public List<String> getWorkDayForRue(String workingDay, String yearMonth) {
        logger.info("==================getWorkDayForRue--workingDay" + workingDay);
        logger.info("==================getWorkDayForRue--yearMonth" + yearMonth);
        Calendar time = Calendar.getInstance();
        time.clear();
        String[] year_month = yearMonth.split("-");
        time.set(Calendar.YEAR, Integer.valueOf(year_month[0]));
        //year年
        time.set(Calendar.MONTH, Integer.valueOf(year_month[1]) - 1);
        //Calendar对象默认一月为0,month月
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        logger.info("============本月天数:" + day);
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        List<String> workdayList = new ArrayList<String>();
        String[] working_day = workingDay.split(",");
        logger.info("============working_day信息:" + working_day);
        for(int i=0;i<working_day.length;i++){
            String week=working_day[i].split("")[1];
            for(int k = 1; k <=day; k++){
                try {
                    Date date = sdf1.parse(yearMonth + "-" +k);
                    String weeks= DateUtils.getWeek(date);
                    if(weeks.indexOf(week)>=0){
                        workdayList.add(sdf1.format(date));
                    }
                } catch (ParseException e) {
                    //do nothing
                }
            }
        }
        logger.info("==================工作日List" + workdayList.size());
        return workdayList;

    }
}
