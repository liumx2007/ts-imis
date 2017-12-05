package com.trasen.imis.service;

import cn.trasen.commons.util.DateUtil;
import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.cache.GlobalCache;
import com.trasen.imis.dao.TbAttenceLogMapper;
import com.trasen.imis.dao.TbAttenceMapper;
import com.trasen.imis.model.*;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhangxiahui on 17/6/21.
 */
@Component
public class MobileAttenceService {

    Logger logger = Logger.getLogger(MobileAttenceService.class);

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    @Autowired
    TbAttenceMapper tbAttenceMapper;

    @Autowired
    TbAttenceLogMapper tbAttenceLogMapper;

    @Autowired
    AttenceService attenceService;


    public AttenceVo getWeixinUser(String openId){
        AttenceVo attenceVo = null;
        if(openId!=null){
            attenceVo = tbAttenceMapper.getWeixinUser(openId);
        }
        return attenceVo;
    }

    public AttenceVo getAttenceToday(String openId){
        AttenceVo attenceVo = null;
        if(openId!=null){
            attenceVo = getWeixinUser(openId);
            if(attenceVo!=null){
                attenceVo.setAttenceDate(DateUtil.getDateTime("yyyy-MM-dd"));
                AttenceVo attenceVoToday = tbAttenceMapper.getAttenceToday(attenceVo);
                if(attenceVoToday!=null){
                    return attenceVoToday;
                }else{
                    return attenceVo;
                }
            }
        }
        return attenceVo;
    }



