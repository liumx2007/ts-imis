package com.trasen.imis.service;

import cn.trasen.commons.util.DateUtil;
import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.dao.TbPersonnelMapper;
import com.trasen.imis.dao.TbTagPersonnelMapper;
import com.trasen.imis.dao.TbTreeMapper;
import com.trasen.imis.dao.TuserMaperr;
import com.trasen.imis.model.*;
import com.trasen.imis.utils.DateUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhangxiahui on 17/7/11.
 */
@Component
public class PersonnelService {

    Logger logger = Logger.getLogger(PersonnelService.class);


    @Autowired
    TbPersonnelMapper tbPersonnelMapper;

    @Autowired
    TbTreeMapper tbTreeMapper;

    @Autowired
    TbTagPersonnelMapper tbTagPersonnelMapper;

    @Autowired
    ContractService contractService;

    @Autowired
    TuserMaperr tuserMaperr;

    public List<TbPersonnel> searchPersonnelList(Map<String, Object> params, Page page){
        List<TbPersonnel> list = new ArrayList<>();
        if(params!=null){
            list = tbPersonnelMapper.queryPersonnelList(params,page);
        }
        return list;
    }


    public Integer getPkid(String type){
        TbPkid tb = new TbPkid();
        tb.setType(type);
        tbPersonnelMapper.getPkid(tb);
        return tb.getPkid();
    }

    public int savePersonnel(TbPersonnel tbPersonnel) throws BadHanyuPinyinOutputFormatCombination {
        Map<String,Object> mappram=new HashMap<String,Object>();
        if(tbPersonnel!=null&&tbPersonnel.getWorkNum()!=null){
            if(tbPersonnel.getDepId()!=null){
                String tagCode = getTagCode(tbPersonnel.getDepId(),null);
                tbPersonnel.setTagCode(tagCode);
            }
            TbPersonnel oldPersonnel = tbPersonnelMapper.getPersonnelForWorkNum(tbPersonnel.getWorkNum());
            if(oldPersonnel==null){
                Integer pkid = getPkid("tb_personnel");
                if(pkid !=null){
                    tbPersonnel.setPerId(pkid.toString());
                    tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                    tbPersonnel.setCreated(new Date());
                    tbPersonnelMapper.insertPersonnel(tbPersonnel);

                    mappram.put("pkid",pkid);
                    mappram.put("per_deptid",tbPersonnel.getDepId());
                    mappram.put("deptname",tbPersonnel.getName());
                    mappram.put("level",4);
                    mappram.put("type","person");
                    tbTreeMapper.insertTree(mappram);

                    TbContract tbContract=new TbContract();
                    tbContract.setEntryDate(tbPersonnel.getEntryDate());
                    tbContract.setName(tbPersonnel.getName());
                    tbContract.setWorkNum(tbPersonnel.getWorkNum());
                    tbContract.setOperator(VisitInfoHolder.getUserId());
                    tbContract.setStatus(0);
                    contractService.insertContract(tbContract);

                    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
                    Tuser tuser=new Tuser();
                    String name="";
                    if(tbPersonnel.getName().matches("[\\u4e00-\\u9fa5]+")) {
                        char[] nameArray = tbPersonnel.getName().toCharArray();
                        for (int i = 0; i < nameArray.length; i++) {
                            if (i == 0) {
                                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(nameArray[0], format);
                                name = pinyin[0].substring(0, 1).toUpperCase() + pinyin[0].substring(1, pinyin[0].length() - 1);
                            } else {
                                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(nameArray[i], format);
                                name = name + pinyin[0].substring(0, 1);
                            }
                        }
                        name=name+tbPersonnel.getWorkNum();
                    }else{
                        name= tbPersonnel.getName()+tbPersonnel.getWorkNum();
                    }
                    tuser.setName(name);
                    tuser.setDisplayName(tbPersonnel.getName());
                    tuser.setPassword("123456");
                    tuser.setCreated(new Date());
                    tuser.setStatus(1);
                    tuser.setPerId(String.valueOf(pkid));
                    tuserMaperr.insertGetId(tuser);
                }
            }else{
                //判断部门ID是否有修改
                if(!oldPersonnel.getDepId().equals(tbPersonnel.getDepId())){
                    //如果部门ID有修改写入调岗记录
                    insertDeptLog(oldPersonnel,tbPersonnel);
                }
                tbPersonnel.setPerId(oldPersonnel.getPerId());
                tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                tbPersonnel.setUpdated(new Date());
                tbPersonnelMapper.updatePersonnel(tbPersonnel);
                mappram.put("pkid",tbPersonnel.getPerId());
                mappram.put("per_deptid",tbPersonnel.getDepId());
                mappram.put("deptname",tbPersonnel.getName());
                mappram.put("level",4);
                mappram.put("type","person");
                tbTreeMapper.updateTree(mappram);
                Tuser tuser1=tuserMaperr.selectByperId(tbPersonnel.getPerId());
                if(tuser1!=null){
                    String name = "";
                    if (tbPersonnel.getName().matches("[\\u4e00-\\u9fa5]+")) {
                        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
                        char[] nameArray = tbPersonnel.getName().toCharArray();
                        for (int i = 0; i < nameArray.length; i++){
                            if (i == 0) {
                                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(nameArray[0], format);
                                name = pinyin[0].substring(0, 1).toUpperCase() + pinyin[0].substring(1, pinyin[0].length() - 1);
                            } else {
                                String[] pinyin=PinyinHelper.toHanyuPinyinStringArray(nameArray[i], format);
                                name = name+pinyin[0].substring(0, 1);
                            }
                        }
                        name = name + tbPersonnel.getWorkNum();
                    }else{
                        name= tbPersonnel.getName()+tbPersonnel.getWorkNum();
                    }
                    tuser1.setName(name);
                    tuser1.setDisplayName(tbPersonnel.getName());
                    tuser1.setUpdated(new Date());
                    tuserMaperr.updatePersonByTuser(tuser1);
                }
            }
            //更新微信用户
            updateWeixinUserTag(tbPersonnel);
            return 1;
        }
        return 0;
    }

