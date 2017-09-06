package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.dao.PromotionAppMapper;
import com.trasen.imis.dao.PromotionMapper;
import com.trasen.imis.dao.RecordApprovalMapper;
import com.trasen.imis.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/5
 */
@Component
public class RecordApprovalService {

    Logger logger = Logger.getLogger(RecordApprovalService.class);

    @Autowired
    private RecordApprovalMapper recordApprovalMapper;

    @Autowired
    private PromotionAppMapper promotionAppMapper;

    @Autowired
    private PromotionMapper promotionMapper;

    public List<TbJfRecord> getRecordApprovalList(Map<String,String> param, Page page){

        List<TbJfRecord> tbJfRecordList=recordApprovalMapper.getRecordApprovalList(param,page);
        List<TbTree> tbTreeList = promotionAppMapper.getCompanyList();
        for (TbJfRecord tbJfRecord : tbJfRecordList){
            for (TbTree tbTree : tbTreeList) {
                if (tbJfRecord.getTagCode().indexOf(tbTree.getPkid()) >= 0) {
                    tbJfRecord.setCompany(tbTree.getName());
                    logger.info("获取公司====" + tbTree.getName());
                    break;
                }
            }
        }
        return tbJfRecordList;
    }

    public void updateJfRecrod(List<TbJfRecord> tbJfRecordList,int status){
            for(TbJfRecord tbJfRecord:tbJfRecordList){
                if(AppCons.RECORDAPP_AGREE==status) {
                    int count = promotionMapper.getPersonJfCount(tbJfRecord.getWorkNum());
                    TbJfPerson tbJfPerson = new TbJfPerson();
                    if (count > 0) {
                        tbJfPerson.setUpdated(new Date());
                        tbJfPerson.setOperator(VisitInfoHolder.getUserId());
                        tbJfPerson.setWorkNum(tbJfRecord.getWorkNum());
                        if(tbJfRecord.getCurrentScore()==null){
                            tbJfPerson.setScore(tbJfRecord.getScore());
                        }else{
                            tbJfPerson.setScore(tbJfRecord.getScore()+tbJfRecord.getCurrentScore());
                        }

                        recordApprovalMapper.updatePerjfScore(tbJfPerson);
                    } else {
                        tbJfPerson.setWorkNum(tbJfRecord.getWorkNum());
                        tbJfPerson.setScore(tbJfRecord.getScore());
                        tbJfPerson.setCreated(new Date());
                        promotionMapper.savaPersonJf(tbJfPerson);
                    }
                }
                tbJfRecord.setStatus(status);
                tbJfRecord.setOperator(VisitInfoHolder.getUserId());
                tbJfRecord.setUpdated(new Date());
                recordApprovalMapper.updateJfRecord(tbJfRecord);
        }
    }
}
