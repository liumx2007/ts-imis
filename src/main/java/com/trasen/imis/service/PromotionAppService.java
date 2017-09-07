package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.dao.PromotionAppMapper;
import com.trasen.imis.dao.PromotionMapper;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbRankCheck;
import com.trasen.imis.model.TbTree;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/4
 */
@Component
public class PromotionAppService {

    Logger logger = Logger.getLogger(PromotionAppService.class);

    @Autowired
    private PromotionAppMapper promotionAppMapper;

    @Autowired
    private PromotionMapper promotionMapper;

    public List<TbTree> getCompanyList(){
        return promotionAppMapper.getCompanyList();
    }

    public List<TbTree> getDeptList(String pkid){
        List<TbTree> tbList=new ArrayList<TbTree>();
        if(pkid!=null){
            List<TbTree> treeDeptList=promotionAppMapper.getDeptList(pkid);
            tbList.addAll(treeDeptList);
            for(TbTree tbTree:treeDeptList){
                List<TbTree> tb=getDeptList(tbTree.getPkid());
                 tbList.addAll(tb);
            }

        }
        return tbList;
    }

    public List<TbRankCheck> getRankCheckList(Map<String,String> param, Page page) {
        List<TbRankCheck> tbRankCheckList = promotionAppMapper.getRankCheckList(param, page);
        List<TbTree> tbTreeList = promotionAppMapper.getCompanyList();
        for (TbRankCheck tbRankCheck : tbRankCheckList){
            for (TbTree tbTree : tbTreeList) {
                if (tbRankCheck.getTagCode().indexOf(tbTree.getPkid()) >= 0) {
                    tbRankCheck.setCompany(tbTree.getName());
                    logger.info("获取公司====" + tbTree.getName());
                    break;
                }
            }
        }
        return tbRankCheckList;
    }

    public int updateRankCkeck(List<TbRankCheck> tbRankCheckList,int status){
       Map<String,Object> param=new HashMap<>();
       param.put("status",status);
       param.put("updated",new Date());
       param.put("operator",VisitInfoHolder.getUserId());
       param.put("remark",tbRankCheckList.get(0).getRemark());
       param.put("list",tbRankCheckList);
       List<TbRankCheck> updateperjf=new ArrayList<>();
       List<TbJfPerson> saveperjf=new ArrayList<>();
       if(AppCons.PROMOTION_AGREE==status){
           for(TbRankCheck tbRankCheck:tbRankCheckList){
               int count=promotionMapper.getPersonJfCount(tbRankCheck.getWorkNum());
               if(count>0){
                   tbRankCheck.setUpdated(new Date());
                   tbRankCheck.setOperator(VisitInfoHolder.getUserId());
                   updateperjf.add(tbRankCheck);
               }else{
                   TbJfPerson tbJfPerson=new TbJfPerson();
                   tbJfPerson.setWorkNum(tbRankCheck.getWorkNum());
                   tbJfPerson.setRank(tbRankCheck.getNewRank());
                   tbJfPerson.setCreated(new Date());
                   saveperjf.add(tbJfPerson);
               }
           }
       }
        if(updateperjf.size()>0){
            promotionAppMapper.updatetbPersonnelJf(updateperjf);
        }
        if(saveperjf.size()>0){
            promotionAppMapper.savetbPersonnelJf(saveperjf);
        }
       int updateCount=promotionAppMapper.updateRankCkeck(param);
       return updateCount;
    }
}
