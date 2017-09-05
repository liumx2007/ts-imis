package com.trasen.imis.dao;

import com.trasen.imis.model.TbJfRank;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/31
 */
public interface RecordMapper {
    public List<TbJfRank> getRecordRankList();

    public int deleteRecordRank(TbJfRank tbJfRank);

    public int updateRecordLevel(TbJfRank tbJfRank);

    public int insertRecordLevel(TbJfRank tbJfRank);

    public int deleteMoreRecordRank(List<String> pkidList);
}
