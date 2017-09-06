package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbJfRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/9/5.
 */
public interface TbJfRecordMapper {

    List<TbJfPerson> queryJfPersonnel(Map<String,Object> param);

    List<TbJfRecord> seachJfRecord(Map<String,Object> param,Page page);

    TbJfPerson getJfPersonnel(String workNum);

    int addJfPersonToScore(TbJfPerson tbJfPerson);

    int addJfPersonToRank(TbJfPerson tbJfPerson);

    int updateJfPersonToScore(TbJfPerson tbJfPerson);

    int updateJfPersonToRank(TbJfPerson tbJfPerson);

    String getRankName(Integer pkid);




}
