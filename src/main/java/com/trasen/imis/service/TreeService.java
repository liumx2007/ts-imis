package com.trasen.imis.service;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.dao.TbTreeMapper;
import com.trasen.imis.model.TbDept;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.TbTree;
import com.trasen.imis.model.TreeVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/5.
 */
@Component
public class TreeService {

    Logger logger = Logger.getLogger(TreeService.class);

    @Autowired
    TbTreeMapper tbTreeMapper;

    @Autowired
    PersonnelService personnelService;

    public TreeVo getTree(TbTree tbTree) {
        TreeVo vo= new TreeVo();
        if (tbTree!=null) {
            vo.setLabel(tbTree.getName());
            vo.setData(tbTree);
            List<TbTree> treeList = tbTreeMapper.getTreeList(tbTree.getPkid());
            List<TreeVo> children = new ArrayList<>();
            if(treeList!=null){
                if(treeList.size()==0&&"dept".equals(tbTree.getType())){
                    TreeVo notPer = new TreeVo();
                    notPer.setLabel("暂无下级");
                    children.add(notPer);
                }else{
                    for (TbTree tree : treeList) {
                        TreeVo childrenVo = getTree(tree);
                        children.add(childrenVo);
                    }
                }
            }
            vo.setChildren(children);
        }
        return vo;
    }

    public TreeVo getDeptTree(TbTree tbTree) {
        TreeVo vo = new TreeVo();
        if (tbTree!=null) {
            vo.setLabel(tbTree.getName());
            vo.setData(tbTree);
            List<TbTree> treeList = tbTreeMapper.getDeptTreeList(tbTree.getPkid());
            List<TreeVo> children = new ArrayList<>();
            for (TbTree tree : treeList) {
                TreeVo childrenVo = getDeptTree(tree);
                children.add(childrenVo);
            }
            vo.setChildren(children);
        }
        return vo;
    }

    public TbTree getParentTree(){
        return tbTreeMapper.getParentTree();
    }

    public List<TbDept> getOrganizationDept(Map<String,String> parm,Page page){
        return tbTreeMapper.getOrganizationDept(parm,page);
    }

    public List<TbPersonnel>  getOrganizationDeptPerson(Map<String,String> parm, Page page){
        return tbTreeMapper.getOrganizationDeptPerson(parm,page);
    }

    public int findDeptidRepeat(String deptid){
        return tbTreeMapper.findDeptidRepeat(deptid);
    }

    public void insertTreeDept(Map<String,Object> mapTree){
        tbTreeMapper.insertTree(mapTree);
        tbTreeMapper.insertDept(mapTree);

    }

    public void updateTreePerson(Map<String,Object> param){

        tbTreeMapper.updatePersonnelTree(param);
        tbTreeMapper.updatePersonnel(param);
    }


    public TbDept getSuperiorDepid(String pkid){
        return tbTreeMapper.getSuperiorDepid(pkid);
    }

    public int getCountForPkid(String pkid){
        return tbTreeMapper.getCountForPkid(pkid);
    }

    public int deleteTreeAndDept(String pkid){
        tbTreeMapper.delteForDept(pkid);
        return tbTreeMapper.delteForTree(pkid);
    }

    /**
     * 修改部门关联修改下级部门和人员
     *
     * */
    public boolean updateDept(String depId,String deptName,String newParentDepId,String remark){
        boolean boo = false;
        //查询新上级部门
        TbDept newParent = tbTreeMapper.getDept(newParentDepId);
        if(newParent!=null){
            //1、修改部门
            TbDept updateDept = new TbDept();
            updateDept.setDepId(depId);
            updateDept.setDepName(deptName);
            updateDept.setParentDepId(newParent.getDepId());
            updateDept.setParentDepName(newParent.getDepName());
            updateDept.setDepLevel(newParent.getDepLevel()+1);
            updateDept.setRemark(remark);
            updateDept.setOperator(VisitInfoHolder.getUserId());
            tbTreeMapper.updateDept(updateDept);
            // 下级部门名称
            tbTreeMapper.updateSubDeptName(updateDept);
            //2、修改树
            TbTree tbTree = new TbTree();
            tbTree.setPkid(depId);
            tbTree.setName(deptName);
            tbTree.setParent(newParent.getDepId());
            tbTree.setLevel(newParent.getDepLevel()+1);
            tbTreeMapper.updateDeptTree(tbTree);
            //3、修改人的关联关系中的tagCode
            String newtagCode = personnelService.getTagCode(newParentDepId,null);
            Map<String,String> parm = new HashMap();
            parm.put("id",depId);
            List<TbPersonnel> list = tbTreeMapper.getOrganizationDeptPerson(parm);
            if(list!=null&&list.size()>0){
                for(TbPersonnel tbPersonnel : list){
                    String tagCode = tbPersonnel.getTagCode();
                    if(tagCode!=null){
                        String [] tagCodes = tagCode.split(depId);
                        if(tagCodes.length==2){
                            String tagcodeT = newtagCode + depId + tagCodes[1];
                            tbPersonnel.setTagCode(tagcodeT);
                            // 如果depId为该人员的直接上级则修改部门名称,否则不修改
                            if("|".equals(tagCodes[1])){
                                tbPersonnel.setDepName(deptName);
                            }
                            tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                            //更新人员
                            tbTreeMapper.updatePersonnelTagCode(tbPersonnel);
                            //更新微信用户
                            personnelService.updateWeixinUserTag(tbPersonnel);
                        }
                    }
                }
            }
            boo = true;
        }
        return boo;
    }

}
