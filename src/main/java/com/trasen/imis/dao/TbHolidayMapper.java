package com.trasen.imis.dao;

import com.trasen.imis.model.TbHoliday;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/27
 */
public interface TbHolidayMapper {

    public TbHoliday selectCountByYearMonth(String yearMonth);
    public int saveByList(List<TbHoliday> tbHolidayList);
    public int deleteByYearMonth(String yearMonth);
}
