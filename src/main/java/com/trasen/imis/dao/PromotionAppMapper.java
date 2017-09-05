package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbRankCheck;
import com.trasen.imis.model.TbTree;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/4
 */
public interface PromotionAppMapper {
     List<TbTree> getCompanyList();

     List<TbTree> getDeptList(String pkid);

     List<TbRankCheck> getRankCheckList(Map<String,String> param,Page page);

     int updateRankCkeck(Map<String,Object> param);

     int updatetbPersonnelJf(List<TbRankCheck> tbRankCheckList);

     int savetbPersonnelJf(List<TbJfPerson> tbJfPersonList);
}
