package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbDept;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.TbTree;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/5.
 */
public interface TbTreeMapper {

    List<TbTree> getTreeList(String pkid);

    List<TbTree> getDeptTreeList(String pkid);

    TbTree getParentTree();

    List<TbDept> getOrganizationDept(Map<String,String> parm,Page page);

    List<TbPersonnel> getOrganizationDeptPerson(Map<String,String> parm, Page page);

    int findDeptidRepeat(String deptid);

    void insertTree(Map<String,Object> mapTree);

    void insertDept(Map<String,Object> mapDept);

    int updateTree(Map<String,Object> mapTree);

    int updatePersonnelTree(Map<String,Object> mapTree);

    int updatePersonnel(Map<String,Object> mapperson);

    String selectTagCode(String parent);

    void deleteTreePersonnel(String pkid);

    TbDept getSuperiorDepid(String pkid);

    int getCountForPkid(String pkid);

    int delteForTree(String pkid);

    int delteForDept(String pkid);

    TbDept getDept(String depId);

    int updateDept(TbDept tbDept);

    int updateDeptTree(TbTree tbTree);

    List<TbPersonnel> getOrganizationDeptPerson(Map<String,String> parm);

    int updatePersonnelTagCode(TbPersonnel tbPersonnel);

    int updateSubDeptName(TbDept tbDept);


}
