package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasen.imis.model.TbContract;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.service.ContractService;
import com.trasen.imis.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/18
 */
@Controller
@RequestMapping(value="/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private PersonnelService personnelService;

    @RequestMapping(value="/getTbContractList",method = RequestMethod.POST)
    @ResponseBody
    public String getTbContractList(@RequestBody Map<String,String> param) throws JsonProcessingException {
        String pageNo=param.get("pageNo");
        String pageSize=param.get("pageSize");
        String json=null;
        if(pageNo.isEmpty()||pageSize.isEmpty()){
            json="{\"message\": " + "传入参数错误" + ",\"code\": "+"-1"+"}";
            return json;
        }
        Page page=new Page(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        page.setSidx("pkid");
        page.setSord("desc");
        List<TbContract> tbContractList=contractService.getTbContractList(param,page);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(tbContractList);
        json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+",\"code\": "+"1"+"}";
        return json;
    }

    @RequestMapping(value="/updateContract",method = RequestMethod.POST)
    @ResponseBody
    public Result updateContract(@RequestBody TbContract tbContract){
        Result result=new Result();
        if(tbContract==null){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }
        if(tbContract.getPkid()!=null){
            int contractInt=contractService.updateContract(tbContract);
            if(contractInt>0){
                result.setMessage("数据更新成功");
                result.setSuccess(true);
            }else{
                result.setMessage("数据无更新");
                result.setSuccess(false);
            }
        }else{
            contractService.insertContract(tbContract);
            result.setMessage("数据保存成功");
            result.setSuccess(true);

        }
        result.setObject(tbContract);
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/findWorkNumForContrach",method=RequestMethod.POST)
    public Result findWorkNumRepeat(@RequestBody Map<String,String> mapDeptId) throws JsonProcessingException {
        Result result=new Result();
        if(mapDeptId.get("workNum")==null||mapDeptId.get("workNum")=="unfinded"){
            result.setMessage("参数错误");
            result.setSuccess(false);
            return result;
        }

            TbPersonnel tbPersonnel=personnelService.findWorkNumForPersonnel(mapDeptId.get("workNum"));
            if(tbPersonnel==null){
                result.setSuccess(false);
                result.setMessage("请检查你输入的工号是否正确，公司无此工号");
            }else{
                result.setSuccess(true);
                result.setObject(tbPersonnel);
            }
        return result;
    }

}
