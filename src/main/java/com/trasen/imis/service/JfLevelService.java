package com.trasen.imis.service;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.dao.TbJfRecordMapper;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbJfRecord;
import org.apache.ibatis.ognl.InappropriateExpressionException;
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
            tbJfPerson.setOperator(VisitInfoHolder.getUserId());
            TbJfPerson jfPerson = tbJfRecordMapper.getJfPersonnel(tbJfPerson.getWorkNum());
            if(type == AppCons.SCORE&&tbJfPerson.getScore()!=null){
                if(jfPerson==null){
                    if(tbJfPerson.getScore()<0){
                        tbJfPerson.setScore(0);
                    }
                    tbJfRecordMapper.addJfPersonToScore(tbJfPerson);
                }else{
                    if(jfPerson.getScore()!=null){
                        Integer score = jfPerson.getScore()+tbJfPerson.getScore();
                        if(score<0){
                            score = 0;
                        }
                        tbJfPerson.setScore(score);
                    }else{
                        if(tbJfPerson.getScore()<0){
                            tbJfPerson.setScore(0);
                        }
                    }
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

    public boolean addJfRecord(TbJfRecord tbJfRecord){
        boolean boo = false;
        if(tbJfRecord!=null){
            tbJfRecord.setCreateUser(VisitInfoHolder.getUserId());
            tbJfRecord.setOperator(VisitInfoHolder.getUserId());
            if(tbJfRecord.getPkid()!=null){
                tbJfRecordMapper.updateJfRecord(tbJfRecord);
                boo = true;
            }else{

                Integer type = tbJfRecord.getType();
                if(type!=null){
                    if(type==AppCons.HR_ADD_SCORE){
                        tbJfRecord.setStatus(1);
                        tbJfRecordMapper.addJfRecord(tbJfRecord);
                        TbJfPerson tbJfPerson = new TbJfPerson();
                        tbJfPerson.setWorkNum(tbJfRecord.getWorkNum());
                        tbJfPerson.setScore(tbJfRecord.getScore());
                        tbJfPerson.setOperator(VisitInfoHolder.getUserId());
                        boo = saveJfPersonToScoreAndRank(tbJfPerson,AppCons.SCORE);
                    }else{
                        tbJfRecord.setStatus(0);
                        tbJfRecordMapper.addJfRecord(tbJfRecord);
                        boo = true;
                    }
                }
            }
        }

        return boo;
    }

    public Integer getScoreFromWorkNum(String workNum){
        Integer score = tbJfRecordMapper.getScoreFromWorkNum(workNum);
        if(score==null){
            score =0;
        }
        return score;
    }

    public boolean isShowRecord(TbJfRecord tbJfRecord){
        boolean boo = false;
        int num = tbJfRecordMapper.isShowRecord(tbJfRecord);
        if(num>0){
            boo = true;
        }
        return boo;
    }



}
