package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbTalentPool;
import com.trasen.imis.model.TbWorkHistory;

import java.util.List;

/**
 * Created by zhangxiahui on 17/7/18.
 */
public interface TbTalentPoolMapper {

    List<TbTalentPool> searchTalentPoolList(TbTalentPool tbTalentPool);

    List<TbTalentPool> searchTalentPoolList(TbTalentPool tbTalentPool, Page page);

    void saveTalentPool(TbTalentPool tbTalentPool);

    void updateTalentPool(TbTalentPool tbTalentPool);

    void deleteTalentPool(Integer pkid);

    List<TbWorkHistory> queryWorkHistory(TbWorkHistory tbWorkHistory, Page page);

    void insertWorkHistory(TbWorkHistory tbWorkHistory);

    void updateWorkHistory(TbWorkHistory tbWorkHistory);

    void deleteWorkHistory(Integer pkid);
}
