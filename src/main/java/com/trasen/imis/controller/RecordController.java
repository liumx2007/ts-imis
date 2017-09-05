package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.model.TbJfRank;
import com.trasen.imis.service.RecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/31
 */
@Controller
@RequestMapping(value="/record")
public class RecordController {

    private static final Logger logger = Logger.getLogger(AttenceController.class);

    @Autowired
    private RecordService recordService;

    @ResponseBody
    @RequestMapping(value="/getRecordRankList",method = RequestMethod.POST)
    public Result getRecordRankList(){
        Result result=new Result();
        List<TbJfRank> tbJfRankList=recordService.getRecordRankList();
        if(tbJfRankList==null){
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }else{
            result.setSuccess(true);
            result.setObject(tbJfRankList);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/deleteRecordRank",method = RequestMethod.POST)
    public Result deleteRecordRank(@RequestBody TbJfRank tbJfRank){
        Result result=new Result();
        if(tbJfRank==null){
            result.setSuccess(false);
            result.setMessage("参数错误，删除失败");
        }else{
            int deleteCount=recordService.deleteRecordRank(tbJfRank);
            if(deleteCount==0){
                result.setSuccess(false);
                result.setMessage("删除失败");
            }else{
                result.setSuccess(true);
                result.setMessage("删除成功");
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/saveRecordLevel",method =RequestMethod.POST)
    public Result saveRecordLevel(@RequestBody TbJfRank tbJfRank){
        Result result=new Result();
        if(tbJfRank==null){
            result.setSuccess(false);
            result.setMessage("参数错误，删除失败");
        }else{

            if(tbJfRank.getPkid()!=null){
              int  updateCount=recordService.updateRecordLevel(tbJfRank);
              if(updateCount>0){
                  result.setSuccess(true);
                  result.setMessage("更新成功");
              }else{
                  result.setSuccess(false);
                  result.setMessage("数据更新失败");
              }
            }else{
              int  insertCount=recordService.insertRecordLevel(tbJfRank);
                if(insertCount>0){
                    logger.info("=========="+tbJfRank.getPkid());
                    result.setSuccess(true);
                    result.setMessage("新增数据成功");
                    result.setObject(tbJfRank.getPkid());
                }else{
                    result.setSuccess(false);
                    result.setMessage("新增数据失败");
                }
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/deleteMoreRecordRank",method =RequestMethod.POST)
    public Result deleteMoreRecordRank(@RequestBody List<String> pkidList){
        Result result=new Result();
        int deleCount=recordService.deleteMoreRecordRank(pkidList);
        if(deleCount==0){
            result.setMessage("数据删除失败");
            result.setSuccess(false);
        }else{
            result.setMessage("数据删除成功");
            result.setSuccess(true);
        }
        return result;
    }

}
