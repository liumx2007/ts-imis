package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/11.
 */
public interface TbPersonnelMapper {

    List<TbPersonnel> queryPersonnelList(Map<String, Object> params, Page page);

    List<TbPersonnel> queryPersonnelList(Map<String, Object> params);

    List<TbPersonnel> queryAddresssPersonnelList(Map<String, Object> params);

    void getPkid(TbPkid pkid);

    void insertPersonnel(TbPersonnel tbPersonnel);

    void updatePersonnel(TbPersonnel tbPersonnel);

    void deletePersonnel(String perId);

    String getParentDepId(String depId);

    TbPersonnel getPersonnelBasic(String perId);

    TbPersonnel getPersonnelFile(String perId);

    void insertPersonnelBasic(TbPersonnel tbPersonnel);

    void updatePersonnelBasic(TbPersonnel tbPersonnel);

    void insertPersonnelFile(TbPersonnel tbPersonnel);

    void updatePersonnelFile(TbPersonnel tbPersonnel);

    int findWorkNumRepeat(String workNum);

    TbPersonnel getPersonnelForWorkNum(String workNum);

    void insertDeptLog(TbDeptLog tbDeptLog);

    List<TbDeptLog> queryDeptLog(TbDeptLog tbDeptLog, Page page);

    void updateWeixinUser(AttenceVo attenceVo);

    void insertWeixinUser(AttenceVo attenceVo);

    AttenceVo getWeixinUser(String workNum);

    void deleteWeixinUser(String workNum);

    //==离职信息========
    List<TbPersonnel> queryQuitPersonnelList(Map<String, Object> params, Page page);
    List<TbPersonnel> queryQuitPersonnelList(Map<String, Object> params);

}
