package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.AttenceDetailVo;
import com.trasen.imis.model.AttenceLeave;
import com.trasen.imis.model.TbAttenceCount;
import com.trasen.imis.model.TbWeixinUser;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/6.
 */
public interface TbAttenceCountMapper {

    List<TbWeixinUser> queryWeixinUser();

    int getFactSignNum(Map<String,Object> paraMap);

    int getAttNum(Map<String,Object> paraMap);

    int getLateNum(Map<String,Object> paraMap);

    int getBackNum(Map<String,Object> paraMap);

    int getLackNum(Map<String,Object> paraMap);

    List<AttenceLeave> queryAttenceLeaveList(Map<String,Object> paraMap);

    void saveAttenceCount(TbAttenceCount count);

    void updateAttenceCount(TbAttenceCount count);

    TbAttenceCount getAttenceCount(Map<String,Object> paraMap);

    List<TbAttenceCount> queryAttenceCountList(TbAttenceCount tbAttenceCount, Page page);

    List<TbAttenceCount> queryAttenceCountList(TbAttenceCount tbAttenceCount);

    List<AttenceDetailVo> queryAttenceDetail(Map<String,Object> param, Page page);

    List<AttenceDetailVo> queryLackDetail(Map<String,Object> param, Page page);

    List<AttenceDetailVo> queryLeaveDetail(Map<String,Object> param, Page page);
}
