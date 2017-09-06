package com.trasen.imis.service;

import com.trasen.imis.dao.PromotionMapper;
import com.trasen.imis.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/29
 */
@Component
public class WeiXinPromotionService {

    Logger logger = Logger.getLogger(WeiXinPromotionService.class);
    @Autowired
    private PromotionMapper promotionMapper;


    public TbJfPerson getJfPersonByopenId(String openId){
        logger.info("方法getJfPersonByopenId===="+openId);
        TbJfPerson tbJfPerson=promotionMapper.getJfPersonByopenId(openId);
        if(tbJfPerson!=null){
            List<TbTree> tbTreeList = promotionMapper.selectCompany();
            for (TbTree tbTree : tbTreeList) {
                if (tbJfPerson.getTagCode().indexOf(tbTree.getPkid()) >= 0) {
                    tbJfPerson.setCompany(tbTree.getName());
                    logger.info("获取公司====" + tbTree.getName());
                    break;
                }
            }
            List<TbJfRank> tbJfRankList = promotionMapper.selectRankList();
            if (tbJfPerson.getPx() == null) {
                logger.info("====初始化未转");
                tbJfPerson.setPx(tbJfRankList.get(0).getPx());
                tbJfPerson.setRank(tbJfRankList.get(0).getPkid());
                tbJfPerson.setRankName(tbJfRankList.get(0).getName() + tbJfRankList.get(0).getType());
                tbJfPerson.setScore(0);
                tbJfPerson.setNextRankName(tbJfRankList.get(1).getName() + tbJfRankList.get(1).getType());
                tbJfPerson.setPrmScore(tbJfRankList.get(1).getScore());
                tbJfPerson.setNextRank(tbJfRankList.get(1).getPkid());
            } else {
                if (tbJfPerson.getPx() == tbJfRankList.get(0).getPx() && tbJfPerson.getPrmScore() == tbJfRankList.get(0).getPrmScore()) {
                    logger.info("====初级一级");
                    tbJfPerson.setNextRankName(tbJfRankList.get(1).getName() + tbJfRankList.get(1).getType());
                    tbJfPerson.setPrmScore(tbJfRankList.get(1).getScore());
                    tbJfPerson.setNextRank(tbJfRankList.get(1).getPkid());
                } else {
                    TbJfRank tbJfRank = promotionMapper.selectNextRank(tbJfPerson.getPx() + 1);
                    if (tbJfRank != null) {
                        logger.info("获取下一级====" + tbJfRank.getName() + tbJfRank.getType());
                        tbJfPerson.setNextRankName(tbJfRank.getName() + tbJfRank.getType());
                        tbJfPerson.setNextRank(tbJfRank.getPkid());
                    }
                }
            }
            if (promotionMapper.selectTbCheckCount(tbJfPerson.getWorkNum()) > 0) {
                tbJfPerson.setStatus("1");
            } else {
                tbJfPerson.setStatus("0");
            }
        }
        return tbJfPerson;
    }

    public void savaCheck(TbRankCheck tbRankCheck){
        if(promotionMapper.getPersonJfCount(tbRankCheck.getWorkNum())>0){
            logger.info("TbJfPerson不为空====");
            promotionMapper.savaCheck(tbRankCheck);
            logger.info("晋级申请保存成功====");
        }else{
            TbJfPerson tbJfPerson=new TbJfPerson();
            tbJfPerson.setRank(tbRankCheck.getOldRank());
            tbJfPerson.setWorkNum(tbRankCheck.getWorkNum());
            tbJfPerson.setCreated(tbRankCheck.getCreated());
            promotionMapper.savaPersonJf(tbJfPerson);
            promotionMapper.savaCheck(tbRankCheck);
            logger.info("人员积分和晋级申请保存成功====");
        }

    }

    public List<TbJfRecord> getJfRecordByOpendId(String openId){
        return promotionMapper.getJfRecordByOpendId(openId);
    }

    public TbJfPerson getPersonByopenId(String openId){
        return promotionMapper.getPersonByopenId(openId);
    }

}
