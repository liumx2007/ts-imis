package com.trasen.imis.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbTalentPool;
import com.trasen.imis.model.TbWorkHistory;
import com.trasen.imis.service.TalentPoolService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by zhangxiahui on 17/7/18.
 */
@Controller
@RequestMapping("/talent")
public class TalentPoolController {

    private static final Logger logger = Logger.getLogger(TalentPoolController.class);

    @Autowired
    TalentPoolService talentPoolService;

    @ResponseBody
    @RequestMapping(value = "/searchTalent", method = RequestMethod.POST)
    public Map<String,Object> searchTalent(@RequestBody Map<String, Object> params){
        //结果集
        Map<String,Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo",1);
        result.put("pageSize", 0);
        result.put("totalCount",0);
        result.put("totalPages",1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String name = MapUtils.getString(params, "name");
            String willJob = MapUtils.getString(params, "willJob");
            String beGood = MapUtils.getString(params, "beGood");
            Integer isCome = MapUtils.getInteger(params,"isCome");
            String result_selct=MapUtils.getString(params,"result");

            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSord("desc");
            page.setSidx("created");


            TbTalentPool tbTalentPool = new TbTalentPool();
            if(!StringUtil.isEmpty(name)){
                tbTalentPool.setName(name);
            }
            if(!StringUtil.isEmpty(willJob)){
                tbTalentPool.setWillJob(willJob);
            }
            if(!StringUtil.isEmpty(beGood)){
                tbTalentPool.setBeGood(beGood);
            }
            if(!StringUtil.isEmpty(result_selct)){
                tbTalentPool.setResult(result_selct);
            }
            if(isCome!=null){
                tbTalentPool.setIsCome(isCome);
            }

            List<TbTalentPool> list = talentPoolService.searchTalentPoolList(tbTalentPool,page);
            result.put("list",list);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取人才库列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取人才库列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }

    /**
     * 人才库保存
     * */
    @ResponseBody
    @RequestMapping(value="/talentSave", method = RequestMethod.POST)
    public Result talentSave(@RequestBody TbTalentPool tbTalentPool)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            //数据更新
            if(tbTalentPool!=null){
                if(tbTalentPool.getPkid()==null){
                    result.setSuccess(true);
                    result.setMessage("新增成功");
                }else{
                    result.setSuccess(true);
                    result.setMessage("修改成功");
                }
                talentPoolService.saveTalentPool(tbTalentPool);
                result.setObject(tbTalentPool);
            }

        }catch (Exception e) {
            logger.error("人才库保存异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }

    /**
     * 删除人才库
     * */
    @ResponseBody
    @RequestMapping(value="/deleteTalent", method = RequestMethod.POST)
    public Result deletePerson(@RequestParam Integer pkid){
        Result result=new Result();
        talentPoolService.deleteTalentPool(pkid);
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }



    @ResponseBody
    @RequestMapping(value = "/queryWorkHistory", method = RequestMethod.POST)
    public Map<String,Object> queryWorkHistory(@RequestBody Map<String, Object> params){
        //结果集
        Map<String,Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo",1);
        result.put("pageSize", 0);
        result.put("totalCount",0);
        result.put("totalPages",1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String perId = MapUtils.getString(params, "perId");
            Integer type = MapUtils.getInteger(params, "type");

            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSord("desc");
            page.setSidx("created");


            TbWorkHistory tbWorkHistory = new TbWorkHistory();
            if(!StringUtil.isEmpty(perId)){
                tbWorkHistory.setPerId(perId);
            }
            if(type!=null){
                tbWorkHistory.setType(type);
            }

            List<TbWorkHistory> list = talentPoolService.queryWorkHistory(tbWorkHistory,page);
            result.put("list",list);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取工作经历列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取工作经历列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }

    /**
     * 工作经历保存
     * */
    @ResponseBody
    @RequestMapping(value="/workHistorySave", method = RequestMethod.POST)
    public Result workHistorySave(@RequestBody TbWorkHistory tbWorkHistory)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            //数据更新
            if(tbWorkHistory!=null){
                tbWorkHistory.setType(2);
                if(tbWorkHistory.getPkid()==null){
                    result.setSuccess(true);
                    result.setMessage("保存成功");
                }else{
                    result.setSuccess(true);
                    result.setMessage("修改成功");
                }
                talentPoolService.saveWorkHistory(tbWorkHistory);
                result.setObject(tbWorkHistory);
            }

        }catch (Exception e) {
            logger.error("工作经历保存异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }

    /**
     * 删除工作经历
     * */
    @ResponseBody
    @RequestMapping(value="/deleteWorkHistory", method = RequestMethod.POST)
    public Result deleteWorkHistory(@RequestParam Integer pkid){
        Result result=new Result();
        talentPoolService.deleteWorkHistory(pkid);
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }
}