    public void updateWeixinUserTag(TbPersonnel tbPersonnel){
        //更新微信用户表的标签ID
        //优先更新用户标签,如果用户没有标签则更新成部门路径
        List<TbTagPersonnel> tagList = tbTagPersonnelMapper.getTaTagPersonnelList(tbPersonnel.getWorkNum());
        AttenceVo attenceVo = new AttenceVo();
        attenceVo.setWorkNum(tbPersonnel.getWorkNum());
        attenceVo.setName(tbPersonnel.getName());
        attenceVo.setPosition(tbPersonnel.getPosition());
        String tagId = tbPersonnel.getTagCode();
        String tagName = tbPersonnel.getDepName();
        if(tagList!=null&&tagList.size()>0){
            for(TbTagPersonnel tag:tagList){
                if(tag.getTagId()!=null){
                    String key = tag.getTagId().replace("|","");
                    tagId = tagId +key+"|";
                }
            }
        }
        attenceVo.setTagId(tagId);
        attenceVo.setTagName(tagName);
        AttenceVo weixinUser = tbPersonnelMapper.getWeixinUser(tbPersonnel.getWorkNum());
        if(weixinUser==null){
            tbPersonnelMapper.insertWeixinUser(attenceVo);
        }else{
            tbPersonnelMapper.updateWeixinUser(attenceVo);
        }
    }

    public int savePersonnelBasic(TbPersonnel tbPersonnel){
        TbPersonnel oldPersonnel = tbPersonnelMapper.getPersonnelForWorkNum(tbPersonnel.getWorkNum());
        if(tbPersonnel!=null&&oldPersonnel!=null){
            TbPersonnel perBasic = tbPersonnelMapper.getPersonnelBasic(oldPersonnel.getPerId());
            if(perBasic==null){
                //新增
                tbPersonnel.setPerId(oldPersonnel.getPerId());
                tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                tbPersonnel.setCreated(new Date());
                tbPersonnelMapper.insertPersonnelBasic(tbPersonnel);
            }else{
                //更新
                tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                tbPersonnel.setUpdated(new Date());
                tbPersonnelMapper.updatePersonnelBasic(tbPersonnel);
            }
            return 1;
        }
        return 0;
    }

    public int savePersonnelFile(TbPersonnel tbPersonnel){
        TbPersonnel oldPersonnel = tbPersonnelMapper.getPersonnelForWorkNum(tbPersonnel.getWorkNum());
        if(tbPersonnel!=null&&oldPersonnel!=null){
            TbPersonnel perFile = tbPersonnelMapper.getPersonnelFile(oldPersonnel.getPerId());
            if(perFile==null){
                //新增
                tbPersonnel.setPerId(oldPersonnel.getPerId());
                tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                tbPersonnel.setCreated(new Date());
                tbPersonnelMapper.insertPersonnelFile(tbPersonnel);
            }else{
                //更新
                tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                tbPersonnel.setUpdated(new Date());
                tbPersonnelMapper.updatePersonnelFile(tbPersonnel);
            }
            return 1;
        }
        return 0;
    }

    public void deletePersonnel(String perId,String workNum){

        tbPersonnelMapper.deletePersonnel(perId);
        tbTreeMapper.deleteTreePersonnel(perId);
        contractService.deleteContractforWorkNum(workNum);
        tbPersonnelMapper.deleteWeixinUser(workNum);
    }

    public String getTagCode(String depId,String tagCode){
        if(depId!=null){
            if(tagCode==null){
                tagCode = "|"+depId+"|";
            }
            String parentDepId = tbPersonnelMapper.getParentDepId(depId);
            if(parentDepId!=null){
                tagCode = "|"+parentDepId+tagCode;
                tagCode = getTagCode(parentDepId,tagCode);
            }
            return tagCode;
        }
        return null;
    }

    public int findWorkNumRepeat(String workNum){
        return tbPersonnelMapper.findWorkNumRepeat(workNum);
    }


