package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.common.AppCons;
import com.trasen.imis.dao.TbAttenceCountMapper;
import com.trasen.imis.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhangxiahui on 17/7/6.
 */
@Component
public class AttenceCountService {

    Logger logger = Logger.getLogger(AttenceCountService.class);

    @Autowired
    TbAttenceCountMapper tbAttenceCountMapper;

    @Autowired
    TbHolidayService tbHolidayService;

    public void countAttence(String date){
        //获取需要统计的所有用户
        List<TbWeixinUser> userList = tbAttenceCountMapper.queryWeixinUser();
        if(userList!=null&&date!=null){
            logger.info("====本次统计人数为["+userList.size()+"]人");
            for(TbWeixinUser user : userList){
                Map<String,Object> paraMap = new HashMap<>();
                paraMap.put("name",user.getName());
                paraMap.put("workNum",user.getWorkNum());
                paraMap.put("date",date);
                logger.info("====开始统计["+user.getName()+"]的考勤数据");
                //通过date+name查询统计表
                TbAttenceCount count = null;
                TbAttenceCount tbAttenceCount = tbAttenceCountMapper.getAttenceCount(paraMap);
                if (tbAttenceCount!=null){
                    count = tbAttenceCount;
                }else {
                    count = new TbAttenceCount();
                }
                int annualLeave = 0;//'年假',
                int maternityLeave = 0;// '产假',
                int peternituLeave = 0;// '陪产假',
                int maritalLeave = 0;// '婚假',
                int funeralLeave = 0;// '丧假',
                int affairLeave = 0;// '事假',
                int sickLeave = 0;// '病假',
                int otherLeave = 0;// '其他假',
                count.setName(user.getName());
                count.setWorkNum(user.getWorkNum());
                count.setTagName(user.getTagName());
                count.setTagId(user.getTagId());
                count.setCountDate(date);
                String year=date.substring(0,4);
                String month=date.substring(4,6);
                int singnNum=tbHolidayService.getDaysforYearMonth(year+"-"+month.replace("0",""),user.getTagId());
                count.setShouldSignNum(singnNum);
                count.setFactSignNum(tbAttenceCountMapper.getFactSignNum(paraMap));
                count.setAttNum(tbAttenceCountMapper.getAttNum(paraMap));
                count.setLateNum(tbAttenceCountMapper.getLateNum(paraMap));
                count.setBackNum(tbAttenceCountMapper.getBackNum(paraMap));
                count.setLackNum(tbAttenceCountMapper.getLackNum(paraMap));

                List<AttenceLeave> leaveList = tbAttenceCountMapper.queryAttenceLeaveList(paraMap);
                if(leaveList!=null){
                    for (AttenceLeave leave : leaveList){
                        if(leave.getType()== AppCons.ANNUAL_LEAVE){
                            annualLeave++;
                        }else if(leave.getType()== AppCons.MATERNITY_LEAVE){
                            maternityLeave++;
                        }else if(leave.getType()== AppCons.PETERNITU_LEAVE){
                            peternituLeave++;
                        }else if(leave.getType()== AppCons.MARITAL_LEAVE){
                            maritalLeave++;
                        }else if(leave.getType()== AppCons.FUNERAL_LEAVE){
                            funeralLeave++;
                        }else if(leave.getType()== AppCons.AFFAIR_LEAVE){
                            affairLeave++;
                        }else if(leave.getType()== AppCons.SICK_LEAVE){
                            sickLeave++;
                        }else if(leave.getType()== AppCons.OTHER_LEAVE){
                            otherLeave++;
                        }
                    }
                }
                count.setAnnualLeave(annualLeave);
                count.setMaternityLeave(maternityLeave);
                count.setPeternituLeave(peternituLeave);
                count.setMaritalLeave(maritalLeave);
                count.setFuneralLeave(funeralLeave);
                count.setAffairLeave(affairLeave);
                count.setSickLeave(sickLeave);
                count.setOtherLeave(otherLeave);
                count.setCreated(new Date());
                if(count.getPkid()!=null){
                    tbAttenceCountMapper.updateAttenceCount(count);
                }else{
                    tbAttenceCountMapper.saveAttenceCount(count);
                }
            }

        }
    }

    public List<TbAttenceCount> queryAttenceCountList(TbAttenceCount count, Page page){
        List<TbAttenceCount> list = new ArrayList<>();
        if(count!=null){
            list = tbAttenceCountMapper.queryAttenceCountList(count,page);
        }
        return list;
    }
    public List<TbAttenceCount> queryAttenceCountList(TbAttenceCount count){
        List<TbAttenceCount> list = new ArrayList<>();
        if(count!=null){
            list = tbAttenceCountMapper.queryAttenceCountList(count);
        }
        return list;
    }

    public List<AttenceDetailVo> queryAttenceDetail(Map<String,Object> param, Page page){
        List<AttenceDetailVo> list = new ArrayList<>();
        if(param!=null&&param.get("countType")!=null){
            logger.info("===查看考勤明细==["+param.get("countType")+"]===["+param.get("type")+"]===["+param.get("date")+"]===");
            if("attence".equals(param.get("countType"))){
                list = tbAttenceCountMapper.queryAttenceDetail(param,page);
            }else if("lack".equals(param.get("countType"))){
                list = tbAttenceCountMapper.queryLackDetail(param,page);
            }else if("leave".equals(param.get("countType"))){
                list = tbAttenceCountMapper.queryLeaveDetail(param,page);
            }
        }
        return list;
    }

}
