package com.trasen.imis.service;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.dao.TbJfRecordMapper;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbJfRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/9/5.
 */
@Component
public class JfLevelService {

    Logger logger = Logger.getLogger(JfLevelService.class);

    @Autowired
    TbJfRecordMapper tbJfRecordMapper;

    public List<TbJfPerson> queryJfPersonnel(Map<String,Object> param){
        List<TbJfPerson> list =  tbJfRecordMapper.queryJfPersonnel(param) ;
        for(TbJfPerson person : list){
            if(person.getScore()==null){
                person.setScore(0);
            }
            if(person.getRankName()==null){
                person.setRankName("初级未转");
            }
        }
        return list;
    }

    public List<TbJfRecord> seachJfRecord(Map<String,Object> param,Page page){
        List<TbJfRecord> list = new ArrayList<>();
        if(param!=null&&param.get("workNum")!=null){
            list = tbJfRecordMapper.seachJfRecord(param,page);
        }
        return list;
    }

    public boolean saveJfPersonToScoreAndRank(TbJfPerson tbJfPerson,Integer type){
        boolean boo = false;
        if(tbJfPerson!=null&&tbJfPerson.getWorkNum()!=null&&type!=null){
            TbJfPerson jfPerson = tbJfRecordMapper.getJfPersonnel(tbJfPerson.getWorkNum());
            if(type == AppCons.SCORE&&tbJfPerson.getScore()!=null){
                if(jfPerson==null){
                    tbJfRecordMapper.addJfPersonToScore(tbJfPerson);
                }else{
                    tbJfRecordMapper.updateJfPersonToScore(tbJfPerson);
                }
                boo = true;
            }else if(type == AppCons.RANK&&tbJfPerson.getRank()!=null){
                if(jfPerson==null){
                    tbJfRecordMapper.addJfPersonToRank(tbJfPerson);
                }else{
                    tbJfRecordMapper.updateJfPersonToRank(tbJfPerson);
                }
                boo = true;
            }
        }
        return boo;
    }

    public String getRankName(Integer pkid){
        return tbJfRecordMapper.getRankName(pkid);
    }



}