    public List<TbPersonnel> searchExcelPersonnelList(Map<String, Object> params){
        List<TbPersonnel> list = new ArrayList<>();
        if(params!=null){
            list = tbPersonnelMapper.queryPersonnelList(params);
        }
        return list;
    }

    public List<TbPersonnel> searchAddressPersonnelList(Map<String, Object> params){
        List<TbPersonnel> list = new ArrayList<>();
        list = tbPersonnelMapper.queryAddresssPersonnelList(params);
        return list;
    }

    public void insertDeptLog(TbPersonnel oldPersonnel,TbPersonnel newPersonnel){
        String today = DateUtil.getDateTime("yyyy-MM-dd");
        TbDeptLog tbDeptLog = new TbDeptLog();
        tbDeptLog.setOldDepId(oldPersonnel.getDepId());
        tbDeptLog.setOldDept(oldPersonnel.getDepName());
        tbDeptLog.setOldPosition(oldPersonnel.getPosition());
        tbDeptLog.setNewDepId(newPersonnel.getDepId());
        tbDeptLog.setNewDept(newPersonnel.getDepName());
        tbDeptLog.setNewPosition(newPersonnel.getPosition());
        tbDeptLog.setChangeDate(today);
        tbDeptLog.setWorkNum(oldPersonnel.getWorkNum());
        tbDeptLog.setPerId(oldPersonnel.getPerId());
        tbDeptLog.setExeDate(today);
        tbDeptLog.setCreated(new Date());
        tbDeptLog.setOperator(VisitInfoHolder.getUserId());
        tbPersonnelMapper.insertDeptLog(tbDeptLog);
    }

    public List<TbDeptLog> queryDeptLog(TbDeptLog tbDeptLog,Page page){
        List<TbDeptLog> list = new ArrayList<>();
        if(tbDeptLog!=null){
            list = tbPersonnelMapper.queryDeptLog(tbDeptLog,page);
        }
        return list;
    }

    public TbPersonnel findWorkNumForPersonnel(String workNum){
        return tbPersonnelMapper.getPersonnelForWorkNum(workNum);
    }

    //===离职信息========
    public List<TbPersonnel> searchQuitPersonnelList(Map<String, Object> params, Page page){
        List<TbPersonnel> list = new ArrayList<>();
        if(params!=null){
            list = tbPersonnelMapper.queryQuitPersonnelList(params,page);
        }
        return list;
    }

    public List<TbPersonnel> searchExcelQuitPersonnelList(Map<String, Object> params){
        List<TbPersonnel> list = new ArrayList<>();
        list = tbPersonnelMapper.queryQuitPersonnelList(params);
        return list;
    }

    public List<Map<String,Object>> getPersonnelTags(){
        return tbTagPersonnelMapper.getPersonnelTags(VisitInfoHolder.getUid());
    }

    public void batchUpdateDept(List<String> list,String depId,String depName){
        Map<String,Object> mappram=new HashMap<String,Object>();
        String tagCode = getTagCode(depId,null);
        for (String workNum : list){
            TbPersonnel oldPersonnel = tbPersonnelMapper.getPersonnelForWorkNum(workNum);
            TbPersonnel tbPersonnel = new TbPersonnel();
            tbPersonnel.setPerId(oldPersonnel.getPerId());
            tbPersonnel.setWorkNum(oldPersonnel.getWorkNum());
            tbPersonnel.setName(oldPersonnel.getName());
            tbPersonnel.setSex(oldPersonnel.getSex());
            tbPersonnel.setPhone(oldPersonnel.getPhone());
            tbPersonnel.setOperator(VisitInfoHolder.getUserId());
            tbPersonnel.setPosition(oldPersonnel.getPosition());
            tbPersonnel.setEntryDate(oldPersonnel.getEntryDate());
            tbPersonnel.setPicture(oldPersonnel.getPicture());

            tbPersonnel.setDepId(depId);
            tbPersonnel.setDepName(depName);
            tbPersonnel.setPosition(oldPersonnel.getPosition());
            tbPersonnel.setTagCode(tagCode);

            //判断部门ID是否有修改
            if(!oldPersonnel.getDepId().equals(tbPersonnel.getDepId())){
                //如果部门ID有修改写入调岗记录
                insertDeptLog(oldPersonnel,tbPersonnel);
                tbPersonnel.setPerId(oldPersonnel.getPerId());
                tbPersonnel.setOperator(VisitInfoHolder.getUserId());
                tbPersonnel.setUpdated(new Date());
                tbPersonnelMapper.updatePersonnel(tbPersonnel);
                mappram.put("pkid",tbPersonnel.getPerId());
                mappram.put("per_deptid",tbPersonnel.getDepId());
                mappram.put("deptname",tbPersonnel.getName());
                mappram.put("level",4);
                mappram.put("type","person");
                tbTreeMapper.updateTree(mappram);
                updateWeixinUserTag(tbPersonnel);
            }

        }


    }
}
