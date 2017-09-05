package com.trasen.imis.service;

import com.trasen.imis.dao.RecordMapper;
import com.trasen.imis.model.TbJfRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/31
 */
@Component
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;

    public List<TbJfRank> getRecordRankList(){
        return recordMapper.getRecordRankList();
    }

    public int deleteRecordRank(TbJfRank tbJfRank){
        return recordMapper.deleteRecordRank(tbJfRank);
    }

    public int insertRecordLevel(TbJfRank tbJfRank){
        return recordMapper.insertRecordLevel(tbJfRank);
    }
    public int updateRecordLevel(TbJfRank tbJfRank){
        return recordMapper.updateRecordLevel(tbJfRank);
    }

    public int deleteMoreRecordRank(List<String> pkidList){
        return recordMapper.deleteMoreRecordRank(pkidList);
    }
}
