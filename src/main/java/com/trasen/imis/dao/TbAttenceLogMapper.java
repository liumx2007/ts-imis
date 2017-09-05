package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.AttenceLogVo;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/6/21.
 */
public interface TbAttenceLogMapper {

    void insertAttenceLog(AttenceLogVo attenceLogVo);

    List<AttenceLogVo> queryAttLogList(AttenceLogVo attenceLogVo);

    public List<AttenceLogVo> getAttenceLogList(Map<String,Object> attenceLogMap, Page page);
}
