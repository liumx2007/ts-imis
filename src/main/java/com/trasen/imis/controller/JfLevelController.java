package com.trasen.imis.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.model.TbJfPerson;
import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.TbTalentPool;
import com.trasen.imis.service.JfLevelService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by zhangxiahui on 17/9/5.
 */
@Controller
@RequestMapping(value="/jfLevel")
public class JfLevelController {

    private static final Logger logger = Logger.getLogger(JfLevelController.class);

    @Autowired
    JfLevelService jfLevelService;



    @RequestMapping(value="/queryJfPersonnel",method = RequestMethod.POST)
    @ResponseBody
    public Result queryJfPersonnel(@RequestBody Map<String,String> params) throws JsonProcessingException {
        //结果集
        Result result = new Result();
        result.setStatusCode(0);
        result.setSuccess(false);
        try {
            String companyId = MapUtils.getString(params, "companyId");
            String deptId = MapUtils.getString(params, "deptId");
            String name = MapUtils.getString(params, "name");

            Map<String,Object> param = new HashMap<>();
            String tagCode = null;
            if(!StringUtil.isEmpty(companyId)){
                tagCode = companyId;
            }
            if(!StringUtil.isEmpty(deptId)){
                tagCode = deptId;
            }
            param.put("tagCode",tagCode);
            param.put("name",name);

            List<TbJfPerson> list = jfLevelService.queryJfPersonnel(param);

            result.setObject(list);
            result.setSuccess(true);
            result.setStatusCode(1);
        } catch (IllegalArgumentException e) {
            logger.error("获取人员列表异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取人员列表异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;

    }


    @ResponseBody
    @RequestMapping(value = "/seachJfRecord", method = RequestMethod.POST)
    public Map<String,Object> seachJfRecord(@RequestBody Map<String, Object> params) {
        //结果集
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo", 1);
        result.put("pageSize", 0);
        result.put("totalCount", 0);
        result.put("totalPages", 1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String workNum = MapUtils.getString(params, "workNum");
            String startDate = MapUtils.getString(params, "startDate");
            String endDate = MapUtils.getString(params, "endDate");

            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo, pageSize);
            page.setSord("desc");
            page.setSidx("created");

            Map<String,Object> param = new HashMap<>();
            param.put("workNum",workNum);
            param.put("startDate",startDate);
            param.put("endDate",endDate);


            List<TbJfRecord> list = jfLevelService.seachJfRecord(param,page);

            result.put("list", list);
            result.put("pageNo", page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount", page.getTotalCount());
            result.put("totalPages", page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取积分记录列表异常" + e.getMessage(), e);
            result.put("code", 0);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("获取积分记录列表异常" + e.getMessage(), e);
            result.put("code", 0);
            result.put("msg", e.getMessage());
        }
        return result;
    }


    /**
     * 修改级别
     * */
    @ResponseBody
    @RequestMapping(value="/updateLevel", method = RequestMethod.POST)
    public Result updateLevel(@RequestBody TbJfPerson tbJfPerson)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            //数据更新
            if(tbJfPerson!=null){
                result.setSuccess(true);
                result.setMessage("修改成功");
                jfLevelService.saveJfPersonToScoreAndRank(tbJfPerson, AppCons.RANK);
                String rankName = jfLevelService.getRankName(tbJfPerson.getRank());
                if(rankName==null){
                    rankName = "初级未转";
                }
                result.setObject(rankName);
            }
        }catch (Exception e) {
            logger.error("级别修改异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("修改失败");
        }
        return  result;

    }


    /**
     * 添加修改积分记录
     * */
    @ResponseBody
    @RequestMapping(value="/addJfRecord", method = RequestMethod.POST)
    public Result addJfRecord(@RequestBody TbJfRecord tbJfRecord)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("失败");
        try {
            //数据更新
            if(tbJfRecord!=null){
                if(tbJfRecord.getWorkNum()!=null&&tbJfRecord.getWorkNums()==null){
                    jfLevelService.addJfRecord(tbJfRecord);
                    Integer score = jfLevelService.getScoreFromWorkNum(tbJfRecord.getWorkNum());
                    result.setObject(score);
                }
                if(tbJfRecord.getWorkNum()==null&&tbJfRecord.getWorkNums()!=null&&tbJfRecord.getWorkNums().size()>0){
                    for(String workNum : tbJfRecord.getWorkNums()){
                        TbJfRecord jfRecord = new TbJfRecord();
                        jfRecord.setWorkNum(workNum);
                        jfRecord.setType(AppCons.HR_ADD_SCORE);
                        jfRecord.setScore(tbJfRecord.getScore());
                        jfRecord.setRemark(tbJfRecord.getRemark());
                        jfLevelService.addJfRecord(jfRecord);
                    }
                }
                result.setSuccess(true);
                result.setMessage("成功");
            }
        }catch (Exception e) {
            logger.error("添加修改积分记录异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("失败");
        }
        return  result;

    }

    /**
     * 移动的展示隐藏积分记录
     * */
    @ResponseBody
    @RequestMapping(value="/isShowRecord", method = RequestMethod.POST)
    public Result isShowRecord(@RequestBody TbJfRecord tbJfRecord)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            //数据更新
            if(tbJfRecord!=null){
                boolean boo = jfLevelService.isShowRecord(tbJfRecord);
                result.setSuccess(boo);
            }
        }catch (Exception e) {
            logger.error("移动的展示隐藏积分记录异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("失败");
        }
        return  result;

    }
}
