package com.trasen.imis.service;

import cn.trasen.commons.util.StringUtil;
import com.trasen.imis.dao.TbPerformanceMapper;
import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.model.TbPerformance;
import com.trasen.imis.model.TbPerformanceJF;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 18/2/26.
 */
@Component
public class TbPerformanceService {

    Logger logger = Logger.getLogger(TbPerformanceService.class);

    @Autowired
    TbPerformanceMapper tbPerformanceMapper;

    @Autowired
    JfLevelService jfLevelService;


    public void savePerformance(List<TbPerformance> list){
        Map<String,Object> param = new HashMap<>();
        param.put("list",list);
        tbPerformanceMapper.savePerformance(param);
    }

    public boolean isImport(String date){
        Integer num = tbPerformanceMapper.getPerformanceDate(date);
        if(num==0){
            return false;
        }else{
            return true;
        }
    }

    public void updatePerformanceJF(List<Map<String,Object>> list){
        if(list!=null&&list.size()>0){
            for(Map<String,Object> map : list){
                tbPerformanceMapper.updatePerformanceJF(map);
            }
        }
    }

    public TbPerformanceJF getJF(){
        TbPerformanceJF jf = new TbPerformanceJF();
        List<Map<String,Object>> list = tbPerformanceMapper.getJF();
        for(Map<String,Object> map : list){
            if("fine".equals(map.get("code").toString())){
                Double value = Double.parseDouble(map.get("value").toString());
                jf.setFine(value);
            }
            if("good".equals(map.get("code").toString())){
                Double value = Double.parseDouble(map.get("value").toString());
                jf.setGood(value);
            }
            if("middling".equals(map.get("code").toString())){
                Double value = Double.parseDouble(map.get("value").toString());
                jf.setMiddling(value);
            }
            if("pass".equals(map.get("code").toString())){
                Double value = Double.parseDouble(map.get("value").toString());
                jf.setPass(value);
            }
            if("flunk".equals(map.get("code").toString())){
                Double value = Double.parseDouble(map.get("value").toString());
                jf.setFlunk(value);
            }
        }
        return jf;
    }

    public List<TbPerformance> queryPerformance(){
        return tbPerformanceMapper.queryPerformance();
    }

    public List<TbJfRecord> getJfRecordf(String date){
        return tbPerformanceMapper.getJfRecord(date);
    }

    public List<TbPerformance> getPerformanceToDate(String date){
        return tbPerformanceMapper.getPerformanceToDate(date);
    }

    public Integer countPerforJF(String date){
        return tbPerformanceMapper.countPerforJF(date);
    }

    public int autoAddJf(List<TbJfRecord> tbJfRecordList,List<TbPerformance> tbPerformances,String date){
        Map<String,Double> jfMap = new HashMap<>();
        Map<String,TbPerformance> performanceMap = new HashMap<>();
        int num = 0;
        //绩效系数
        List<Map<String,Object>> list = tbPerformanceMapper.getJF();
        for(Map<String,Object> map : list){
            jfMap.put(map.get("name").toString(),Double.parseDouble(map.get("value").toString()));
        }

        //工号查绩效
        for(TbPerformance performance : tbPerformances){
            performanceMap.put(performance.getWorkNum(),performance);
        }

        for(TbJfRecord jfRecord : tbJfRecordList){
            DecimalFormat df = new DecimalFormat("0");
            String remark = date.substring(0,4)+"年"+date.substring(4,6)+"月";


            TbPerformance performance = performanceMap.get(jfRecord.getWorkNum());

            if(performance!=null&&performance.getGrade()!=null){
                Double jf = jfMap.get(performance.getGrade().substring(0,2));
                if(jf!=null){
                    Double scoreD = jfRecord.getScore()*jf - jfRecord.getScore();
                    Integer score = Integer.parseInt(df.format(scoreD));
                    if(score>0){
                        remark = remark+"绩效加"+score+"分（"+performance.getGrade()+"）。";
                    }else if(score<0){
                        remark = remark+"绩效减"+Math.abs(score)+"分（"+performance.getGrade()+"）。";
                    }else{
                        continue;
                    }
                    TbJfRecord tbJfRecord = new TbJfRecord();
                    tbJfRecord.setWorkNum(jfRecord.getWorkNum());
                    tbJfRecord.setType(4);
                    tbJfRecord.setScore(score);
                    tbJfRecord.setRemark(remark);
                    tbJfRecord.setStatus(1);
                    tbJfRecord.setCreated(new Date());
                    tbJfRecord.setIsShow(1);
                    tbJfRecord.setCreateUser("系统");
                    jfLevelService.addJfRecord(tbJfRecord);
                    tbPerformanceMapper.updatePerformance(performance.getPkid());
                    num++;
                }

            }

        }
        tbPerformanceMapper.updatePerformanceToTwo(date);
        return num;
    }








}
