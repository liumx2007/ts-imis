package com.trasen.imis.dao;

import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.model.TbPerformance;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 18/2/26.
 */
public interface TbPerformanceMapper {

    void savePerformance(Map<String,Object> param);

    Integer getPerformanceDate(String importDate);

    void updatePerformanceJF(Map<String,Object> param);

    List<Map<String,Object>> getJF();

    List<TbPerformance> queryPerformance(String date);

    List<TbJfRecord> getJfRecord(String date);

    List<TbPerformance> getPerformanceToDate(String date);

    Integer countPerforJF(String date);

    void updatePerformance(Integer pkid);

    void updatePerformanceToTwo(String date);
}
