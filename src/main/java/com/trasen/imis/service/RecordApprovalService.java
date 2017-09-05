package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.dao.PromotionAppMapper;
import com.trasen.imis.dao.RecordApprovalMapper;
import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.model.TbRankCheck;
import com.trasen.imis.model.TbTree;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
}
