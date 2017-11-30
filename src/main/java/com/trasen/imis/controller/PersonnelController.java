package com.trasen.imis.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.model.TbDeptLog;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.service.PersonnelService;
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
 * Created by zhangxiahui on 17/7/11.
 */
@Controller
@RequestMapping("/personnel")
public class PersonnelController {

    private static final Logger logger = Logger.getLogger(PersonnelController.class);

    @Autowired
    PersonnelService personnelService;

    @ResponseBody
    @RequestMapping(value = "/searchPersonnel", method = RequestMethod.POST)
    public Map<String,Object> searchPersonnel(@RequestBody Map<String, Object> params){
        //结果集
        Map<String,Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo",1);
        result.put("pageSize", 0);
        result.put("totalCount",0);
        result.put("totalPages",1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            String depName = MapUtils.getString(params, "depName");
            String name = MapUtils.getString(params, "name");
            String position = MapUtils.getString(params, "position");

            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSord("asc");
            page.setSidx("p.work_num");


            /*TbPersonnel tbPersonnel = new TbPersonnel();
            if(!StringUtil.isEmpty(name)){
                tbPersonnel.setName(name);
            }
            if(!StringUtil.isEmpty(depName)){
                tbPersonnel.setDepName(depName);
            }
            if(!StringUtil.isEmpty(position)){
                tbPersonnel.setPosition(position);
            }
            if(!StringUtil.isEmpty(position)){
                tbPersonnel.setPosition(position);
            }*/
            List<TbPersonnel> list = personnelService.searchPersonnelList(params,page);

            result.put("list",list);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取人员档案列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取人员档案列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }


    /**
    * 人员档案保存
    * */
    @ResponseBody
    @RequestMapping(value="/personnelSave", method = RequestMethod.POST)
    public Result personnelSave(@RequestBody TbPersonnel tbPersonnel)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            //数据更新
            if(tbPersonnel!=null){
                if(StringUtil.isEmpty(tbPersonnel.getPerId())){
                    result.setSuccess(true);
                    result.setMessage("新增成功");
                }else{
                    result.setSuccess(true);
                    result.setMessage("修改成功");
                }
                personnelService.savePersonnel(tbPersonnel);
                result.setObject(tbPersonnel);

            }

        }catch (Exception e) {
            logger.error("个人基本信息异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }

    /**
     * 人员档案保存
     * */
    @ResponseBody
    @RequestMapping(value="/personnelBasicSave", method = RequestMethod.POST)
    public Result personnelBasicSave(@RequestBody TbPersonnel tbPersonnel)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            personnelService.savePersonnelBasic(tbPersonnel);
            result.setSuccess(true);
            result.setMessage("个人信息更新成功");

        }catch (Exception e) {
            logger.error("个人信息更新异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;
    }

    /**
     * 人员档案保存
     * */
    @ResponseBody
    @RequestMapping(value="/personnelFileSave", method = RequestMethod.POST)
    public Result personnelFileSave(@RequestBody TbPersonnel tbPersonnel)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            personnelService.savePersonnelFile(tbPersonnel);
            result.setSuccess(true);
            result.setMessage("个人档案更新成功");

        }catch (Exception e) {
            logger.error("个人档案更新异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }


    /**
    * 删除人员档案
    * */
    @ResponseBody
    @RequestMapping(value="/deletePerson", method = RequestMethod.POST)
    public Result deletePerson(@RequestBody Map<String, String> params){
        Result result=new Result();
        personnelService.deletePersonnel(params.get("perId"),params.get("workNum"));
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }


    @ResponseBody
    @RequestMapping(value="/findWorkNumRepeat",method=RequestMethod.POST)
    public Result findWorkNumRepeat(@RequestBody Map<String,String> mapDeptId) throws JsonProcessingException {
        Result result=new Result();
        if(mapDeptId.get("workNum")==null||mapDeptId.get("workNum")=="unfinded"){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }

        int workNumcount=personnelService.findWorkNumRepeat(mapDeptId.get("workNum"));
        if(workNumcount==0){
            result.setSuccess(true);
            result.setMessage("该工号可以使用");
        }
        if(workNumcount>0){
            result.setSuccess(false);
            result.setMessage("该工号已被使用");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryDeptLog", method = RequestMethod.POST)
    public Map<String,Object> queryDeptLog(@RequestBody Map<String, Object> params){
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

            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSord("desc");
            page.setSidx("created");


            TbDeptLog tbDeptLog = new TbDeptLog();
            if(!StringUtil.isEmpty(perId)){
                tbDeptLog.setPerId(perId);
            }
            List<TbDeptLog> list = personnelService.queryDeptLog(tbDeptLog,page);

            result.put("list",list);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取调岗日志异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取调岗日志列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }


    //===========离职信息===========
    @ResponseBody
    @RequestMapping(value = "/searchQuitPersonnel", method = RequestMethod.POST)
    public Map<String,Object> searchQuitPersonnel(@RequestBody Map<String, Object> params){
        //结果集
        Map<String,Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("pageNo",1);
        result.put("pageSize", 0);
        result.put("totalCount",0);
        result.put("totalPages",1);
        try {
            checkArgument(MapUtils.isNotEmpty(params), "参数对象params不可为空!");
            Integer pageNo = MapUtils.getInteger(params, "pageNo");
            Integer pageSize = MapUtils.getInteger(params, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSord("asc");
            page.setSidx("p.work_num");
            List<TbPersonnel> list = personnelService.searchQuitPersonnelList(params,page);

            result.put("list",list);
            result.put("pageNo",page.getPageNo());
            result.put("pageSize", page.getPageSize());
            result.put("totalCount",page.getTotalCount());
            result.put("totalPages",page.getTotalPages());
        } catch (IllegalArgumentException e) {
            logger.error("获取离职人员信息列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        } catch (Exception e) {
            logger.error("获取离职人员信息列表异常" + e.getMessage(), e);
            result.put("code",0);
            result.put("msg",e.getMessage());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public Result getOperList() {
        logger.info("===查询用户["+ VisitInfoHolder.getUserId()+"]的权限标签=======");
        Result result = new Result();
        result.setStatusCode(1);
        result.setSuccess(true);
        try {
            List<Map<String,Object>> list = personnelService.getPersonnelTags();
            for(Map<String,Object> map : list){
                map.put("checked",false);
            }
            result.setObject(list);
        } catch (Exception e) {
            logger.error("获取用户的权限标签" + e.getMessage(), e);
            result.setStatusCode(0);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
