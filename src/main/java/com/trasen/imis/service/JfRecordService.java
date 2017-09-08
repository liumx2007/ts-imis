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
                    TbJfRecord tbJfRecord = new TbJfRecord();
                    tbJfRecord.setWorkNum(tbAttenceCount.getWorkNum());
                    tbJfRecord.setType(3);
                    tbJfRecord.setScore(tbAttenceCount.getFactSignNum());
                    tbJfRecord.setRemark(count_date.substring(4) + "考勤加分");
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
