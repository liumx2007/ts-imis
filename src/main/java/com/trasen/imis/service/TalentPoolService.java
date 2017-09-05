package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.dao.TbTalentPoolMapper;
import com.trasen.imis.model.AttenceVo;
import com.trasen.imis.model.TbTalentPool;
import com.trasen.imis.model.TbWorkHistory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangxiahui on 17/7/18.
 */
@Component
public class TalentPoolService {

    Logger logger = Logger.getLogger(TalentPoolService.class);

    @Autowired
    TbTalentPoolMapper tbTalentPoolMapper;


    public List<TbTalentPool> searchTalentPoolList(TbTalentPool tbTalentPool, Page page){
        List<TbTalentPool> list = new ArrayList<>();
        if(tbTalentPool!=null){
            list = tbTalentPoolMapper.searchTalentPoolList(tbTalentPool,page);
        }
        return list;
    }

    public List<TbTalentPool> searchTalentPoolList(TbTalentPool tbTalentPool){
        List<TbTalentPool> list = new ArrayList<>();
        if(tbTalentPool!=null){
            list = tbTalentPoolMapper.searchTalentPoolList(tbTalentPool);
        }
        return list;
    }

    public void saveTalentPool(TbTalentPool tbTalentPool){
        if(tbTalentPool!=null){
            if(tbTalentPool.getWorkDate()!=null){
                tbTalentPool.setWorkDate(tbTalentPool.getWorkDate().substring(0,10));
            }
            if(tbTalentPool.getPkid()==null){
                tbTalentPool.setCreated(new Date());
                tbTalentPool.setOperator(VisitInfoHolder.getUserId());
                tbTalentPoolMapper.saveTalentPool(tbTalentPool);
            }else{
                tbTalentPool.setUpdated(new Date());
                tbTalentPool.setOperator(VisitInfoHolder.getUserId());
                tbTalentPoolMapper.updateTalentPool(tbTalentPool);
            }
        }
    }

    public void deleteTalentPool(Integer pkid){
        if(pkid !=null){
            tbTalentPoolMapper.deleteTalentPool(pkid);
        }
    }

    public List<TbWorkHistory> queryWorkHistory(TbWorkHistory tbWorkHistory, Page page){
        List<TbWorkHistory> list = new ArrayList<>();
        if(tbWorkHistory!=null){
            list = tbTalentPoolMapper.queryWorkHistory(tbWorkHistory,page);
        }
        return list;
    }

    public void saveWorkHistory(TbWorkHistory tbWorkHistory){
        if(tbWorkHistory!=null){
            if(tbWorkHistory.getPkid()==null){
                tbWorkHistory.setCreated(new Date());
                tbWorkHistory.setOperator(VisitInfoHolder.getUserId());
                tbTalentPoolMapper.insertWorkHistory(tbWorkHistory);
            }else{
                tbWorkHistory.setUpdated(new Date());
                tbWorkHistory.setOperator(VisitInfoHolder.getUserId());
                tbTalentPoolMapper.updateWorkHistory(tbWorkHistory);
            }

        }

    }

    public void deleteWorkHistory(Integer pkid){
        if (pkid!=null){
            tbTalentPoolMapper.deleteWorkHistory(pkid);
        }
    }

}
