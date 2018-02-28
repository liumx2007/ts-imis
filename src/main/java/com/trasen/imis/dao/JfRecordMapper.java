package com.trasen.imis.dao;

import com.trasen.imis.model.TbAttenceCount;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/8
 */
public interface JfRecordMapper {
    List<TbAttenceCount> getattcenCounttList(String count_date);

    int getSystemBonus(Map<String,String> param);

    Integer getAddWorkNum(Map<String,Object> param);
}
