package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasen.imis.model.*;
import com.trasen.imis.service.TbTagPersonnelService;
import com.trasen.imis.service.TreeService;
import com.trasen.imis.service.TuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/6
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private TreeService treeService;

    @Autowired
    private TbTagPersonnelService tbTagPersonnelService;

    @Autowired
    private TuserService tuserService;

    @RequestMapping(value="/getOrganization",method = RequestMethod.POST)
    @ResponseBody
    public Result getOrganization(){
        Result result=new Result();
        TbTree tree =treeService.getParentTree();
        TreeVo treeVo = treeService.getTree(tree);
        List<TreeVo> list = new ArrayList<>();
        list.add(treeVo);
        result.setObject(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value="/getDeptOrganization",method = RequestMethod.POST)
    @ResponseBody
    public Result getDeptOrganization(){
        Result result=new Result();
        TbTree tree = treeService.getParentTree();
        TreeVo treeVo = treeService.getDeptTree(tree);
        List<TreeVo> list = new ArrayList<>();
        list.add(treeVo);
        result.setObject(list);
        result.setSuccess(true);
        return result;
    }


    @RequestMapping(value="/getOrganizationDept",method = RequestMethod.POST)
    @ResponseBody
    public String getOrganizationDept(@RequestBody Map<String,String> parm) throws JsonProcessingException {
        if(parm.get("id").isEmpty()){
            String json = "{\"message\": " + "传入参数错误" + ",\"code\": "+"-1"+"}";
            return json;
        }
        Integer pageNo=Integer.valueOf(parm.get("pageNo").toString());
        Integer pageSize=Integer.valueOf(parm.get("pageSize").toString());
        Page page = new Page(pageNo,pageSize);
        page.setSidx("dep_id");
        page.setSord("desc");
        List<TbDept> tbDeptList=treeService.getOrganizationDept(parm,page);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(tbDeptList);
        String json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+",\"code\": "+"1"+"}";
        return json;
    }

    @RequestMapping(value="/getOrganizationDeptPerson",method = RequestMethod.POST)
    @ResponseBody
    public String getOrganizationDeptPerson(@RequestBody Map<String,String> parm) throws JsonProcessingException {
        if(parm.get("id").isEmpty()){
            String json = "{\"message\": " + "传入参数错误" + ",\"code\": "+"-1"+"}";
            return json;
        }
        Integer pageNo=Integer.valueOf(parm.get("pageNo").toString());
        Integer pageSize=Integer.valueOf(parm.get("pageSize").toString());
        Page page = new Page(pageNo,pageSize);
        page.setSidx("dep_id");
        page.setSord("desc");
        List<TbPersonnel> tbDeptPersonList=treeService.getOrganizationDeptPerson(parm,page);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(tbDeptPersonList);
        String json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+",\"code\": "+"1"+"}";
        return json;
    }

    @RequestMapping(value="/findDeptidRepeat",method = RequestMethod.POST)
    @ResponseBody
    public Result findDeptidRepeat(@RequestBody Map<String,String> mapDeptId) throws JsonProcessingException {
        Result result=new Result();
        if(mapDeptId.get("deptid")==null||mapDeptId.get("deptid")=="unfinded"){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }

        int deptcount=treeService.findDeptidRepeat(mapDeptId.get("deptid"));
        if(deptcount==0){
            result.setMessage("该部门ID可以使用");
            result.setSuccess(true);
        }
        if(deptcount>0){
            result.setMessage("该部门ID已被使用");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/saveDept",method = RequestMethod.POST)
    @ResponseBody
    public Result saveDept(@RequestBody Map<String,Object> param) throws JsonProcessingException {
        Result result=new Result();
        if(param.get("pkid")==null||param.get("deptname")==null||param.get("per_deptid")==null||param.get("per_deptname")==null){
            result.setSuccess(false);
            result.setMessage("参数错误");
            return result;
        }
        int deptcount=treeService.findDeptidRepeat(param.get("pkid").toString());
        if(deptcount>0){
            String depId = param.get("pkid").toString();
            String deptName = param.get("deptname").toString();
            String newParentDepId = param.get("per_deptid").toString();
            String remark = null;
            if(param.get("remark")!=null){
                remark = param.get("remark").toString();
            }
            treeService.updateDept(depId,deptName,newParentDepId,remark);
            result.setSuccess(true);
            result.setMessage("更新成功");
        }else {
            param.put("created", new Date());
            treeService.insertTreeDept(param);
            result.setSuccess(true);
            result.setMessage("保存成功");
        }
        return result;
    }

    @RequestMapping(value="/updateDept",method = RequestMethod.POST)
    @ResponseBody
    public Result updateDept(@RequestBody Map<String,Object> param) throws JsonProcessingException {
        Result result=new Result();
        if(param.get("pkid")==null||param.get("deptname")==null||param.get("per_deptid")==null||param.get("per_deptname")==null){
            result.setSuccess(false);
            result.setMessage("参数错误");
            return result;
        }
        String depId = param.get("pkid").toString();
        String deptName = param.get("deptname").toString();
        String newParentDepId = param.get("per_deptid").toString();
        String remark = null;
        if(param.get("remark")!=null){
            remark = param.get("remark").toString();
        }
        treeService.updateDept(depId,deptName,newParentDepId,remark);
        result.setMessage("部门更新成功");
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value="/deleteDept",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteDept(@RequestBody Map<String,Object> param) throws JsonProcessingException {
        Result result=new Result();
        if(param.get("pkid")==null||param.get("deptname")==null){
            result.setSuccess(false);
            result.setMessage("参数错误");
            return result;
        }
        int countTree=treeService.getCountForPkid(param.get("pkid").toString());
        if(countTree>0){
            result.setMessage("该部门还有下属机构或人员，禁止删除");
            result.setSuccess(false);
        }else{
            treeService.deleteTreeAndDept(param.get("pkid").toString());
            result.setSuccess(true);
            result.setMessage("部门删除成功");
        }

        return result;
    }





    @RequestMapping(value="/getTaTagPersonnelList",method = RequestMethod.GET)
    @ResponseBody
    public Result getTaTagPersonnelList(@QueryParam("workNum") String workNum){
        Result result=new Result();
        List<TbTagPersonnel> tbTagPersonnelList= tbTagPersonnelService.getTaTagPersonnelList(workNum);
        result.setSuccess(true);
        result.setObject(tbTagPersonnelList);
        return result;
    }

    @RequestMapping(value="/getSuperiorDepid",method = RequestMethod.GET)
    @ResponseBody
    public Result getSuperiorDepid(@QueryParam("pkid") String pkid){
        Result result=new Result();
        if(pkid==null||pkid.equals("undefined")){
            result.setSuccess(false);
            result.setMessage("参数错误");
            //return result;
        }else{
            TbDept dept = treeService.getSuperiorDepid(pkid);
            if(dept==null){
                result.setSuccess(false);
                result.setMessage("数据查询失败");
            }else{
                result.setSuccess(true);
                result.setObject(dept);
            }
        }
        return result;
    }

    @RequestMapping(value="/saveTuser",method = RequestMethod.POST)
    @ResponseBody
    public Result saveTuser(@RequestBody Map<String,String> param){
        Result result=new Result();
        if(param.isEmpty()||param.get("displayName")==null||param.get("name").equals("")||param.get("password").equals("")||param.get("status")==null||param.get("pkid").equals("")){
            result.setMessage("人员账户信息参数错误");
            result.setSuccess(false);
            return result;
        }
        Tuser tuser=new Tuser();
        tuser.setDisplayName(param.get("displayName"));
        tuser.setName(param.get("name"));
        tuser.setPassword(param.get("password"));
        tuser.setPerId(param.get("perId"));
        tuser.setUpdated(new Date());
        tuser.setStatus(Integer.valueOf(param.get("status")));
        tuser.setPkid(Integer.valueOf(param.get("pkid")));
        int updateCount=tuserService.updateTuser(tuser);
        if(updateCount>0){
            result.setMessage("数据更新成功");
            result.setSuccess(true);
        }else{
            result.setMessage("数据更新失败");
            result.setSuccess(false);
        }
        return result;
    }
    @RequestMapping(value="/selectByperId",method = RequestMethod.POST)
    @ResponseBody
    public Result selectByperId(@RequestBody Map<String,String> param){
        Result result=new Result();
        if(param.isEmpty()||param.get("workNum")==null||param.get("perId")==null){
            result.setMessage("人员账户参数错误");
            result.setSuccess(false);
            return result;
        }else{
            Tuser tuser=tuserService.selectByperId(param.get("perId"),param.get("workNum"));
            if(tuser==null){
                result.setSuccess(false);
                result.setMessage("人员账户信息查询失败");
            }else{
                result.setSuccess(true);
                result.setObject(tuser);
            }
            return result;
        }
    }
}
