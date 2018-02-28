package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.model.*;
import com.trasen.imis.service.TbPerformanceService;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.ImportExcelUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 18/2/26.
 */
@Controller
@RequestMapping("/performance")
public class TbPerformanceController {

    private static final Logger logger = Logger.getLogger(TbPerformanceController.class);

    @Autowired
    TbPerformanceService tbPerformanceService;


    @ResponseBody
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result importExcel(HttpServletRequest request, HttpServletResponse response,String date) {
        Result result=new Result();
        List<TbPerformance> performanceList = new ArrayList<>();
        result.setSuccess(false);
        result.setMessage("上传失败");

        response.addHeader("key","value");

        if(tbPerformanceService.isImport(date)){
            result.setMessage("日期["+date+"]绩效已经导入,不可重复导入!");
            result.setObject(performanceList);
            return result;
        }


        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession()
                .getServletContext());
        if(commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipart = multipartHttpServletRequest.getMultiFileMap().get("file").get(0);

            if(multipart!=null){
                String fileName = multipart.getOriginalFilename();
                String saveFileUrl = PropertiesUtils.getProperty("saveFileUrl");
                if(saveFileUrl==null){
                    saveFileUrl = "/ucenter/cs/driverFile/imisFile/";
                }

                try {
                    File fileDir = new File(saveFileUrl);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    File tmpFile = new File(saveFileUrl+ System.getProperty("file.separator") +fileName);
                    multipart.transferTo(tmpFile);
                    if(!tmpFile.isFile()) {
                        return result;
                    }
                    List<List<Object>> dataList= ImportExcelUtil.importExcel(tmpFile);

                    for (int i = 1; i < dataList.size(); i++) {
                        if(dataList.get(i).size()==4&&dataList.get(i).get(0)!=null
                                &&dataList.get(i).get(1)!=null&&dataList.get(i).get(2)!=null
                                &&dataList.get(i).get(3)!=null&&date!=null){
                            TbPerformance performance = new TbPerformance();
                            performance.setImportDate(date);
                            performance.setWorkNum(dataList.get(i).get(0).toString().split("\\.")[0]);
                            performance.setName(dataList.get(i).get(1).toString());
                            performance.setScore(dataList.get(i).get(2).toString());
                            performance.setGrade(dataList.get(i).get(3).toString());
                            performanceList.add(performance);
                        }
                    }
                    tbPerformanceService.savePerformance(performanceList);

                    List<TbPerformance> list = tbPerformanceService.queryPerformance(date);

                    result.setSuccess(true);
                    result.setStatusCode(1);
                    result.setMessage("绩效导入成功,共计导入"+performanceList.size()+"条");
                    result.setObject(list);
                    tmpFile.delete();
                } catch(IOException e) {
                    logger.error("文件上传异常:" + e.getMessage(), e);
                }

            }

        }
        return result;
    }



    @ResponseBody
    @RequestMapping(value="/updateJf", method = RequestMethod.POST)
    public Result updateJf(@RequestBody TbPerformanceJF tbPerformanceJF)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("更新失败");
        try {
            //数据更新
            if(tbPerformanceJF!=null){
                List<Map<String,Object>> list = new ArrayList<>();
                if(tbPerformanceJF.getFine()!=null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("code","fine");
                    map.put("value",tbPerformanceJF.getFine());
                    list.add(map);
                }
                if(tbPerformanceJF.getGood()!=null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("code","good");
                    map.put("value",tbPerformanceJF.getGood());
                    list.add(map);
                }
                if(tbPerformanceJF.getMiddling()!=null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("code","middling");
                    map.put("value",tbPerformanceJF.getMiddling());
                    list.add(map);
                }
                if(tbPerformanceJF.getPass()!=null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("code","pass");
                    map.put("value",tbPerformanceJF.getPass());
                    list.add(map);
                }
                if(tbPerformanceJF.getFlunk()!=null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("code","flunk");
                    map.put("value",tbPerformanceJF.getFlunk());
                    list.add(map);
                }
                tbPerformanceService.updatePerformanceJF(list);
                result.setSuccess(true);
                result.setMessage("更新成功!");
                result.setObject(tbPerformanceJF);
            }

        }catch (Exception e) {
            logger.error("绩效积分系数保存异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }

    @ResponseBody
    @RequestMapping(value="/getJF", method = RequestMethod.POST)
    public Result getJF()  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            TbPerformanceJF tbPerformanceJF = tbPerformanceService.getJF();
            result.setSuccess(true);
            result.setObject(tbPerformanceJF);
        }catch (Exception e) {
            logger.error("绩效积分系数保存异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }

    @ResponseBody
    @RequestMapping(value="/queryPerformance", method = RequestMethod.POST)
    public Result queryPerformance(@RequestParam String date)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            List<TbPerformance> list = tbPerformanceService.queryPerformance(date);
            result.setSuccess(true);
            result.setObject(list);
        }catch (Exception e) {
            logger.error("绩效积分系数保存异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }


    @ResponseBody
    @RequestMapping(value="/autoAddJf", method = RequestMethod.POST)
    public Result autoAddJf(@RequestParam String date)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            Integer num = tbPerformanceService.countPerforJF(date);
            if(num>0){
                result.setSuccess(false);
                result.setMessage("日期["+date+"]绩效积分已经处理,不可重复添加!");
                return  result;
            }

            List<TbJfRecord> tbJfRecordList = tbPerformanceService.getJfRecordf(DateUtils.getYearMonth(date));
            if(tbJfRecordList==null||tbJfRecordList.size()<=0){
                result.setSuccess(false);
                result.setMessage("日期["+date+"]还未生成考勤积分!");
                return  result;
            }


            List<TbPerformance> tbPerformances = tbPerformanceService.getPerformanceToDate(date);
            if(tbPerformances==null||tbPerformances.size()<=0){
                result.setSuccess(false);
                result.setMessage("日期["+date+"]还未导入绩效或者已经完成绩效积分操作!");
                return  result;
            }

            int handNUm = tbPerformanceService.autoAddJf(tbJfRecordList,tbPerformances,date);

            result.setSuccess(true);
            result.setMessage("日期["+date+"]绩效积分全部添加完成,共计处理"+handNUm+"位员工!");
        }catch (Exception e) {
            logger.error("绩效积分添加出现异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("绩效积分添加出现异常");
        }
        return  result;

    }


}
