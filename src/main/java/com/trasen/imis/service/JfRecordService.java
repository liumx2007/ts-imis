package com.trasen.imis.service;

import com.trasen.imis.dao.JfRecordMapper;
import com.trasen.imis.model.TbAttenceCount;
import com.trasen.imis.model.TbJfRecord;
import com.trasen.imis.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/8
 */
@Component
public class JfRecordService {

    @Autowired
    private JfRecordMapper jfRecordMapper;

    @Autowired
    private JfLevelService jfLevelService;

    public void savejfRecordFor_attence(String count_date){
        Map<String,String> param=new HashMap<String,String>();
        List<TbAttenceCount> tbAttenceCountList=jfRecordMapper.getattcenCounttList(count_date);
        if(tbAttenceCountList!=null){
            for(TbAttenceCount tbAttenceCount:tbAttenceCountList){
                param.put("workNum",tbAttenceCount.getWorkNum());
                param.put("yearMonth", DateUtils.getDate(new Date(),"yyyy-MM"));
                int count=jfRecordMapper.getSystemBonus(param);
                if(count==0) {
                    //获取当月自然月天数
                    int days = DateUtils.getDaysByYearMonth(Integer.parseInt(count_date.substring(0,4))
                            ,Integer.parseInt(count_date.substring(4,6)));
                    //获取应签天数
                    int should = tbAttenceCount.getShouldSignNum();
                    //获取实签天数
                    int fact = tbAttenceCount.getFactSignNum();
                    //获取迟到天数
                    int late = tbAttenceCount.getLateNum();

                    //todo 获取未签天数 这个指标是旷工,要利用这个指标考勤规则必须严格执行。
                    //todo 当前人事并没有严格制定考勤规则,所以使用该指标会有问题。
                    int lack = tbAttenceCount.getLackNum();

                    //获取请假天数
                    int leave = tbAttenceCount.getAnnualLeave()+tbAttenceCount.getMaternityLeave()+tbAttenceCount.getPeternituLeave()
                            +tbAttenceCount.getMaritalLeave()+tbAttenceCount.getFuneralLeave()+tbAttenceCount.getAffairLeave()
                            +tbAttenceCount.getSickLeave()+tbAttenceCount.getOtherLeave();

                    //加班
                    Map<String,Object> addMap = new HashMap<>();
                    addMap.put("workNum",tbAttenceCount.getWorkNum());
                    addMap.put("date",count_date);
                    Integer addWork = jfRecordMapper.getAddWorkNum(addMap);


                    int num = 0;
                    String remark = count_date.substring(0,4)+"年"+count_date.substring(4,6)+"月";
                    if((leave+late)==0){//全勤判断 请假+迟到=0
                        if(addWork>0){//有加班情况
                            num = days+addWork;
                            remark = remark + "全勤，出勤"+num+"分(加班"+addWork+"天)。";
                        }else{//无加班情况
                            num = days;
                            remark = remark + "全勤，出勤"+num+"分。";
                        }
                    }else{
                        num = fact+addWork;
                        String lateStr = "";
                        if(late>0){
                            lateStr = "迟到"+late+"次 ";
                            num = num - late;
                        }

                        String leaveStr = "";
                        if(leave>0){
                            leaveStr = "请假"+leave+"次 ";
                        }

                        String addWorkStr = "";
                        if(addWork>0){
                            addWorkStr = "加班"+addWork+"天";
                        }
                        remark = remark + "缺勤，出勤"+num+"分（"+lateStr+leaveStr+addWorkStr+"）。";
                    }
                    TbJfRecord tbJfRecord = new TbJfRecord();
                    tbJfRecord.setWorkNum(tbAttenceCount.getWorkNum());
                    tbJfRecord.setType(3);
                    tbJfRecord.setScore(num);
                    tbJfRecord.setRemark(remark);
                    tbJfRecord.setStatus(1);
                    tbJfRecord.setCreated(new Date());
                    tbJfRecord.setIsShow(1);
                    tbJfRecord.setCreateUser("系统");
                    jfLevelService.addJfRecord(tbJfRecord);
                }
            }

        }
    }

}
