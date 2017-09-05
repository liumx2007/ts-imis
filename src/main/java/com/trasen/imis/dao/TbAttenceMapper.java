package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.AttenceVo;
import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.model.TbPersonnel;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/6/21.
 */
public interface TbAttenceMapper {

    AttenceVo getWeixinUser(String openId);

    AttenceVo getAttenceToday(AttenceVo attenceVo);

    void insertAttence(AttenceVo attenceVo);

    void updateAttenceSignoutTime(AttenceVo attenceVo);

    List<AttenceVo> searchAttList(AttenceVo attenceVo,Page page);

    List<AttenceVo> searchLackAttList(AttenceVo attenceVo,Page page);
    List<AttenceVo> searchActualLackAttList(AttenceVo attenceVo,Page page);
    Integer lateActualNum(AttenceVo attenceVo);

    List<AttenceVo> searchAttList(AttenceVo attenceVo);

    List<AttenceVo> searchLackAttList(AttenceVo attenceVo);
    List<AttenceVo> searchActualLackAttList(AttenceVo attenceVo);

    List<TbAttenceLocation> getAttenceLoactionList();

    List<String> queryAttenceToDate(String date);

    List<AttenceVo> queryWeixinUser();

    Integer selectLackAttence(String date);

    void insertLackAttence(Map<String,Object> map);

    List<AttenceVo> searchLeaveAttList(AttenceVo attenceVo,Page page);

    List<AttenceVo> searchLeaveAttList(AttenceVo attenceVo);

    void deleAttence(Long pkid);

    void deleLackAttence(Long pkid);

    Integer signInNum(AttenceVo attenceVo);

    Integer lateNum(AttenceVo attenceVo);

    Integer backNum(AttenceVo attenceVo);

    Integer lackNum(AttenceVo attenceVo);

    Integer addWorkNum(AttenceVo attenceVo);

    Integer leaveNum(AttenceVo attenceVo);

    List<TbPersonnel> getSubPerson(AttenceVo attenceVo);

    Integer getUserIdToOPenId(String openId);
}