    public AttenceVo signIn(AttenceLogVo attenceLogVo){
        if(attenceLogVo!=null&&attenceLogVo.getOpenId()!=null){
            //处理考勤地址,如果公司考勤则显示在'湖南创星科技股份有限公司'附近
            if(attenceLogVo.getAddress()!=null){
                String [] addresses = attenceLogVo.getAddress().split("\\[");
                if(addresses.length==2){
                    if(addresses[1].indexOf("梦洁家纺(谷苑路店)")!=-1&&!"sign".equals(attenceLogVo.getType())){
                        attenceLogVo.setAddress(addresses[0]+"[湖南创星科技股份有限公司]");
                    }
                    if(addresses[1].indexOf("麓谷国际工业园")!=-1&&!"sign".equals(attenceLogVo.getType())){
                        attenceLogVo.setAddress(addresses[0]+"[湖南创星科技股份有限公司]");
                    }
                    if(addresses[1].indexOf("湖南远越农业信息化科技有限公司")!=-1&&!"sign".equals(attenceLogVo.getType())){
                        attenceLogVo.setAddress(addresses[0]+"[湖南创星科技股份有限公司]");
                    }
                }
            }
            //获取考勤规则
            String key = attenceLogVo.getTagId();
            if(key!=null&&!StringUtil.isEmpty(key)){
                TbAttenceLocation rule = attenceService.getAttenceRule(key,globalCache.getAttRuleMap());
                if(rule==null){
                    logger.info("===姓名["+attenceLogVo.getName()+"]没有匹配到考勤规则,考勤失败");
                    return null;
                }
                Integer type = 0;
                String today = DateUtil.getDateTime("yyyy-MM-dd");
                String todayT = DateUtil.getDateTime("yyyyMMdd");
                String outDate = rule.getOutDate();
                if(outDate!=null&&outDate.indexOf(today)>0){
                    type = 1;//在排除日期内为加班
                }else if(isHoliday(todayT)
                        ||(rule.getWorkingDay()!=null&&rule.getWorkingDay().indexOf(attenceLogVo.getAttenceWeek())==-1)){
                    type = 1;//在法定节假日,或者非工作日时间算加班
                }else{
                    type = 0;
                }

                String singInTimeStr = today+" 08:45:00";
                String singOutTimeStr = today+" 17:45:00";
                if(rule.getSigninTime()!=null){
                    singInTimeStr = today+" "+rule.getSigninTime();
                }
                if(rule.getSignoutTime()!=null){
                    singOutTimeStr = today+" "+rule.getSignoutTime();
                }

                if(!"sign".equals(attenceLogVo.getType())){
                    logger.info("======公司考勤=====验证地点==");
                    if(rule.getLatitude()!=null&&rule.getLongitude()!=null&&rule.getRange()!=null){
                        float latitude = rule.getLatitude()*10000;
                        float longitude = rule.getLongitude()*10000;
                        int range = rule.getRange();
                        float latLog = attenceLogVo.getLatitude()*10000;
                        float lonLog = attenceLogVo.getLongitude()*10000;
                        logger.info("======公司考勤=====考勤坐标["+longitude+","+latitude+"]======范围["+range+"]=");
                        logger.info("======公司考勤=====要验证的坐标["+lonLog+","+latLog+"]=======");
                        if(latLog<latitude-range||latLog>latitude+range){
                            logger.info("======公司考勤=====验证地点["+lonLog+","+latLog+"]=======超出纬度范围");
                            return null;
                        }
                        if(lonLog<longitude-range||lonLog>longitude+range){
                            logger.info("======公司考勤=====验证地点["+lonLog+","+latLog+"]=======超出精度范围");
                            return null;
                        }
                    }else{
                        logger.info("======公司考勤=====匹配到的考勤规则未设置地址坐标,无法考勤=====");
                        return null;
                    }

                }else{
                    logger.info("======外出考勤=====不验证地点==");
                }
                AttenceVo attenceVo = getAttenceToday(attenceLogVo.getOpenId());
                if(attenceVo!=null){
                    attenceVo.setType(type);
                    if(attenceVo.getSigninTime()!=null){
                        Date signinTime = attenceVo.getSigninTime();
                        Date signoutTime = new Date();
                        Long time = DateUtil.getTimeBetween(signoutTime,signinTime)/3600;
                        Integer workHours = Integer.parseInt(time.toString());
                        attenceVo.setSignoutTime(signoutTime);
                        //签退地址
                        attenceVo.setSignoutAddress(attenceLogVo.getAddress());
                        attenceVo.setWorkHours(workHours);
                        attenceVo.setOperator(attenceLogVo.getOpenId());
                        Date outTime = DateUtil.stringToDate(singOutTimeStr);
                        if(signoutTime.before(outTime)){
                            long backTime = DateUtil.getTimeBetween(outTime,signoutTime)/60;
                            attenceVo.setBackTime(backTime);
                            if(attenceLogVo.getType()!=null&&!"sign".equals(attenceLogVo.getType())){
                                attenceLogVo.setType("outEx");
                            }
                        }
                        tbAttenceLogMapper.insertAttenceLog(attenceLogVo);
                        tbAttenceMapper.updateAttenceSignoutTime(attenceVo);
                        return attenceVo;
                    }else{
                        attenceVo.setIsVaild(1);
                        Date inTime = DateUtil.stringToDate(singInTimeStr);
                        Date attinTime = new Date();
                        if(inTime.before(attinTime)){
                            Long time = DateUtil.getTimeBetween(attinTime,inTime)/60;
                            attenceVo.setLateTime(time);
                            if(attenceLogVo.getType()!=null&&!"sign".equals(attenceLogVo.getType())){
                                attenceLogVo.setType("inEx");
                            }
                            //迟到处理
                            //查该同学有没有请假,如果请假自动改状态
                            Integer ifLeave = tbAttenceMapper.ifLeave(attenceVo.getName());
                            if(ifLeave>0){
                                attenceVo.setIsVaild(0);
                            }
                        }
                        attenceVo.setAttenceDate(attenceLogVo.getAttenceDate());
                        attenceVo.setWeek(attenceLogVo.getAttenceWeek());
                        attenceVo.setSigninTime(attinTime);
                        //外出事由
                        if(attenceLogVo.getRemark()!=null){
                            attenceVo.setRemark(attenceLogVo.getRemark());
                        }
                        //签到地址
                        attenceVo.setSigninAddress(attenceLogVo.getAddress());
                        attenceVo.setSigninType(attenceLogVo.getType());
                        tbAttenceLogMapper.insertAttenceLog(attenceLogVo);
                        tbAttenceMapper.insertAttence(attenceVo);
                        return attenceVo;
                    }
                }

            }

        }
        return null;
    }

    public List<AttenceLogVo> queryAttLogList(AttenceLogVo attLog){
        List<AttenceLogVo> list = new ArrayList<>();
        if(attLog.getOpenId()!=null&&attLog.getAttenceDate()!=null){
            list = tbAttenceLogMapper.queryAttLogList(attLog);
        }
        return list;
    }

    public boolean isHoliday(String date){
        logger.info("==获取节假日["+date+"]");
        String requestUrl = PropertiesUtils.getProperty("holiday_url").replace("DATE",date);

        String result = HttpUtil.connectURL(requestUrl,"","GET");
        logger.info("==获取节假日["+result+"]");
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        Integer data = jsonObject.getInteger("data");
        if(data!=null&&data.intValue()!=2){
            return false;
        }
        return true;
    }

    public List<TbPersonnel> getSubPerson(String openId,String name){
        List<TbPersonnel> list = new ArrayList<>();
        AttenceVo attenceVo = getWeixinUser(openId);
        if(attenceVo!=null){
            attenceVo.setRemark(name);
            list = tbAttenceMapper.getSubPerson(attenceVo);
        }
        return list;
    }

    public Integer getUserIdToOPenId(String openId){
        return tbAttenceMapper.getUserIdToOPenId(openId);
    }



}
