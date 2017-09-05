package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.common.VisitInfoHolder;
import com.trasen.imis.model.TbTagPersonnel;
import com.trasen.imis.service.TbTagPersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/13
 */
@Controller
@RequestMapping(value="/tag")
public class TagPersonnelController {
    @Autowired
    private TbTagPersonnelService tbTagPersonnelService;

    @RequestMapping(value="/getTaTagPersonnelList",method = RequestMethod.GET)
    @ResponseBody
    public Result getTaTagPersonnelList(@QueryParam("workNum") String workNum){
        Result result=new Result();
        List<TbTagPersonnel> tbTagPersonnelList=tbTagPersonnelService.getTaTagPersonnelList(workNum);
        result.setSuccess(true);
        result.setObject(tbTagPersonnelList);

        return result;
    }

    @RequestMapping(value="/saveTaTagPersonnel",method = RequestMethod.POST)
    @ResponseBody
    public Result saveTaTagPersonnel(@RequestBody List<Map<String,Object>> parm){

        tbTagPersonnelService.deleteTaTagPersonnelForWorkNum(parm.get(0).get("workNum").toString());
        Result result=new Result();
        //TODO 修改操作人
        for(int i=0;i<parm.size();i++){
            parm.get(i).put("created",new Date());
            parm.get(i).put("operator", VisitInfoHolder.getUserId());
        }
        System.out.println(parm.get(0).get("tagName"));
        if(parm.get(0).get("tagName")!=null){
            tbTagPersonnelService.saveTagPersonnel(parm);
        }

        result.setSuccess(true);
        result.setMessage("保存成功");
       return result;
    }

}
