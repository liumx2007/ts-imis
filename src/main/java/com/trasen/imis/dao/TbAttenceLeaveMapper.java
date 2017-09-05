package com.trasen.imis.dao;

import com.trasen.imis.model.AttenceLeave;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/4
 */
public interface TbAttenceLeaveMapper {
    public int insertAttenceLeaveList(List<AttenceLeave> attenceLeaveList);
}
