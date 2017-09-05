package com.trasen.imis.controller;

import cn.trasen.commons.util.DownloadExcelUtil;
import cn.trasen.commons.util.StringUtil;
import com.trasen.imis.model.*;
import com.trasen.imis.service.*;
import com.trasen.imis.utils.DateUtils;
import jxl.CellType;
import jxl.write.DateFormat;
import jxl.write.WriteException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/3
 */
@Controller
@RequestMapping(value="/excel")
public class ExcelController {

    private static final Logger logger = Logger.getLogger(AttenceController.class);

    @Autowired
    AttenceService attenceService;

    @Autowired
    AttenceCountService attenceCountService;

    @Autowired
    PersonnelService personnelService;

    @Autowired
    ContractService contractService;

    @Autowired
    TalentPoolService talentPoolService;

    @Autowired
    TbTagPersonnelService tbTagPersonnelService;


    @RequestMapping(value = "/excelExport", method = RequestMethod.GET)
    public void excelExport(HttpServletResponse response, HttpServletRequest request) throws IOException, WriteException {

        String tagName=request.getParameter("tagName");
        String name=request.getParameter("name");
        String attenceDate=request.getParameter("attenceDate");
        String export=request.getParameter("export");
        String type=request.getParameter("type");
        String lackAtt=request.getParameter("lackAtt");
        String lateTime=request.getParameter("lateTime");
        String backTime=request.getParameter("backTime");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String signinType =request.getParameter("signinType");
        String createUser = request.getParameter("createUser");

        //Page page = new Page(1,10);
        AttenceVo attenceVo = new AttenceVo();
        if(name!=null&&!name.equals("undefined")){
            attenceVo.setName(name);
        }
        if(attenceDate!=null&&!attenceDate.equals("undefined")){
            attenceVo.setAttenceDate(attenceDate.substring(0,10));
        }
        if(tagName!=null&&!tagName.equals("undefined")){
            attenceVo.setTagName(tagName);
        }
        if(type!=null&&!type.equals("undefined")){
            attenceVo.setType(Integer.valueOf(type));
        }
        if(lateTime!=null){
            attenceVo.setLateTime(Long.valueOf(lateTime));
        }
        if(backTime!=null){
            attenceVo.setBackTime(Long.valueOf(backTime));
        }
        if(startDate!=null&&!startDate.equals("undefined")){
            attenceVo.setStartTime(startDate);
        }
        if(endDate!=null&&!endDate.equals("undefined")){
            attenceVo.setEndTime(endDate);
        }
        if(signinType!=null&&!signinType.equals("undefined")){
            attenceVo.setSigninType(signinType);
        }
        if(createUser!=null&&!createUser.equals("undefined")){
            attenceVo.setCreateUser(createUser);
        }
        List<AttenceVo> list = new ArrayList<>();
        if(!StringUtil.isEmpty(lackAtt)&&"lack".equals(lackAtt)){
            list = attenceService.excelExprotLackAttList(attenceVo);
        }else if(!StringUtil.isEmpty(lackAtt)&&"leave".equals(lackAtt)){
            list = attenceService.excelExprotLeaveAttList(attenceVo);
        }else{
            list = attenceService.excelExprotAttList(attenceVo);
        }
        String fileName=export+".xls";
        DownloadExcelUtil downloadExcelUtil=new DownloadExcelUtil(response,fileName,export);
        DateFormat dateFormat=new DateFormat("yyyy-mm-dd");

        downloadExcelUtil.addCell(0,0,"部门标签",CellType.LABEL,dateFormat,false,false);
        downloadExcelUtil.addCell(1,0,"员工",CellType.LABEL,dateFormat,false,false);
        downloadExcelUtil.addCell(2,0,"日期",CellType.LABEL,dateFormat,false,false);
        downloadExcelUtil.addCell(3,0,"星期",CellType.LABEL,dateFormat,false,false);

        Map<String,String> typeMap = new HashMap<>();
        typeMap.put("signIn","公司签到");
        typeMap.put("signOut","公司签退");
        typeMap.put("sign","外出考勤");
        typeMap.put("inEx","公司迟到");
        typeMap.put("outEx","公司早退");

        if(export.equals("考勤")){
            downloadExcelUtil.addCell(4,0,"签到时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"签到地点",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"签到类型",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"外出事由",CellType.LABEL,dateFormat,false,false);
        }else if(export.equals("迟到")){
            downloadExcelUtil.addCell(4,0,"签到时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"考勤地点",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"迟到时长",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"签到类型",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(8,0,"备注",CellType.LABEL,dateFormat,false,false);
        }else if(export.equals("早退")){
            downloadExcelUtil.addCell(4,0,"签退时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"考勤地点",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"早退时长",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"备注",CellType.LABEL,dateFormat,false,false);
        }else if(export.equals("缺勤")){
            downloadExcelUtil.addCell(4,0,"原因",CellType.LABEL,dateFormat,false,false);
        }else if(export.equals("加班")){
            downloadExcelUtil.addCell(4,0,"签到时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"签退时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"签到地点",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"签退地点",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(8,0,"加班时长",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(9,0,"加班事由",CellType.LABEL,dateFormat,false,false);
        }else if(export.equals("请假")){
            downloadExcelUtil.addCell(4,0,"请假类型",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"请假时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"操作人",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"请假事由",CellType.LABEL,dateFormat,false,false);
        }
        for(int i=0;i<list.size();i++){
            if(list.get(i).getTagName()==null){
                downloadExcelUtil.addCell(0,i+1,"",CellType.LABEL,dateFormat,false,false);
            }else{
                downloadExcelUtil.addCell(0,i+1,list.get(i).getTagName(),CellType.LABEL,dateFormat,false,false);
            }
            if(list.get(i).getName()==null){
                downloadExcelUtil.addCell(1,i+1,"",CellType.LABEL,dateFormat,false,false);
            }else{
                downloadExcelUtil.addCell(1,i+1,list.get(i).getName(),CellType.LABEL,dateFormat,false,false);
            }
            if(list.get(i).getAttenceDate()==null){
                downloadExcelUtil.addCell(2,i+1,"",CellType.DATE,dateFormat,false,false);
            }else{
                downloadExcelUtil.addCell(2,i+1,DateUtils.getDate(list.get(i).getAttenceDate()),CellType.DATE,dateFormat,false,false);
            }
            if(list.get(i).getWeek()==null){
                downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
            }else{
                downloadExcelUtil.addCell(3,i+1,DateUtils.getChinaWeek(list.get(i).getWeek()),CellType.LABEL,dateFormat,false,false);
            }
            if(export.equals("考勤")){
                if(list.get(i).getSigninTime()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,DateUtils.getTimes(list.get(i).getSigninTime()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSigninAddress()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getSigninAddress(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSigninType()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,typeMap.get(list.get(i).getSigninType()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }
            }else if(export.equals("迟到")){
                if(list.get(i).getSigninTime()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,DateUtils.getTimes(list.get(i).getSigninTime()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSigninAddress()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getSigninAddress(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getLateTime()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,list.get(i).getLateTime(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSigninType()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,typeMap.get(list.get(i).getSigninType()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(8,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(8,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }
            }else if(export.equals("早退")){
                if(list.get(i).getSignoutTime()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,DateUtils.getTimes(list.get(i).getSignoutTime()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSignoutAddress()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getSignoutAddress(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getBackTime()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,list.get(i).getBackTime(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }

            }else if(export.equals("缺勤")){
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }

            }else if(export.equals("加班")){
                if(list.get(i).getSigninTime()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,DateUtils.getTimes(list.get(i).getSigninTime()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSignoutTime()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,DateUtils.getTimes(list.get(i).getSignoutTime()),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSigninAddress()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,list.get(i).getSigninAddress(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSignoutAddress()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,list.get(i).getSignoutAddress(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getWorkHours()==null){
                    downloadExcelUtil.addCell(8,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(8,i+1,list.get(i).getWorkHours(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(9,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(9,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }

            }else if(export.equals("请假")){
                if(list.get(i).getType()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,list.get(i).getType(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getStartTime()==null&&list.get(i).getEndTime()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getStartTime()+"~"+list.get(i).getEndTime(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getCreateUser()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,list.get(i).getCreateUser(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }

            }
        }
        downloadExcelUtil.close();

    }

    @RequestMapping(value = "/excelContactExport", method = RequestMethod.GET)
    public void excelContactExport(HttpServletResponse response, HttpServletRequest request) throws IOException, WriteException {
        String countDate=request.getParameter("countDate");
        String name=request.getParameter("name");
        String tagName=request.getParameter("tagName");
        String column=request.getParameter("column");
        String selectCx=request.getParameter("selectCx");
        List<String> columnList = new ArrayList<>();

        int row = 1;

        if(!"undefined".equals(column)){
            String [] clumnStrs = column.split(",");
            if(clumnStrs.length>0){
                for(int i=0;i<clumnStrs.length;i++){
                    columnList.add(clumnStrs[i]);
                }
            }
        }

        if(columnList.size()==16){
            row = 2;
        }

        TbAttenceCount count = new TbAttenceCount();

        if(!selectCx.equals("undefined")){
            String [] selectArray = selectCx.split(",");

            for(int i=0;i<selectArray.length;i++){
                if(selectArray[i].equals("lateNum"))count.setLateNum(1);
                if(selectArray[i].equals("backNum")) count.setBackNum(1);
                if(selectArray[i].equals("lackNum")) count.setLackNum(1);
                if(selectArray[i].equals("maternityLeave")) count.setMaternityLeave(1);
                if(selectArray[i].equals("annualLeave")) count.setAnnualLeave(1);
                if(selectArray[i].equals("peternituLeave")) count.setPeternituLeave(1);
                if(selectArray[i].equals("maritalLeave")) count.setMaritalLeave(1);
                if(selectArray[i].equals("funeralLeave")) count.setFuneralLeave(1);
                if(selectArray[i].equals("affairLeave")) count.setAffairLeave(1);
                if(selectArray[i].equals("sickLeave")) count.setSickLeave(1);
                if(selectArray[i].equals("otherLeave")) count.setOtherLeave(1);
            }
        }

        if(!name.equals("undefined")){
            count.setName(name);
        }
        if(!countDate.equals("undefined")){
            count.setCountDate(countDate);
        }
        if(!tagName.equals("undefined")){
            count.setTagName(tagName);
        }

        List<TbAttenceCount> list = attenceCountService.queryAttenceCountList(count);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);

        String fileName="考勤统计"+dateNowStr+".xls";
        DownloadExcelUtil downloadExcelUtil=new DownloadExcelUtil(response,fileName,"考勤统计");
        DateFormat dateFormat=new DateFormat("yyyy-mm-dd");

        if(row==2){
            downloadExcelUtil.setMergeCells(0,0,0,1);
            downloadExcelUtil.addCell(0,0,"姓名",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.setMergeCells(1,0,1,1);
            downloadExcelUtil.addCell(1,0,"部门",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.setMergeCells(2,0,2,1);
            downloadExcelUtil.addCell(2,0,"应签天数",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.setMergeCells(3,0,3,1);
            downloadExcelUtil.addCell(3,0,"实签天数",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.setMergeCells(4,0,7,0);
            downloadExcelUtil.addCell(4,0,"签到情况",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(4,1,"正常出勤",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,1,"迟到天数",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,1,"早退天数",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,1,"旷工",CellType.LABEL,dateFormat,false,false);

            downloadExcelUtil.setMergeCells(8,0,12,0);
            downloadExcelUtil.addCell(8,0,"带薪假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(8,1,"年假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(9,1,"产假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(10,1,"陪产假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(11,1,"婚假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(12,1,"丧假",CellType.LABEL,dateFormat,false,false);

            downloadExcelUtil.setMergeCells(13,0,15,0);
            downloadExcelUtil.addCell(13,0,"扣薪假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(13,1,"事假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(14,1,"病假",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(15,1,"其他",CellType.LABEL,dateFormat,false,false);
        }else{
            int cla = 0;
            if(columnList.contains("name")) downloadExcelUtil.addCell(cla++,0,"姓名",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("tagName")) downloadExcelUtil.addCell(cla++,0,"部门",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("shouldSignNum")) downloadExcelUtil.addCell(cla++,0,"应签天数",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("factSignNum")) downloadExcelUtil.addCell(cla++,0,"实签天数",CellType.LABEL,dateFormat,false,false);

            if(columnList.contains("attNum")) downloadExcelUtil.addCell(cla++,0,"正常出勤",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("lateNum")) downloadExcelUtil.addCell(cla++,0,"迟到天数",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("backNum")) downloadExcelUtil.addCell(cla++,0,"早退天数",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("lackNum")) downloadExcelUtil.addCell(cla++,0,"旷工",CellType.LABEL,dateFormat,false,false);

            if(columnList.contains("annualLeave")) downloadExcelUtil.addCell(cla++,0,"年假",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("maternityLeave")) downloadExcelUtil.addCell(cla++,0,"产假",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("peternituLeave")) downloadExcelUtil.addCell(cla++,0,"陪产假",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("maritalLeave")) downloadExcelUtil.addCell(cla++,0,"婚假",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("funeralLeave")) downloadExcelUtil.addCell(cla++,0,"丧假",CellType.LABEL,dateFormat,false,false);

            if(columnList.contains("affairLeave")) downloadExcelUtil.addCell(cla++,0,"事假",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("sickLeave")) downloadExcelUtil.addCell(cla++,0,"病假",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("otherLeave")) downloadExcelUtil.addCell(cla++,0,"其他",CellType.LABEL,dateFormat,false,false);
        }




        for(int i=0;i<list.size();i++){
            int cl = 0;
            if(columnList.contains("name")){
                if(list.get(i).getName()==null){
                    downloadExcelUtil.addCell(cl++,i+row,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(cl++,i+row,list.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                }

            }
            if(columnList.contains("tagName")){
                if(list.get(i).getTagName()==null){
                    downloadExcelUtil.addCell(cl++,i+row,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(cl++,i+row,list.get(i).getTagName(),CellType.LABEL,dateFormat,false,false);
                }
            }
            if(columnList.contains("shouldSignNum")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getShouldSignNum(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("factSignNum")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getFactSignNum(),CellType.LABEL,dateFormat,false,false);

            if(columnList.contains("attNum")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getAttNum(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("lateNum")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getLateNum(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("backNum")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getBackNum(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("lackNum")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getLackNum(),CellType.LABEL,dateFormat,false,false);

            if(columnList.contains("annualLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getAnnualLeave(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("maternityLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getMaternityLeave(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("peternituLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getPeternituLeave(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("maritalLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getMaritalLeave(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("funeralLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getFuneralLeave(),CellType.LABEL,dateFormat,false,false);

            if(columnList.contains("affairLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getAffairLeave(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("sickLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getSickLeave(),CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("otherLeave")) downloadExcelUtil.addCell(cl++,i+row,list.get(i).getOtherLeave(),CellType.LABEL,dateFormat,false,false);
        }

        downloadExcelUtil.close();

    }

    @RequestMapping(value = "/excelPersonExprot",method = RequestMethod.GET)
    private void  excelPersonExprot(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> param=new HashMap<String,Object>();
        String depName=request.getParameter("depName");
        String name=request.getParameter("name");
        String position=request.getParameter("position");
        String sex=request.getParameter("sex");
        String workNum=request.getParameter("workNum");
        String birthdayType=request.getParameter("birthdayType");

        String SocialStart=request.getParameter("SocialStart");
        String SocialEnd=request.getParameter("SocialEnd");

        String AccumulationStart=request.getParameter("AccumulationStart");
        String AccumulationEnd=request.getParameter("AccumulationEnd");

        String BirthdayStart=request.getParameter("BirthdayStart");
        String BirthdayEnd=request.getParameter("BirthdayEnd");

        if(!name.equals("undefined")){
            param.put("name",name);
        }
        if(!depName.equals("undefined")){
            param.put("depName",depName);
        }
        if(!position.equals("undefined")){
            param.put("position",position);
        }
        if(!sex.equals("undefined")){
            param.put("sex",sex);
        }
        if(!workNum.equals("undefined")){
            param.put("workNum",workNum);
        }
        if(!birthdayType.equals("undefined")){
            param.put("birthdayType",birthdayType);
        }
        if(!SocialStart.equals("undefined")){
            param.put("SocialStart",SocialStart);
        }
        if(!SocialEnd.equals("undefined")){
            param.put("SocialEnd",SocialEnd);
        }
        if(!AccumulationStart.equals("undefined")){
            param.put("AccumulationStart",AccumulationStart);
        }
        if(!AccumulationEnd.equals("undefined")){
            param.put("AccumulationEnd",AccumulationEnd);
        }
        if(!BirthdayStart.equals("undefined")){
            param.put("BirthdayStart",BirthdayStart);
        }
        if(!BirthdayEnd.equals("undefined")){
            param.put("BirthdayEnd",BirthdayEnd);
        }

        List<TbPersonnel> list = personnelService.searchExcelPersonnelList(param);
        String fileName="人员信息"+".xls";
        DownloadExcelUtil downloadExcelUtil= null;
        try {
            downloadExcelUtil = new DownloadExcelUtil(response,fileName,"人员信息");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            String sex_list="男",birthdayType_list="阴历";
            downloadExcelUtil.addCell(0,0,"姓名",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(1,0,"性别",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(2,0,"工号",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(3,0,"部门",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(4,0,"手机号码",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"身份证号",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"家庭住址",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"生日",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(8,0,"生日类别",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(9,0,"紧急联系人",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(10,0,"联系人电话",CellType.LABEL,dateFormat,false,false);

            for(int i=0;i<list.size();i++){
                downloadExcelUtil.addCell(0,i+1,list.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getSex()==1){
                    sex_list="男";
                }else{
                    sex_list="女";
                }
                if(list.get(i).getBirthdayType()!=null){
                    if(list.get(i).getBirthdayType()==1){
                        birthdayType_list="公历";
                    }else{
                        birthdayType_list="阳历";
                    }
                }
                downloadExcelUtil.addCell(1,i+1,sex_list,CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getWorkNum()==null){
                    downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(2,i+1,list.get(i).getWorkNum(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getDepName()==null){
                    downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(3,i+1,list.get(i).getDepName(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getPhone()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,list.get(i).getPhone(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getIdCard()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getIdCard(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getHouseAddress()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,list.get(i).getHouseAddress(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getBirthday()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,list.get(i).getBirthday(),CellType.LABEL,dateFormat,false,false);
                }
                downloadExcelUtil.addCell(8,i+1,birthdayType_list,CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getLinkman()==null){
                    downloadExcelUtil.addCell(9,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(9,i+1,list.get(i).getLinkman(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getLinkPhone()==null){
                    downloadExcelUtil.addCell(10,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(10,i+1,list.get(i).getLinkPhone(),CellType.LABEL,dateFormat,false,false);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/excelPersonAddressExprot",method = RequestMethod.GET)
    private void  excelPersonAddressExprot(HttpServletRequest request,HttpServletResponse response){
        // TbPersonnel tbPersonnel = new TbPersonnel();
        Map<String,Object> param=new HashMap<String,Object>();
        List<TbPersonnel> list = personnelService.searchAddressPersonnelList(param);
        String tagName=request.getParameter("tagName");

        List<String> columnList = new ArrayList<>();
        if(!"undefined".equals(tagName)){
            String[] tagNameArray=tagName.split(",");
            if(tagNameArray.length>0){
                for(int i=0;i<tagNameArray.length;i++){
                    columnList.add(tagNameArray[i]);
                }
            }
        }
        String fileName="人员通讯录"+".xls";
        DownloadExcelUtil downloadExcelUtil= null;
        try {
            downloadExcelUtil = new DownloadExcelUtil(response,fileName,"通讯录");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            downloadExcelUtil.setMergeCells(0,0,columnList.size()-1,0);
            downloadExcelUtil.addCell(0,0,"湖南创星科技股份有限公司员工通讯录（"+list.size()+"人）",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.setMergeCells(0,1,columnList.size()-1,1);
            downloadExcelUtil.addCell(0,1,"电话：85482099  传真：85482098  售后维护：85482199",CellType.LABEL,dateFormat,false,false);
            int cal=0;
            if(columnList.contains("workNum"))downloadExcelUtil.addCell(cal++,2,"编号",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("name"))downloadExcelUtil.addCell(cal++,2,"姓名",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("sex"))downloadExcelUtil.addCell(cal++,2,"性别",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("dpet"))downloadExcelUtil.addCell(cal++,2,"部门",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("position"))downloadExcelUtil.addCell(cal++,2,"职位信息",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("phone"))downloadExcelUtil.addCell(cal++,2,"手机号码",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("szz"))downloadExcelUtil.addCell(cal++,2,"所在组",CellType.LABEL,dateFormat,false,false);
            if(columnList.contains("remark"))downloadExcelUtil.addCell(cal++,2,"备注",CellType.LABEL,dateFormat,false,false);
            /*for(int i=0;i<tagNameArray.length;i++){
                if(tagNameArray[i].equals("workNum"))downloadExcelUtil.addCell(i,2,"编号",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("name"))downloadExcelUtil.addCell(i,2,"姓名",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("sex"))downloadExcelUtil.addCell(i,2,"性别",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("dpet"))downloadExcelUtil.addCell(i,2,"部门",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("position"))downloadExcelUtil.addCell(i,2,"职位信息",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("phone"))downloadExcelUtil.addCell(i,2,"手机号码",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("szz"))downloadExcelUtil.addCell(i,2,"所在组",CellType.LABEL,dateFormat,false,false);
                if(tagNameArray[i].equals("remark"))downloadExcelUtil.addCell(i,2,"备注",CellType.LABEL,dateFormat,false,false);
            }*/

            String tag_g_Name=null;
            String sex_name=null;
            for(int i=0;i<list.size();i++){
                int cl=0;
                if(columnList.contains("workNum")){
                    if(list.get(i).getWorkNum()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,list.get(i).getWorkNum(),CellType.LABEL,dateFormat,false,false);
                    }
                }
                if(columnList.contains("name")){
                    if(list.get(i).getName()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,list.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                    }
                }
                if(columnList.contains("sex")){
                    if(list.get(i).getSex()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        if(list.get(i).getSex()==1)sex_name="男";
                        if(list.get(i).getSex()==2)sex_name="女";
                        downloadExcelUtil.addCell(cl++,i+3,sex_name,CellType.LABEL,dateFormat,false,false);
                    }
                }

                if(columnList.contains("dpet")){
                    if(list.get(i).getDepName()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,list.get(i).getDepName(),CellType.LABEL,dateFormat,false,false);
                    }
                }

                if(columnList.contains("position")){
                    if(list.get(i).getPosition()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,list.get(i).getPosition(),CellType.LABEL,dateFormat,false,false);
                    }
                }
                if(columnList.contains("phone")){
                    if(list.get(i).getPhone()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,list.get(i).getPhone(),CellType.LABEL,dateFormat,false,false);
                    }
                }
                if(columnList.contains("szz")){
                    tag_g_Name=tbTagPersonnelService.getTagNameforWorNum(list.get(i).getWorkNum());
                    if(tag_g_Name==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,tag_g_Name,CellType.LABEL,dateFormat,false,false);
                    }
                }
                if(columnList.contains("remark")){
                    if(list.get(i).getRemark()==null){
                        downloadExcelUtil.addCell(cl++,i+3,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(cl++,i+3,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/excelContractExprot",method = RequestMethod.GET)
    private void  excelContractExprot(HttpServletRequest request,HttpServletResponse response){
        String name=request.getParameter("name");
        String workNum=request.getParameter("workNum");
        Map<String,String> param=new HashMap<String,String>();

        if(!name.equals("undefined")){
            param.put("name",name);
        }
        if(!workNum.equals("undefined")){
            param.put("workNum",workNum);
        }
        List<TbContract> tbContractList=contractService.getTbContractList(param);
        String fileName="劳动合同"+".xls";
        DownloadExcelUtil downloadExcelUtil= null;
        try {
            downloadExcelUtil = new DownloadExcelUtil(response,fileName,"劳动合同");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            downloadExcelUtil.addCell(0,0,"姓名",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(1,0,"工号",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(2,0,"合同编号",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(3,0,"入职时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(4,0,"转正日期",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"合同签订情况",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"合同开始时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"合同结束时间",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(8,0,"合同年限",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(9,0,"合同类型",CellType.LABEL,dateFormat,false,false);
            String status="未签",type="";
            for(int i=0;i<tbContractList.size();i++){

                if(tbContractList.get(i).getRegularDate()==null){
                    downloadExcelUtil.addCell(0,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(0,i+1,tbContractList.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getWorkNum()==null){
                    downloadExcelUtil.addCell(1,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(1,i+1,tbContractList.get(i).getWorkNum(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getConCode()==null){
                    downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(2,i+1,tbContractList.get(i).getConCode(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getEntryDate()==null){
                    downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(3,i+1,tbContractList.get(i).getEntryDate(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getRegularDate()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,tbContractList.get(i).getRegularDate(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getStatus()==null||tbContractList.get(i).getStatus()==0)status="未签";
                if(tbContractList.get(i).getStatus()==1)status="已签";
                downloadExcelUtil.addCell(5,i+1,status,CellType.LABEL,dateFormat,false,false);

                if(tbContractList.get(i).getStartDate()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,tbContractList.get(i).getStartDate(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getEndDate()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,tbContractList.get(i).getEndDate(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbContractList.get(i).getYears()==null){
                    downloadExcelUtil.addCell(8,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(8,i+1,tbContractList.get(i).getYears(),CellType.LABEL,dateFormat,false,false);
                }

                if(tbContractList.get(i).getType()==null){
                    type="";
                }else if(tbContractList.get(i).getType()==1){
                    type="劳动合同";
                }else if(tbContractList.get(i).getType()==2){
                    type="保密协议";
                }else if(tbContractList.get(i).getType()==3){
                    type="竞业协议";
                }else if(tbContractList.get(i).getType()==4){
                    type="培训协议";
                }

                downloadExcelUtil.addCell(9,i+1,type,CellType.LABEL,dateFormat,false,false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/excelTalentExprot",method = RequestMethod.GET)
    private void  excelTalentExprot(HttpServletRequest request, HttpServletResponse response){
        String name=request.getParameter("name");
        String willJob=request.getParameter("willJob");
        String beGood=request.getParameter("beGood");
        String isComeCx=request.getParameter("isCome");
        String result_selct=request.getParameter("result");
        TbTalentPool tbTalentPool = new TbTalentPool();
        if(!name.equals("undefined")){
            tbTalentPool.setName(name);
        }
        if(!willJob.equals("undefined")){
            tbTalentPool.setWillJob(willJob);
        }
        if(!beGood.equals("undefined")){
            tbTalentPool.setBeGood(beGood);
        }
        if(!isComeCx.equals("undefined")){
            tbTalentPool.setIsCome(Integer.valueOf(isComeCx));
        }
        if(!result_selct.equals("undefined")){
            tbTalentPool.setResult(result_selct);
        }
        List<TbTalentPool> list = talentPoolService.searchTalentPoolList(tbTalentPool);
        String fileName="人才库"+".xls";
        String result="";
        DownloadExcelUtil downloadExcelUtil= null;
        try {
            downloadExcelUtil = new DownloadExcelUtil(response,fileName,"人才库");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            String sex="男";
            String isCome="是";

            downloadExcelUtil.addCell(0,0,"姓名", CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(1,0,"性别",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(2,0,"联系方式",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(3,0,"就业年限",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(4,0,"意向岗位",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"擅长技术",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(6,0,"毕业学校",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(7,0,"学历",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(8,0,"相关证书",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(9,0,"是否来过公司",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(10,0,"面试结果",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(11,0,"储备岗位",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(12,0,"备注",CellType.LABEL,dateFormat,false,false);

            for(int i=0;i<list.size();i++){
                if(list.get(i).getName()==null){
                    downloadExcelUtil.addCell(0,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(0,i+1,list.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                }

                if(list.get(i).getSex()==1){
                    sex="男";
                }else{
                    sex="女";
                }
                downloadExcelUtil.addCell(1,i+1,sex,CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getPhone()==null){
                    downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(2,i+1,list.get(i).getPhone(),CellType.LABEL,dateFormat,false,false);
                }

                String today = DateUtils.getDate(new Date(),"yyyy-MM-dd");
                long workYears = 1;
                if(list.get(i).getWorkDate()!=null){
                    long countDiffCount = DateUtils.dateDiff(list.get(i).getWorkDate(), today, "yyyy-MM-dd", "d");
                    /*DecimalFormat df  = new DecimalFormat("######0.00");
                    df.format(d1);*/
                    double year = countDiffCount/365d;
                    workYears = Math.round(year);
                    if(workYears==0){
                        workYears = 1;
                    }
                    downloadExcelUtil.addCell(3,i+1,workYears,CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getWillJob()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,list.get(i).getWillJob(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getBeGood()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getBeGood(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getSchool()==null){
                    downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(6,i+1,list.get(i).getSchool(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getEducation()==null){
                    downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(7,i+1,list.get(i).getEducation(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getCertificate()==null){
                    downloadExcelUtil.addCell(8,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(8,i+1,list.get(i).getCertificate(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getIsCome()==1){
                    isCome="是";
                }else{
                    isCome="否";
                }
                downloadExcelUtil.addCell(9,i+1,isCome,CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getResult()==null){
                    downloadExcelUtil.addCell(10,i+1,result,CellType.LABEL,dateFormat,false,false);
                }else{

                    if(list.get(i).getResult().equals("1")) result="暂未面试";
                    if(list.get(i).getResult().equals("2")) result="已面试-部门经理暂定";
                    if(list.get(i).getResult().equals("3")) result="已面试-人事部暂定";
                    if(list.get(i).getResult().equals("4")) result="已通过-人事部待通知";
                    if(list.get(i).getResult().equals("5")) result="已通过-人事部已通知";

                    downloadExcelUtil.addCell(10,i+1,result,CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getReserve()==null){
                    downloadExcelUtil.addCell(11,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(11,i+1,list.get(i).getReserve(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getRemark()==null){
                    downloadExcelUtil.addCell(12,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(12,i+1,list.get(i).getRemark(),CellType.LABEL,dateFormat,false,false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //====离职信息=======
    @RequestMapping(value = "/excelQuitPersonExprot",method = RequestMethod.GET)
    private void  excelQuitPersonExprot(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> param=new HashMap<String,Object>();
        String depName=request.getParameter("depName");
        String name=request.getParameter("name");
        String position=request.getParameter("position");
        String sex=request.getParameter("sex");
        String workNum=request.getParameter("workNum");

        if(!name.equals("undefined")){
            param.put("name",name);
        }
        if(!depName.equals("undefined")){
            param.put("depName",depName);
        }
        if(!position.equals("undefined")){
            param.put("position",position);
        }
        if(!sex.equals("undefined")){
            param.put("sex",sex);
        }
        if(!workNum.equals("undefined")){
            param.put("workNum",workNum);
        }
        List<TbPersonnel> list = personnelService.searchExcelQuitPersonnelList(param);
        String fileName="离职人员信息"+".xls";
        DownloadExcelUtil downloadExcelUtil= null;
        try {
            downloadExcelUtil = new DownloadExcelUtil(response,fileName,"离职人员信息");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            String sex_list="男";
            downloadExcelUtil.addCell(0,0,"姓名",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(1,0,"性别",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(2,0,"工号",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(3,0,"部门",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(4,0,"手机号码",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"身份证号",CellType.LABEL,dateFormat,false,false);
            for(int i=0;i<list.size();i++){
                downloadExcelUtil.addCell(0,i+1,list.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getSex()==1){
                    sex_list="男";
                }else{
                    sex_list="女";
                }
                downloadExcelUtil.addCell(1,i+1,sex_list,CellType.LABEL,dateFormat,false,false);
                if(list.get(i).getWorkNum()==null){
                    downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(2,i+1,list.get(i).getWorkNum(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getDepName()==null){
                    downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(3,i+1,list.get(i).getDepName(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getPhone()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,list.get(i).getPhone(),CellType.LABEL,dateFormat,false,false);
                }
                if(list.get(i).getIdCard()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,list.get(i).getIdCard(),CellType.LABEL,dateFormat,false,false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
